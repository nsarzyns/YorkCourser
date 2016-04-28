package edu.ycp.cs320.sme.unitTests;

import static org.junit.Assert.*;


import org.junit.Test;

import edu.ycp.cs320.sme.controller.StudentController;

import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;


public class StudentControllerTest {
	

	@Test
	public void changeScheduleTest(){
		Student student = new Student();
		StudentController controller = new StudentController();
		Schedule schedule1 = new Schedule();
		Schedule schedule2 = new Schedule();
		
		schedule1.setName("name1");
		schedule2.setName("name2");
		student.addSchedule(schedule1);
		student.setSelectedSchedule(schedule1);
		
		student.addSchedule(schedule2);
		
		assertEquals(student.getSelectedSchedule().getName(), "name1");
		student = controller.changeSelectedSchedule(student, null);
		assertEquals(student.getSelectedSchedule().getName(), "name1");
		
		student = controller.changeSelectedSchedule(student, "name1");
		assertEquals(student.getSelectedSchedule().getName(), "name1");
		
		student = controller.changeSelectedSchedule(student, "name2");
		assertEquals(student.getSelectedSchedule().getName(), "name2");
	}
}
