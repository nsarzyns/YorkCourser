package edu.ycp.cs320.sme.unitTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import edu.ycp.cs320.sme.controller.SscheduleViewControl;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.Student;



public class schViewControllerTest {

	@Test
	public void ContructorTest() {
		assertEquals(true,new File("./war/Student_test.csv").exists());
		SscheduleViewControl controller = new SscheduleViewControl(new File("./war/Student_test.csv"));
		Student s = (Student) controller.fetchStudent(1);
		assertEquals(4,s.getSelectedSchedule().getCourseList().size());
		Course c = s.getSelectedSchedule().getCourseList().get(2);
		
		//Fall 2016,10204,CHM,444.101,Inorganic Chem,3,LEC,CH 103,,T,,R,,,,"Steel, William"
		assertEquals(Subject.CHM,c.getSubject() );
		assertEquals("444.101",c.getCourseNum());
		assertEquals(true,"Inorganic Chem".equals(c.getTitle()));
		char[] days = c.getDays();
		assertEquals(days[1],'T');
		assertEquals(days[2], '\0');
		assertEquals(days[6], '\0');
	}

}
