package edu.ycp.cs320.sme.model;

import java.util.Date;
import java.util.Set;

public class Schedule {
	private String name;
	private String semester;
	private Set<Course> courseList;
	private Date lastModified;

	public String getName() {
		return name;
	}

	public Set<Course> getCourseList() {
		return courseList;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setCourseList(Set<Course> courseList) {
		this.courseList = courseList;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addCourse(Course c){
		courseList.add(c);
	}
	//TODO check if all courses in list are still available to take
	public void update(){
		throw new UnsupportedOperationException(":TODO make me work");
	}
}
