package edu.ycp.cs320.sme.controller;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.Teacher;
import edu.ycp.cs320.sme.sql.DatabaseProvider;
import edu.ycp.cs320.sme.sql.IDatabase;

/**
 * Class responsible for handling all actions teacher servlets computations and assignments
 * 
 *
 */
public class TeacherController {

	public Teacher getTeacher(){
		IDatabase db = DatabaseProvider.getInstance();
		Teacher firstTryT = db.fetchTeacher("Hake");
		if (firstTryT != null){
			return firstTryT;
		}
		System.out.println("teacher is null");
		return firstTryT;
		
	}

	public Teacher changeSelectedSchedule(Teacher teacher, String parameter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
