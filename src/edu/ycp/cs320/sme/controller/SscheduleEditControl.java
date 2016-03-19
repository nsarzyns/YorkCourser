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

public class SscheduleEditControl {
	/*TODO methods: fetch student from ID
	 * getLastSchedule(Student)
	 * firstClasstime(schedule)
	 * lastClasstime(schedule)
	 */
	private Student student = new Student();
	
	public SscheduleEditControl(File f){
		student.setName("John Smith");
		student.setEmail("coolBeans@ycp.edu");
		//initialize this new "student"
		Schedule sched = new Schedule();
		//sched.setLastModified(new Date());
		 try{
	            BufferedReader reader = new BufferedReader(new FileReader(f));
	            //Skip first line - titles
	            reader.readLine();
	            String line = "";
	            //Loop line by line
	            while((line = reader.readLine()) != null){
	            	Course c = new Course();
	            	String[] tokens = line.split(",");
	            	c.setCRN(Integer.parseInt(tokens[1]));
	            	c.setSubject(Subject.valueOf(tokens[2]));
	            	c.setCourseNum(tokens[3]);
	            	c.setTitle(tokens[4]);
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
	            	//Make instructor a new teacher
	            	String inst = tokens[15] + tokens[16];
	                Teacher t = new Teacher();
	                t.setName(inst);
	                c.setInstructor(t);
	                
	                sched.addCourse(c);
	            }
	            // after loop, close reader
				reader.close();
			
	        }catch (FileNotFoundException e){
	            e.printStackTrace();
	        } catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 student.addSchedule(sched);
		 student.setSelectedSchedule(sched);
	}

	
	public Student fetchStudent(int i){
		//TODO: change this to work with actual users from DB in future
		return student;
	}
	
}