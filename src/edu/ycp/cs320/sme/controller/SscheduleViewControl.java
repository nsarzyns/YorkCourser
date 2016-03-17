package edu.ycp.cs320.sme.controller;

import edu.ycp.cs320.sme.model.Model;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.User;

public class SscheduleViewControl {
	/*TODO methods: fetch student from ID
	 * getLastSchedule(Student)
	 * firstClasstime(schedule)
	 * lastClasstime(schedule)
	 */
	private Model model;
	
	public SscheduleViewControl(){
		this.model = new Model();
	}
	
	public void setModel(Model newModel){
		this.model = newModel;
	}
	
	public Model getModel(){
		return this.model;
	}
	
	public User fetchUser(int i){
		return this.model.getUserIDs().get(i);
	}
	
	public void addCourse(int i, Course course){
		this.model.getUserIDs().get(i).addToTranscript(course);
	}
}
