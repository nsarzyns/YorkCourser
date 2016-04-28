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
import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;
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
			"INSERT INTO courses(CRN, Semester,Subject,Title,courseObject) VALUES (?,?,?,?,?)";

	private static final String SQL_insertUser = 
			"INSERT INTO users (isTeacher,uniqueID,firstName,lastName,email) VALUES (?,?,?,?,?)";
	
	private static final String SQL_updateCourse = 
			"UPDATE courses SET courseObject = ? WHERE CRN = ? ";
	
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
	private void wipeTables() throws SQLException{
		executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{

			Statement stmt1= conn.createStatement();
			Statement stmt2= conn.createStatement();
			Statement stmt3= conn.createStatement();
			
		
			DatabaseMetaData dbData1 = conn.getMetaData();
			ResultSet tables = dbData1.getTables(null, null, "SCHEDULES", null);
			if(tables.next()){
				System.out.println("wiping schedules");
				stmt1.executeUpdate("Drop table SCHEDULES"); 
			}
			
			DatabaseMetaData dbData2 = conn.getMetaData();
			ResultSet tables2 = dbData2.getTables(null, null, "COURSES", null);
			if(tables2.next()){
				stmt2.executeUpdate("Drop table COURSES"); 
				System.out.println("wiping courses");
			}
			DatabaseMetaData dbData3 = conn.getMetaData();
			ResultSet tables3 = dbData3.getTables(null, null, "USERS", null);
			if(tables3.next()){
				stmt3.executeUpdate("Drop table USERS"); 
				System.out.println("wiping users");
			}
			
			DBUtil.closeQuietly(conn);
			DBUtil.closeQuietly(tables);
			DBUtil.closeQuietly(tables2);
			DBUtil.closeQuietly(tables3);
			DBUtil.closeQuietly(stmt1);
			DBUtil.closeQuietly(stmt2);
			DBUtil.closeQuietly(stmt3);
			return true;
			} 
		});
	}
	private Connection connect() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		
		// Set autocommit to false to allow multiple the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	
	
	public void initTables() throws FileNotFoundException{
		
		BufferedReader reader = new BufferedReader(new FileReader(new File ("./war/Courses.csv")));
		BufferedReader readerDup = new BufferedReader(new FileReader(new File ("./war/Courses.csv")));
		try {
			//wipeTables();
			buildUserTable(readerDup);
			makeCourseTable();
			createSchedulesJunction();
			buildCourseTable(reader);
			
			reader.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} 
		
	}
	private void createSchedulesJunction() throws IOException, SQLException {
		executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				PreparedStatement stmt1 = null;
				
				DatabaseMetaData dbData = conn.getMetaData();
				ResultSet tables = dbData.getTables(null, null, "SCHEDULES", null);
				if(tables.next()){
					//Courses table exists
					System.out.println("[Schedules] table exists - Exiting");
					
					//conn.createStatement().executeUpdate("Drop table SCHEDULES"); 
					return false;
					
				}
				//Going to need to create a schedule junction table
				stmt1 = conn.prepareStatement(
						"create table schedules ( "
						+ "userID int constraint U_Id references users, "
						+ "courseID int constraint C_Id references courses "
						+ "sch_num int )");
				stmt1.executeUpdate();
				return true;
			}
		});
	}
	
	private void buildUserTable (final BufferedReader reader) throws IOException, SQLException {
		executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				
				ResultSet resultSet1 = null;
				ResultSet resultSet2 = null;
				
				
				DatabaseMetaData dbData = conn.getMetaData();
				ResultSet tables = dbData.getTables(null, null, "USERS", null);
				if(tables.next()){
					//Courses table exists
					System.out.println("[Users] table exists - Exiting");
					
					//conn.createStatement().executeUpdate("Drop table USERS");
					tables.close();
					return false;
					
				}
				tables.close();
				//Going to need to create a courses table
				stmt1 = conn.prepareStatement(
						"create table users ("
						+ "U_Id int primary key GENERATED BY DEFAULT AS IDENTITY, "
						+ "isTeacher int check (isTeacher in (0,1)) NOT NULL, "
						+ "uniqueID int NOT NULL UNIQUE, "
						+ "firstName varchar(255), "
						+ "lastName varchar(255), "
						+ "email varchar(255) )");
				stmt1.executeUpdate();
				System.out.println("[Users] table created");
				
				String line = null;
				String name = null;
				int itr = 0;
				try{
					//skip headers line
					reader.readLine();
					while ((line = reader.readLine()) != null ){
						line = line.trim();
						if (line.equals("")||line.charAt(0) == ','){
							continue;
						}
						String[] tokens = line.split(",");
						name = tokens[10];
						//split into last name, and possibly first name (really just an initial)
						String[] nameTok = name.split("\\s");
						String lastName = nameTok[0];
						String firstName = "";
						if(nameTok.length == 2){
							firstName = nameTok[1];
						}else if(nameTok.length == 3){
							lastName = nameTok[0] +" "+ nameTok[1];
							firstName = nameTok[2];
						}
						stmt2 = conn.prepareStatement(
								"select U_Id from users " +
								"  where users.lastName = ?"
						);
						stmt2.setString(1, lastName);
						//stmt2.setString(2, firstName);
						
						//search db for existing teacher
						resultSet1 = stmt2.executeQuery();
						//Search successfully found existing teacher, don't bother trying to save it again
						if (resultSet1.next()){
							continue;
						}
						//New teacher - Needs to be inserted into DB
						else{
							int uniqueID;
							//Find a uniqueID that has not been generated in table before
							int count = 0;
							do {
								if (count > 0){
									System.err.println("This actually happens.");
								}
								uniqueID = User.generateUniqueID();
								stmt4 = conn.prepareStatement(
										"select U_Id from users " +
										"  where users.uniqueID = ?"
								);
								stmt4.setInt(1, uniqueID);
								resultSet2 = stmt4.executeQuery();
								count += 1;
							} while (resultSet2.next());
							
							stmt3 = conn.prepareStatement(SQL_insertUser);
							stmt3.setInt(1,1); 
							stmt3.setInt(2, uniqueID);
							stmt3.setString(3, firstName);
							stmt3.setString(4, lastName);
							String email = firstName+ lastName + "@ycp.edu";
							stmt3.setString(5, email);
							
							stmt3.executeUpdate();
							//System.out.println("Teacher: " +firstName+ " " +lastName+ ": UID " +uniqueID+ " Email: "+email);	
							itr += 1;
						}
						
						
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally{
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
				}
				
				System.out.println("[Users] tabled filled with " +itr+ " course intructors");
				return true;
			}
		});
	}
	private void makeCourseTable() throws  SQLException {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				
				DatabaseMetaData dbData = conn.getMetaData();
				ResultSet tables = dbData.getTables(null, null, "COURSES", null);
				if(tables.next()){
					//Courses table exists
					System.out.println("[Courses] table exists - Exiting");
					//stmt.executeUpdate("Drop table COURSES"); 
					return false;
					
					
				}
				
				conn.createStatement().execute("create table courses ( " +
							  "C_Id int primary key GENERATED BY DEFAULT AS IDENTITY, " +
							  "CRN int NOT NULL UNIQUE, " +
							  "Semester varchar(25), " +
							  "Subject varchar(3), "+
							  "Title varchar(255), " +
							  "courseObject blob) "   );
				 System.out.println("Created table[Courses] in given database");
				 DBUtil.closeQuietly(conn);
				return true;
			}
		});
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
				PreparedStatement pstmt= null;
				PreparedStatement stmt1 = null;
				PreparedStatement cPKstmt = null;
				ResultSet cPKrs = null;
				ResultSet userPK = null;
				
				
				
				stmt = conn.createStatement();
				//Skip the title line
				try {
					reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
				ResultSet tables = stmt.executeQuery("select count(*) from COURSES ");
				if(tables.next()){
					//Courses table exists
					int rows = tables.getInt(1);
					if(rows != 0){
						System.out.println("[Courses] table all ready populated - Exiting");
						return false;
					}	
					
				}
				String line;
				int count =0;
				try {
					String time,sT,fT;
					while ((line = reader.readLine()) != null){
						line = line.trim();
						if (line.equals("")||line.charAt(0) == ','){
							continue;
						}
						String subject,Semester,Title,Instructor;
						int CRN;
					
						//Create a courseObject to be stored in DB
						Course c = new Course();
						
						//create substrings by commas
						String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
						
						//10854,FLM,216.101,Intro to Film,3,LEC,M W F,10:00AM- 10:50AM,218,HUM 218,Harlacher J,35,35
						//Semester-> store in db
						Semester = "Fall 2016";
						//CRN -> store in db
						CRN = Integer.parseInt(tokens[0]);
						c.setCRN(CRN);
						//subject -> store in db
						subject = tokens[1];
						c.setSubject(Subject.valueOf(subject));
						c.setCourseNum(tokens[2]);
						Title = tokens[3];
						c.setTitle(Title);
						c.setCredits(Double.parseDouble(tokens[4]));
						c.setType(tokens[5]);
						c.setRoom(tokens[9]);
						
						String days = null;
						String stockDays = tokens[6];
						if(stockDays.equalsIgnoreCase("TO BE ARRANGED")){
							days = "To be determined";
						}else{
							days += (stockDays.contains("M")? " Mon. ":"");
							days +=  (stockDays.contains("T")? " Tues. ":"");
							days +=  (stockDays.contains("W")? " Wed. ":"");
							days +=  (stockDays.contains("R")? " Thurs. ":"");
							days +=  (stockDays.contains("F")? " Fri. ":"");
						}
						c.setDays(days);
						
						//string value of start and finish time
						time  = tokens[7];
						if(!time.equalsIgnoreCase("TO BE ARRANGED")){
							//seperate into start and finish time (strings)
							sT = time.substring(0, 7);
							fT = time.substring(9);
							c.setStartTimeM(Integer.parseInt(sT.substring(3, 5)));
							c.setFinTimeM(Integer.parseInt(fT.substring(3, 5)));
							int sH,fH;
							sH = Integer.parseInt(sT.substring(0,2));
							fH = Integer.parseInt(fT.substring(0, 2));
							//add 12 to hour if it is in afternoon
							sH = (sT.charAt(5) == 'P' && sH < 12)? sH+12: sH;
							fH = (fT.charAt(5) == 'P' && fH < 12)? fH+12: fH;
							
							c.setStartTimeH(sH);
							c.setFinTimeH(fH);
							//System.out.println(c.getStartTimeH()+":"+ c.getStartTimeM() +" to "+ c.getFinTimeH() +":"+c.getFinTimeM());
						}
						int maxSeats = Integer.parseInt(tokens[11]);
						c.setMaxSeats(maxSeats);
						int currSeats = Integer.parseInt(tokens[12]);
						c.setCurrSeats(currSeats);
						c.setOpenSeat(maxSeats-currSeats);
						
						//Make instructor(Store in db) a new teacher 
						Instructor = tokens[10];
						if(Instructor.length() > 5 || Instructor.contains(" ")){
							Instructor = Instructor.substring(0, Instructor.lastIndexOf(' '));
						}
						stmt1 = conn.prepareStatement("select users.U_Id from users "
								+ "where users.lastName = ? " );
						stmt1.setString(1, Instructor);
						int userID;
						userPK = stmt1.executeQuery();
						if(userPK.next()){
							userID = userPK.getInt(1);
						}else{
							System.err.println("Something has gone horribly wrong for teacher: "+ Instructor);
							break;
						}
					    
					    //turn course into a basic object;
					    ByteArrayOutputStream bos = new ByteArrayOutputStream ();
					    ObjectOutputStream oos = new ObjectOutputStream (bos);
					    oos.writeObject(c);
					    oos.flush ();
					    oos.close ();
					    bos.close ();
					     
					    byte[] CourseData = bos.toByteArray();
					     
					    pstmt = conn.prepareStatement(SQL_insertCourse);
					    
						//CRN, Semester,Subject,Title,Instructor,courseObject
						pstmt.setInt(1,CRN);
						pstmt.setString(2,Semester);
						pstmt.setString(3,subject);
						pstmt.setString(4,Title.toUpperCase());
						//pstmt.setInt(5,userID);
						pstmt.setObject(5, CourseData);
						pstmt.executeUpdate();
						
						cPKstmt = conn.prepareStatement("select C_Id from courses where CRN = ?" );
						cPKstmt.setInt(1, CRN);
						cPKrs = cPKstmt.executeQuery();
						int CoursePK;
						if (cPKrs.next()){
							CoursePK= cPKrs.getInt(1);
						}else{
							System.err.println("Couldn't find course literally just inserted "+ CRN);
							break;
						}

						cPKstmt = conn.prepareStatement("insert into schedules (userID,courseID) VALUES (?,?)" );
						cPKstmt.setInt(1, userID);
						cPKstmt.setInt(2, CoursePK);
						cPKstmt.executeUpdate();
						//System.out.println("Schedules - USERPK=" +userID+ ", COURSEPK="+ CoursePK);
						
						count+=1;
				
					}
				
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}finally{
					DBUtil.closeQuietly(stmt);
					//DBUtil.closeQuietly(tables);
					DBUtil.closeQuietly(pstmt);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(userPK);
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
					stmt = conn.prepareStatement("select courseObject, users.uniqueID, users.firstName, users.lastName, users.email  "
							+ "from courses, users, schedules where "
							+ "(CRN = ? or "
							+ "Subject = ? ) and users.U_Id = schedules.userID and schedules.courseID = courses.C_Id");
					stmt.setInt(1, sCRN);
					stmt.setString(2, sSubject);
				}else{
					stmt = conn.prepareStatement("select courseObject, users.uniqueID, users.firstName, users.lastName, users.email  "
											+ "from courses,users, schedules where "
											+ "(CRN = ? or "
											+ "Subject = ? or "
											+ "Title like ? )and users.U_Id = schedules.userID and schedules.courseID = courses.C_Id"	);
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
						course.setInstructor(buildTeacher(resultSet, 2));
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
	public boolean updateCourse(final Course course) {
		return executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				if(course == null){
					throw new IllegalArgumentException ("updating course with null course");
				}
				PreparedStatement stmt = null;
				stmt = conn.prepareStatement(SQL_updateCourse);
				//TODO: finish the rest of this method (condense course to byteStream)
				DBUtil.closeQuietly(conn);
				return true;
			}
		});
		
	}
	
	@Override
	public Teacher fetchTeacher(final String lastName) {
		return executeTransaction(new Transaction<Teacher>() {
			@Override
			public Teacher execute(Connection conn) throws SQLException {
				String lnFix = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
				//find and build teacher
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
		
				stmt = conn.prepareStatement("select users.U_Id, users.uniqueID, users.firstName, users.lastName, users.email "
												+ "from users where "
												+ "users.IsTeacher = 1 and users.lastName = ?");
				stmt.setString(1, lnFix);
				resultSet = stmt.executeQuery();
				if(!resultSet.next()){
					System.err.println("Teacher not found");
					return null;
				}
				int teacherPK = resultSet.getInt(1);
				Teacher nuTeacher = buildTeacher(resultSet,2);
				
				//get courseList
				PreparedStatement stmt2 = null;
				ResultSet resultSet2 = null;
				stmt2 = conn.prepareStatement("select courseObject "
						+ "from courses, schedules where "
						+ "schedules.userID = ? and schedules.courseID = courses.C_Id " );
				stmt2.setInt(1, teacherPK);
				resultSet2 = stmt2.executeQuery();
				
				int debugCount = 0;
				while (resultSet2.next()){
					Course course = null;
					byte[] buf = resultSet2.getBytes(1);
					ObjectInputStream objectIn = null;
					if (buf != null){
						try {
							debugCount++;
							objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
							course = (Course) objectIn.readObject();
							course.setInstructor(nuTeacher);
							nuTeacher.addClass(course);
							
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
						
					}
				}
				if(debugCount == 0){
					System.err.println("Teacher returned with no classes");
				}
				
				return nuTeacher;
			}
		});
	}
	/**
	 * 
	 * @param student (Must include first/last name and email address)
	 */
	@Override
	public Boolean updateStudent(final Student student){
		return executeTransaction(new Transaction<Boolean>(){
			@Override
			public Boolean execute(Connection conn) throws SQLException{
				PreparedStatement stmt = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				ResultSet resultSet = null;
				ResultSet resultSet2 = null;
				
				Student existing = fetchStudent(null,null,student.getEmail());
				int uniqueID;
				
				//insert/create student if one does not exist
				if(existing == null){
					int count = 0;
					//create a new unique ID and make sure it is not taken
					do {
						if (count > 0){
							System.err.println("This actually happens.");
						}
						uniqueID = User.generateUniqueID();
						stmt2 = conn.prepareStatement(
								"select U_Id from users " +
								"  where users.uniqueID = ?"
						);
						stmt2.setInt(1, uniqueID);
						resultSet2 = stmt2.executeQuery();
						count += 1;
					} while (resultSet2.next());
					
					
					stmt = conn.prepareStatement(SQL_insertUser);
					stmt.setInt(1, 0);
					stmt.setInt(2, uniqueID);
					String name = student.getName();
					String fn, ln;
					if(name.contains(" ")){
						int split = name.lastIndexOf(' ');
						fn = name.substring(0, split);
						ln = name.substring(split).trim();
					}else{
						fn = name;
						ln = "-";
					}
					stmt.setString(3, fn);
					stmt.setString(4, ln);
					stmt.setString(5, student.getEmail());
					stmt.executeUpdate();
				}else{
					uniqueID = existing.getUniqueID();
				}
				List<Course> courses = student.getSelectedSchedule().getCourseList();
				
				stmt3 = conn.prepareStatement("select U_Id from users where users.uniqueID = ? and users.isTeacher = 0" );
				stmt3.setInt(1, uniqueID);
				resultSet = stmt3.executeQuery();
				
				int studentPK;
				if(resultSet.next()){
					studentPK = resultSet.getInt(1);
				}else{
					//Student not found
					System.err.println("Student not found in StudentUpdate(), This shouldn't ever happen");
					return null;
				}
				PreparedStatement courseStmt = null;
				PreparedStatement courseStmtSkip = null;
				PreparedStatement scheduleInsert = null;
				ResultSet CourseRS = null;
				ResultSet existingRS = null;
				for(Course c: courses){
					
					int coursePK;
					courseStmt = conn.prepareStatement("select C_Id from courses where CRN = ?");
					courseStmt.setInt(1, c.getCRN());
					CourseRS = courseStmt.executeQuery();
					
					//if course ID was found via a CRN
					if(CourseRS.next()){
						coursePK = CourseRS.getInt(1);
						
						//skip over duplicates (if the row exists already)
						courseStmtSkip = conn.prepareStatement("select * from schedules where userID = ? and CourseID = ?");
						courseStmtSkip.setInt(1, studentPK);
						courseStmtSkip.setInt(2, coursePK);
						existingRS = courseStmtSkip.executeQuery();
						if(!existingRS.next()){
							
							scheduleInsert = conn.prepareStatement("INSERT into schedules (UserID,CourseID) "
																	+ "values (?,?)	" );
																	
							scheduleInsert.setInt(1, studentPK);
							scheduleInsert.setInt(2,coursePK);
							scheduleInsert.executeUpdate();
						}
					}else{
						;//DO nothing though this shouldn't happen 
					}
					
				}
				DBUtil.closeQuietly(conn);
				DBUtil.closeQuietly(resultSet2);
				DBUtil.closeQuietly(stmt);
				DBUtil.closeQuietly(stmt2);
				DBUtil.closeQuietly(stmt3);
				DBUtil.closeQuietly(resultSet);
				return true;
				
			}
		});
	}
	
	/**
	 * fetch Student from DB
	 * @param email - leave null if unsure value, or bugs will occur
	 */
	@Override
	public Student fetchStudent(final String firstName, final String lastName, final String email){
		return executeTransaction(new Transaction<Student>(){
			@Override
			public Student execute(Connection conn) throws SQLException{
				String fnFix = null;
				String lnFix = null;
				if(firstName != null){
					fnFix = Character.toUpperCase(firstName.charAt(0)) + firstName.substring(1);
				}
				if(lastName != null){
					 lnFix = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
				}
				
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				if(email == null){
					stmt = conn.prepareStatement("select users.U_Id, users.uniqueID, users.firstName, users.lastName, users.email "
													+ "from users where "
													+ "users.IsTeacher = 0 and users.firstName = ? and users.lastName = ?");
					stmt.setString(2, lnFix);
					stmt.setString(1, fnFix);
					resultSet = stmt.executeQuery();
				}else{
					stmt = conn.prepareStatement("select users.U_Id, users.uniqueID, users.firstName, users.lastName, users.email "
							+ "from users where "
							+ "users.IsTeacher = 0 and users.email = ?");
					stmt.setString(1, email);
					resultSet = stmt.executeQuery();
				}
				int studentPK;
				if(resultSet.next()){
					studentPK = resultSet.getInt(1);
				}else{
					//Student not found
					return null;
				}
				
				Student s = buildStudent(resultSet, 2);
				
				DBUtil.closeQuietly(stmt);
				DBUtil.closeQuietly(resultSet);
				
				//get all student's courses in schedule table
				PreparedStatement stmt2 = null;
				ResultSet rs2 = null;
				stmt2 = conn.prepareStatement("select courses.courseObject, courses.C_Id "
													+ "from users, schedules, courses where "
													+ "schedules.userID = ? and schedules.courseID = courses.C_Id and schedules.userID = users.U_Id");
				stmt2.setInt(1, studentPK);
				rs2 = stmt2.executeQuery();
				Schedule schedule = new Schedule();
				schedule.setName("Default");
				
				ResultSet rs3 = null;
				PreparedStatement stmt3 = null;
				
				while(rs2.next()){
					//credit to: http://javapapers.com/ for method on deSerialize technique
					byte[] buf = rs2.getBytes(1);
					int coursePK = rs2.getInt(2);
					
					stmt3 = conn.prepareStatement("select users.uniqueID, users.firstName, users.lastName, users.email from "
													+ "users,schedules,courses where "
													+ "users.U_Id = schedules.userID and schedules.courseID = ?");
					stmt3.setInt(1, coursePK);
					rs3 = stmt3.executeQuery();
					rs3.next();
					
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
						course.setInstructor(buildTeacher(rs3, 1));
						schedule.addCourse(course);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				s.addSchedule(schedule);
				s.setSelectedSchedule(schedule);
				
				
				
				
				return s;
				
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
		
				stmt = conn.prepareStatement("select courses.courseObject, users.uniqueID, users.firstName, users.lastName, users.email "
												+ "from courses, users, schedules where "
												+ "CRN = ? and courses.C_Id = schedules.courseID and schedules.userID = users.U_Id");
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
							course.setInstructor(buildTeacher(resultSet,2));
							
							
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
		DBmain db = new DBmain();
		db.initTables();
		
		System.out.println("Success!");
	}
	
	/**
	 * 
	 * @param rs resultSet - Expected order is uniqueID,firstName,lastName,email
	 * @param idx
	 * @return new Student Object (Without schedule)
	 * @throws SQLException
	 */
	private Student buildStudent(ResultSet rs, int idx) throws SQLException{
		Student newS = new Student();
		//uniqueID,firstName,lastName,email
		newS.setUniqueID(rs.getInt(idx++));
		String firstName = rs.getString(idx++);
		String lastName = rs.getString(idx++);
		newS.setName(firstName +" "+ lastName);
		newS.setEmail(rs.getString(idx++));
		return newS;
	}
	
	/**
	 * 
	 * @param rs resultSet - Expected order is uniqueID,firstName,lastName,email
	 * @param idx
	 * @return new Teacher
	 * @throws SQLException
	 */
	private Teacher buildTeacher(ResultSet rs, int idx) throws SQLException{
		Teacher ins = new Teacher();
		//uniqueID,firstName,lastName,email
		ins.setUniqueID(rs.getInt(idx++));
		String firstName = rs.getString(idx++);
		ins.setName(rs.getString(idx++) +", "+ firstName);
		ins.setEmail(rs.getString(idx++));
		return ins;
	}

	
}
