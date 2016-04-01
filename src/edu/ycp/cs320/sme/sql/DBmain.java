package edu.ycp.cs320.sme.sql;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Teacher;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.User;
import edu.ycp.cs320.sme.sql.DBUtil;

/*
 * This class is responsible for handling all sql interactions including
 * building table if it doesn't exist, queries, and handling exceptions 
 * 
 * NOTE: Titles are stored in DB as all uppercase, searching is case sensitive
 */
public class DBmain implements IDatabase{
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}
	
	private static final String SQL_insertCourse = 
			"INSERT INTO courses(CRN, Semester,Subject,Title,Instructor,courseObject) VALUES (?,?,?,?,?,?)";
	
	private static final String SQL_insertUser = 
			"INSERT INTO users (UserID,userObject) VALUES (?,?)";
	
	//Update userObject whenever edited param1 = new UserObject and param2 = userID
	private static final String SQL_updateUser = 
			"UPDATE users SET userObject = ? WHERE UserID = ? ";
	
	
	private static final int MAX_ATTEMPTS = 10;
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn){
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
	
	public <ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
			
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
			
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		
		// Set autocommit to false to allow multiple the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	
	
	public void initTables() throws FileNotFoundException{
		
		BufferedReader reader = new BufferedReader(new FileReader(new File ("./war/Fall2016_ScheduleOfClasses_20160315.csv")));
		try {
			buildCourseTable(reader);
			reader.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} 
		
	}
	public static User getUserFromDB(int UserID) 
			throws SQLException, IOException, ClassNotFoundException{
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.err.println("Could not load Derby JDBC driver");
			System.err.println(e.getMessage());
			return null;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		// connect to the database
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		conn.setAutoCommit(true);

		stmt = conn.prepareStatement("select userObject "
										+ "from users where "
										+ "UserID = ? ");
		stmt.setInt(1, UserID);
		resultSet = stmt.executeQuery();
		
		User user = null;
		if (resultSet.next()){
			byte[] buf = resultSet.getBytes(1);
			ObjectInputStream objectIn = null;
			if (buf != null){
				objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
				user = (User) objectIn.readObject();
			}
		}else{
			System.err.println("User not found via UserID");
		}
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
		
		return user;
	}
	/**
	 * Take csv file into a BufferedReader and build course table (only if it does not exist)
	 * 
	 */
	private void buildCourseTable(final BufferedReader reader) throws IOException, SQLException {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException{
					Statement stmt = null;
					stmt = conn.createStatement();
					//Skip the title line
					try {
						reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String line;
					
					DatabaseMetaData dbData = conn.getMetaData();
					ResultSet tables = dbData.getTables(null, null, "COURSES", null);
					if(tables.next()){
						//Courses table exists
						System.out.println("[Courses] table exists - Exiting");
						stmt.executeUpdate("Drop table COURSES");
						return false;
					}else{
						//Going to need to create a courses table
						String make = "create table courses ( " +
									  "C_Id int GENERATED BY DEFAULT AS IDENTITY, " +
									  "CRN int NOT NULL UNIQUE, " +
									  "Semester varchar(25), " +
									  "Subject varchar(3), "+
									  "Title varchar(255), " +
									  "Instructor varchar(255), "+
									  "courseObject blob) "   ;
									// +  "PRIMARY KEY (C_Id))" ;
						
						stmt.executeUpdate(make);
					    System.out.println("Created table[Courses] in given database");
					}
					
					int count =0;
					try {
						while ((line = reader.readLine()) != null){
							line = line.trim();
							if (line.equals("")){
								continue;
							}
							String subject,Semester,Title,Instructor;
							int CRN;
						
							//Create a courseObject to be stored in DB
							Course c = new Course();
							
							//,(?=([^\"]*\"[^\"]*\")*[^\"]*$) - seperate by comma if an even number of quotes follow it
							// Not 100% this works, probably needs to get debugged
							//very inefficient- If stuff starts breaking or grinding to a halt, look here
							String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
							
							//Semester-> store in db
							Semester = tokens[0];
							//CRN -> store in db
							CRN = Integer.parseInt(tokens[1]);
							c.setCRN(CRN);
							//subject -> store in db
							subject = tokens[2];
							c.setSubject(Subject.valueOf(subject));
							c.setCourseNum(tokens[3]);
							Title = tokens[4];
							c.setTitle(Title);
							c.setCredits(Double.parseDouble(tokens[5]));
							c.setRoom(tokens[7]);
							//monday
							c.setDay(tokens[8], 0);
							c.setDay(tokens[9], 1);
							c.setDay(tokens[10], 2);
							c.setDay(tokens[11], 3);
							c.setDay(tokens[12], 4);
							c.setDay(tokens[13], 5);
							//sunday
							c.setDay(tokens[14], 6);
							//Make instructor(Store in db) a new teacher 
							Instructor = tokens[15];
						    Teacher t = new Teacher();
						    t.setName(Instructor);
						    c.setInstructor(t);
						    //turn course into a basic object;
						    ByteArrayOutputStream bos = new ByteArrayOutputStream ();
						    ObjectOutputStream oos = new ObjectOutputStream (bos);
						    oos.writeObject(c);
						    oos.flush ();
						    oos.close ();
						    bos.close ();
						     
						    byte[] CourseData = bos.toByteArray();
						     
						    PreparedStatement pstmt = conn.prepareStatement(SQL_insertCourse);
						    
							//CRN, Semester,Subject,Title,Instructor,courseObject
							pstmt.setInt(1,CRN);
							pstmt.setString(2,Semester);
							pstmt.setString(3,subject);
							pstmt.setString(4,Title.toUpperCase());
							pstmt.setString(5,Instructor);
							pstmt.setObject(6, CourseData);
							pstmt.executeUpdate();
							count+=1;
						}
					} catch (NumberFormatException | IOException e) {
						e.printStackTrace();
					}
						System.out.println(count + " courses added to [Courses] table");
						return true;
			}	
		});
	}
	
	@Override
	public List<Course> queryCourses(final int CRN,final String subject,final String title) {
		return executeTransaction(new Transaction<List<Course>>() {
			@Override
			public List<Course> execute(Connection conn) throws SQLException {
				if(CRN < 9999 && subject == null && title == null){
					System.err.println("empty course search, give at least one proper search");
					return null;
				}
				//If given arguments are bad, make search arguments sure to return no results
				int sCRN = (CRN > 9999)? CRN:-1;
				String sSubject = (subject != null)? subject:"zzz";
				String sTitle = (title == null || title.equals(""))? "zzz":title.toUpperCase();

				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				//title search saps a lot of time in query
				if(title == null){
					stmt = conn.prepareStatement("select courseObject "
							+ "from courses where "
							+ "CRN = ? or "
							+ "Subject = ? ");
					stmt.setInt(1, sCRN);
					stmt.setString(2, sSubject);
				}else{
					stmt = conn.prepareStatement("select courseObject "
											+ "from courses where "
											+ "CRN = ? or "
											+ "Subject = ? or "
											+ "Title like ? "	);
					stmt.setInt(1, sCRN);
					stmt.setString(2, sSubject);
					stmt.setString(3, "%" + sTitle + "%");
				}
				
				resultSet = stmt.executeQuery();
				List<Course> courseList = new LinkedList<Course>();
				while(resultSet.next()){
					//credit to: http://javapapers.com/ for method on deSerialize technique
					byte[] buf = resultSet.getBytes(1);
					ObjectInputStream objectIn = null;
					if (buf != null){
						try {
							objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Course course;
					try {
						course = (Course) objectIn.readObject();
						courseList.add(course);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				DBUtil.closeQuietly(resultSet);
				DBUtil.closeQuietly(stmt);
				DBUtil.closeQuietly(conn);
				
				return courseList;
			}
		});
		
	}
	
	@Override
	public Course getCourseFromCRN(final int CRN) {
		return executeTransaction(new Transaction<Course>() {
			@Override
			public Course execute(Connection conn) throws SQLException {
				
				if(CRN < 9999 && CRN > 100000){
					System.err.println("This is not a valid CRN>> getCourseFromCRN()");
					return null;
				}
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
		
				stmt = conn.prepareStatement("select courseObject "
												+ "from courses where "
												+ "CRN = ? ");
				stmt.setInt(1, CRN);
				resultSet = stmt.executeQuery();
				
				Course course = null;
				if (resultSet.next()){
					byte[] buf = resultSet.getBytes(1);
					ObjectInputStream objectIn = null;
					if (buf != null){
						try {
							objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
							course = (Course) objectIn.readObject();
						} catch (IOException | ClassNotFoundException e) {
						
							e.printStackTrace();
						}
						
					}
				}
				DBUtil.closeQuietly(resultSet);
				DBUtil.closeQuietly(stmt);
				DBUtil.closeQuietly(conn);
				
				return course;
			
			}
			
		});
	}
	public static void main(String[] args) throws IOException {
		System.out.println("DBmain runnable");
		DBmain db = new DBmain();
		db.initTables();
		
		//System.out.println("Loading initial data...");
		//db.loadInitialData();
		
		System.out.println("Success!");
	}
}
