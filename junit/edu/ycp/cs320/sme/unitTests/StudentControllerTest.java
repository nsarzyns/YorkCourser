package edu.ycp.cs320.sme.unitTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import edu.ycp.cs320.sme.controller.SscheduleViewControl;
import edu.ycp.cs320.sme.controller.StudentController;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.User;
import edu.ycp.cs320.sme.model.Course.Subject;

public class StudentControllerTest {
	
	@Test
	public void buildStudentTest(){
		Student student;
		StudentController controller = new StudentController();
		student = controller.buildStudent();
		
		assertEquals(true,new File("./war/Student_test.csv").exists());
		assertEquals(4,student.getSelectedSchedule().getCourseList().size());
		Course c = student.getSelectedSchedule().getCourseList().get(2);
		
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
