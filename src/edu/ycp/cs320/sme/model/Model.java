package edu.ycp.cs320.sme.model;

import edu.ycp.cs320.sme.model.Student;
import java.util.ArrayList;

public class Model{
	private ArrayList<Student> user_id;
	
	public Model(){
		this.user_id = new ArrayList<Student>();
	}
	
	public ArrayList<Student> getUserIDs(){
		return this.user_id;
	}
	
	public void setUserIDs(ArrayList<Student> newUserIDs){
		this.user_id = newUserIDs;
	}
}