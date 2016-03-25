package edu.ycp.cs320.sme.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.Teacher;
/*
 * Controller in charge of adding a class to the current schedule from student
 */
public class SscheduleEditControl {
	
	public SscheduleEditControl(){
		
	}
	/**
	 * Procedure to cut down annoying, empty strings
	 * @param val
	 * @return null for empty and null strings, otherwise the input string
	 */
	public String parseString(String val){
		if (val == null || val.equals("")){
			return null;
		}
		return val;
	}
	/*
	 * Add course to schedule with crn value
	 * Need to connect to DB
	 * pull course via crn and add course to schedule
	 */
	public void addClass(int crn){
		
	}

	
}