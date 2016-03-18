package edu.ycp.cs320.sme.controller;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.User;

public class SscheduleViewControl {
	/*TODO methods: fetch student from ID
	 * getLastSchedule(Student)
	 * firstClasstime(schedule)
	 * lastClasstime(schedule)
	 */
	Student student;
	
	public SscheduleViewControl(){
		//Parse student from temp CSV file
	}
	
	public Schedule getLastSchedule(Student s){
		s.
		
	}
	
	public Model getModel(){
		return this.model;
	}
	
	public User fetchUser(int i){
		//TODO: change this to work with actual users
		return student;
	}
	
	public void addCourse(int i, Course course){
		this.model.getUserIDs().get(i).addToTranscript(course);
	}
}
