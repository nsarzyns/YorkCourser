package edu.ycp.cs320.sme.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Student extends User{
	private int totalCredits;
	//set of all passed classes (and classes currently being taken)
	private Set<Course> transcript;
	//Limit how many schedules a user can have or else algorithm will get out of hand
	private LinkedList<Schedule> scheduleList = new LinkedList<Schedule>();
	private Schedule selectedSchedule;
	//TODO Is course override by CRN? If so turn this into List of CRN's
	private LinkedList<Course> coursesOverrode= new LinkedList<Course>();
	
	//private String major;

	public int getTotalCredits() {
		return totalCredits;
	}
	public Set<Course> getTranscript() {
		return transcript;
	}
	public List<Schedule> getScheduleList(){
		return scheduleList;
	}
	public Schedule getScheduleByN(String name){
		for (Schedule s: scheduleList){
			if(s.getName().toLowerCase().equals(name.toLowerCase())){
				return s;
			}
		}
		//A schedule by this name does not exist
		return null;
	}
	//return a list of strings of names of each schedule, selected schedule being first
	public List<String> getScheduleNameList(){
		List<String> names = new LinkedList<String>();
		names.add(selectedSchedule.getName());
		for(Schedule s : scheduleList ){
			if (!s.equals(selectedSchedule)){
				names.add(s.getName());
			}
		}
		return names;
	}
	public void addSchedule(Schedule s){
		scheduleList.add(s);
	}
	public List<Course> getCoursesOverrode() {
		return coursesOverrode;
	}
	
	public Schedule getSelectedSchedule() {
		return selectedSchedule;
	}
	public void setSelectedSchedule(Schedule selectedSchedule) {
		this.selectedSchedule = selectedSchedule;
	}
	public void setCoursesOverrode(LinkedList<Course> coursesOverrode) {
		this.coursesOverrode = coursesOverrode;
	}
	public void overrideClass(Course c){
		coursesOverrode.add(c);
	}
	public void setTranscript(Set<Course> transcript) {
		this.transcript = transcript;
	}
	public void addToTranscript(Course c){
		transcript.add(c);
	}
	public void addToTranscript(Set<Course> set){
		transcript.addAll(set);
	}
	public void setTotalCredits(int totalCredits) {
		this.totalCredits = totalCredits;
	}
	public void setScheduleList (LinkedList<Schedule> schedule){
		this.scheduleList = schedule;
	}
	
}
