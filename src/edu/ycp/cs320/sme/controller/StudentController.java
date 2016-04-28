package edu.ycp.cs320.sme.controller;


import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.sql.DatabaseProvider;
import edu.ycp.cs320.sme.sql.IDatabase;

/*
 * Build student that will (ideally) persist through session
 */
public class StudentController {
	
	/**
	 * 
	 * @return a "john smith", either fetched from DB or built and inserted if hes not found
	 */
	public Student getGenericStudent(){
		IDatabase db = DatabaseProvider.getInstance();
		//Get john smith if he exists
		Student firstTry = db.fetchStudent("John", "Smith", null);
		if (firstTry != null){
			return firstTry;
		}
		//Otherwise create a new john smith
		Student nuStudent = new Student();
		nuStudent.setName("John Smith");
		nuStudent.setEmail("jSmith@ycp.edu");
		//get some BS classes in his schedule
		Schedule nuSchedule = new Schedule();
		nuSchedule.setName("Default0");
		Course course1 = db.getCourseFromCRN(10143);
		nuSchedule.addCourse(course1);
		Course course2 = db.getCourseFromCRN(11309);
		nuSchedule.addCourse(course2);
		Course course3 = db.getCourseFromCRN(10389);
		nuSchedule.addCourse(course3);
		Course course4 = db.getCourseFromCRN(10011);
		nuSchedule.addCourse(course4);
		
		nuStudent.addSchedule(nuSchedule);
		nuStudent.setSelectedSchedule(nuSchedule);
		db.updateStudent(nuStudent);
		
		return nuStudent;
		
	}

	public Student changeSelectedSchedule (Student s, String newSchedule){
		//We don't want to change the schedule this case
		if (newSchedule == null || newSchedule == s.getSelectedSchedule().getName()){
			return s;
		}
		s.setSelectedSchedule(s.getScheduleByN(newSchedule));
		return s;
	}

	public Student createSchedule (Student s, String newSchedule, String semester){
		Schedule newSche = new Schedule();
		newSche.setName(newSchedule);
		newSche.setSemester(semester);
		s.addSchedule(newSche);
		s.setSelectedSchedule(newSche);
		return s;
	}
	
	
	

}
