package edu.ycp.cs320.sme.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.Teacher;
import edu.ycp.cs320.sme.sql.DBmain;
import edu.ycp.cs320.sme.sql.DatabaseProvider;
import edu.ycp.cs320.sme.sql.IDatabase;

public class DatabaseTests {

	@BeforeClass
	public static void buildup() throws IOException{
		DBmain db = new DBmain();
		db.main(null);
	
		DatabaseProvider.setInstance(db);
		
	}
	
	@Test
	public void CourseQueryTest() {
		
		IDatabase db = DatabaseProvider.getInstance();
		//Fall 2016,10015,FYS,100.115,RACE JUSTICE AMERICA,3,LEC,HUM 144,M,,W,,,,,"Levy, Peter"
		List<Course> course = db.queryCourses(10015, null, null);
		//should contain only one course
		Course c = course.get(0);
		
		assertEquals(Subject.FYS,c.getSubject() );
		assertEquals("100.115",c.getCourseNum());
		assertEquals(true,"RACE JUSTICE AMERICA".equals(c.getTitle()));
		char[] days = c.getDays();
		assertEquals(days[0],'M');
		assertEquals(days[2], 'W');
		assertEquals(days[6], '\0');
		
		course = db.queryCourses(3, null, "work");
		//should contain only one course
		c = course.get(0);
		System.out.println(c.getTitle() +" "+ c.getInstructor().getName());
			
		//bad search returns null
		List<Course> nullList = db.queryCourses(9, null, null);
		assertEquals(nullList, null);

		
	}
	@Test
	public void getSingleCourse() {
		IDatabase db = DatabaseProvider.getInstance();
		//get a real course - 10462,FIN,310.801,Real Estate Finance,3,LEC,T,06:30PM- 09:15PM,225,WBC 225,Gregory T,25,8
		Course c = db.getCourseFromCRN(10462);
		
		assertEquals(Subject.FIN,c.getSubject() );
		assertEquals("310.801",c.getCourseNum());
		assertEquals(true,"Real Estate Finance".equals(c.getTitle()));
		char[] days = c.getDays();
		assertEquals(days[0],'\0');
		assertEquals(days[1], 'T');
		assertEquals(days[4], '\0');
		assertEquals(true,"Gregory, T".equals(c.getInstructor().getName()));
	
		//get a course not found in DB
		Course q = db.getCourseFromCRN(21012);
		assertEquals(q,null);
		
		
	}
	@Test
	public void testFetchTeacher(){
		IDatabase db = DatabaseProvider.getInstance();
		Teacher t = null;
		t = db.fetchTeacher("Hake");
		assertEquals("Hake, D",t.getName());
		System.out.println(t.getName());
		t = db.fetchTeacher("hake");
		assertEquals(false, t == null);
		t = db.fetchTeacher("xxxx");
		assertEquals(null, t);
		
	}
}
