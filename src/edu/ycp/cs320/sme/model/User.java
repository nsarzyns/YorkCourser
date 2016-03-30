package edu.ycp.cs320.sme.model;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	//9 digit unique id for storing in DB
	private int uniqueID;
	
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public int getUniqueID(){
		return uniqueID;
	}
	public String getUniqueID_toS(){
		return String.valueOf(uniqueID);
	}
	public int generateUniqueID(){
		//find a value between 0-999,999,999
		return (int) ((Math.random()+0.000000001) *999999999);
	}
	
	public void setUniqueID(int i){
		uniqueID = i;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
