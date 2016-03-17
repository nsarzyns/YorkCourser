package edu.ycp.cs320.sme.model;

import java.util.List;
import java.util.Set;

public class Student extends User{
	private int totalCredits;
	//set of all passed classes (and classes currently being taken)
	private Set<Course> transcript;
	//Limit how many schedules a user can have or else algorithm will get out of hand
	private List<Schedule> scheduleList;
	private Schedule selectedSchedule;
	//TODO Is course override by CRN? If so turn this into List of CRN's
	private List<Course> coursesOverrode;
	
	private String major;

	public int getTotalCredits() {
		return totalCredits;
	}
	public Set<Course> getTranscript() {
		return transcript;
	}
	public Schedule getScheduleByN(String name){
		for (Schedule s: scheduleList){
			if(s.getName() == name){
				return s;
			}
		}
		//A schedule by this name does not exist
		return null;
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
	public void setCoursesOverrode(List<Course> coursesOverrode) {
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
}
