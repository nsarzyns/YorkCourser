package edu.ycp.cs320.sme.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.sql.DBmain;
import edu.ycp.cs320.sme.sql.DatabaseProvider;
import edu.ycp.cs320.sme.sql.IDatabase;

public class DatabaseTests {

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
		assertEquals(true,"\"Levy, Peter\"".equals(c.getInstructor().getName()));
	
		//bad search returns null
		List<Course> nullList = db.queryCourses(9, null, null);
		assertEquals(nullList, null);

		
	}
	@Test
	public void getSingleCourse() {
		IDatabase db = DatabaseProvider.getInstance();
		//get a real course - Fall 2016,10100,BIO,240.102,Genetics,4,LEC,CH 203,M,,W,,F,,,"Kaltreider, Ronald"
		Course c = db.getCourseFromCRN(10100);
		
		assertEquals(Subject.BIO,c.getSubject() );
		assertEquals("240.102",c.getCourseNum());
		assertEquals(true,"Genetics".equals(c.getTitle()));
		char[] days = c.getDays();
		assertEquals(days[0],'M');
		assertEquals(days[2], 'W');
		assertEquals(days[4], 'F');
		assertEquals(days[6], '\0');
		assertEquals(true,"\"Kaltreider, Ronald\"".equals(c.getInstructor().getName()));
	
		//get a course not found in DB
		Course q = db.getCourseFromCRN(21012);
		assertEquals(q,null);
		
		
	}
}
