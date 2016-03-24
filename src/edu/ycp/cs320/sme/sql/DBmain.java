package edu.ycp.cs320.sme.sql;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Teacher;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.sql.DBUtil;

/*
 * This class is responsible for handling all sql interactions including
 * building table if it doesn't exist, queries, and handling exceptions 
 */
public class DBmain {
	//A list of lists, easy for passing 
	static class RowList extends ArrayList<List<String>> {
		private static final long serialVersionUID = 1L;
	}
	private static final String SQL_insert = 
			"INSERT INTO courses(CRN, Semester,Subject,Title,Instructor,courseObject) VALUES (?,?,?,?,?,?)";
	
	public static void initTables() throws FileNotFoundException, SQLException{
		Connection conn = null;
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.err.println("Could not load Derby JDBC driver");
			System.err.println(e.getMessage());
			return;
		}
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		conn.setAutoCommit(true);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File ("./war/Fall2016_ScheduleOfClasses_20160315.csv")));
		try {
			buildCourseTable(conn,reader);
			reader.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} 
		
	}
	/**
	 * Take csv file into a BufferedReader and build course table (only if it does not exist)
	 * 
	 */
	private static void buildCourseTable(Connection conn, BufferedReader reader) 
			throws IOException, SQLException {
		Statement stmt = null;
		stmt = conn.createStatement();
		//Skip the title line
		reader.readLine();
		String line;
		
		DatabaseMetaData dbData = conn.getMetaData();
		ResultSet tables = dbData.getTables(null, null, "COURSES", null);
		if(tables.next()){
			//Courses table exists
			return;
		}else{
			//Going to need to create a courses table
			String make = "create table courses ( " +
						  "C_Id int PRIMARY KEY, " +
						  "CRN int NOT NULL UNIQUE, " +
						  "Semester varchar(25), " +
						  "Subject varchar(3), "+
						  "Title varchar(255), " +
						  "Instructor varchar(255), "+
						  "courseObject blob);";
			
			stmt.executeUpdate(make);
		    System.out.println("Created table[Courses] in given database");
		}
		
		
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
        	c.setCredits(Integer.parseInt(tokens[5]));
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

            PreparedStatement pstmt = conn.prepareStatement(SQL_insert);
            
			//CRN, Semester,Subject,Title,Instructor,courseObject
    		pstmt.setInt(1,CRN);
    		pstmt.setString(2,Semester);
    		pstmt.setString(3,subject);
    		pstmt.setString(4,Title);
    		pstmt.setString(5,Instructor);
    		pstmt.setObject(6, c);
    		pstmt.executeUpdate();
			
		}
		
	}
	/**
	 * 
	 * @param CRN
	 * @param subject
	 * @param title
	 * @return array list of Course objects found from search 
	 * @throws SQLException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static List<Course> queryCourses(int CRN,String subject,String title) 
			throws SQLException, ClassNotFoundException, IOException{
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			System.err.println("Could not load Derby JDBC driver");
			System.err.println(e.getMessage());
			return null;
		}
		if(CRN < 9999 && subject == null && title == null){
			System.err.println("empty course search, give at least one proper search");
			return null;
		}

		int sCRN = (CRN > 9999)? CRN:-1;
		String sSubject = (subject != null)? subject:"zzz";
		String sTitle = (title != null)? title:"zzz";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		// connect to the database
		conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
		conn.setAutoCommit(true);
		stmt = conn.prepareStatement("select courseObject "
								+ "from courses where "
								+ "CRN = ? or "
								+ "Subject = ? or "
								+ "Title = ?"	);
		stmt.setInt(1, sCRN);
		stmt.setString(2, sSubject);
		stmt.setString(3, sTitle);
		
		resultSet = stmt.executeQuery();
		List<Course> courseList = new LinkedList<Course>();
		while(resultSet.next()){
			//credit to: http://javapapers.com/ for method on deSerialize technique
			byte[] buf = resultSet.getBytes(resultSet.getRow());
			ObjectInputStream objectIn = null;
			if (buf != null){
				objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
			}
			Course course = (Course) objectIn.readObject();
			courseList.add(course);
		}
		DBUtil.closeQuietly(resultSet);
		DBUtil.closeQuietly(stmt);
		DBUtil.closeQuietly(conn);
		
		return courseList;
	}
}
