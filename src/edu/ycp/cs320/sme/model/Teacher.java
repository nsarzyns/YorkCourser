package edu.ycp.cs320.sme.model;
import java.util.ArrayList;

public class Teacher extends User{
	private static final long serialVersionUID = 1L;
	private String teacherName;
	private String email;
	private int uniqueID;
	//CRN's of the classes they teach
	private ArrayList<Course> classList = new ArrayList<Course>();
	
	public Teacher(){
		this.teacherName = "";
		this.email = "";
		this.uniqueID = -1;
		//populate studentList
	}
	
	public void setTeacherName(String newTeacherName){
		this.teacherName = newTeacherName;
	}
	
	public void setTeacherEmail(String newTeacherEmail){
		this.email = newTeacherEmail;
	}
	
	public void setUniqueID(int newID){
		this.uniqueID = newID;
	}
	
	public String getTeacherName(){
		return this.teacherName;
	}
	
	public String getTeacherEmail(){
		return this.email;
	}
	
	public int getUniqueID(){
		return this.uniqueID;
	}
	
	public void addClass(Course newClass){
		this.classList.add(newClass);
	}
	
	public void removeClass(int CRN){
		int classIndex = -1;
		ArrayList<Course> tempCourseList = this.classList;
		//find index of class in class list
		for(int i = 0; i < tempCourseList.size(); i++){
			if(tempCourseList.get(i).getCRN() == CRN){
				classIndex = i;
			}
		}
		//if it exsist remove it, dont need to worry about 
		//removing a class that never exsisted
		if(classIndex != -1){
			this.classList.remove(classIndex);
		}	
	}
}
