package edu.ycp.cs320.sme.model;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends User{
	//CRN's of the classes they teach
	private ArrayList<Course> classList = new ArrayList<Course>();
	
	public Teacher(){

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

	public List<String> getScheduleNameList() {
		// TODO Auto-generated method stub
		return null;
	}

	public User getSelectedSchedule() {
		// TODO Auto-generated method stub
		return null;
	}
}
