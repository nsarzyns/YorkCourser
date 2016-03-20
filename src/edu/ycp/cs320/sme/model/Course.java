package edu.ycp.cs320.sme.model;

import java.util.HashSet;
import java.util.Set;
/*
 * Remember: compare courses with subject and courseNum
 */
public class Course {
	//time <hour(0-23), minute>
	private int startTimeH,startTimeM;
	private int finTimeH,finTimeM;
	//represent days of the week course takes place Mon(0)-Sun(6)
	private char[] days;
	//A must for all courses 
	private String title;
	private String courseNum;
	private int credits;
	private Subject subject;
	private int adr;
	
	private int CRN;
	private int currSeats =0, maxSeats;
	private String roomNum;
	private Teacher instructor;
	//TODO set this up properly
	//private Set<Course> p;
	private Set<Student> students = new HashSet<Student>();
	
	public Course(){
		days = new char[7];
	}
	
	public enum Subject {
		ACC,ANT,ART,BEH,BIO,BUS,CHM,MLS,CM,CS,CJA,ESS,ECO,EDU,ECH,MLE,SE,SPE,EGR,
		ECE,ME,ENT,FLM,FIN,FYS,FCM,G,GER,HIS,HSP,HSV,HUM,IFS,IA,IBS,INT,FRN,GRM,
		ITL,LAT,RUS,SPN,LIT,MGT,MKT,MAT,MUS,NM,NUR,PHL,PE,PSC,PHY,PS,PMD,PSY,QBA,RAD,
		REC,REL,RT,SOC,SPM,SCM,SES,THE,WGS,FCO,WRT
	}
	public int getAdr(){
		return this.adr;
	}
	public int getStartTimeH() {
		return startTimeH;
	}
	public int getStartTimeM() {
		return startTimeM;
	}
	public int getFinTimeH() {
		return finTimeH;
	}
	public int getFinTimeM() {
		return finTimeM;
	}
	public int getCurrSeats() {
		return currSeats;
	}
	public int getMaxSeats() {
		return maxSeats;
	}
	public String getRoom() {
		return roomNum;
	}
	public int getCredits() {
		return credits;
	}
	public Subject getSubject() {
		return subject;
	}
	public Teacher getInstructor() {
		return instructor;
	}
	public int getCRN() {
		return CRN;
	}
	public String getCourseNum() {
		return courseNum;
	}
	public String getTitle() {
		return title;
	}
	public char[] getDays() {
		return days;
	}
	public void setDays(char[] days) {
		this.days = days;
	}
	public void setDay(String day,int idx) {
		if(day != null && !day.equals("")){
			days[idx] = day.charAt(0);
		}
	}
	public void setAdr(int adr){
		this.adr = adr;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}
	public void setCRN(int cRN) {
		CRN = cRN;
	}
	public void setInstructor(Teacher instructor) {
		this.instructor = instructor;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public void setRoom(String roomNum) {
		this.roomNum = roomNum;
	}
	public void setMaxSeats(int maxSeats) {
		this.maxSeats = maxSeats;
	}
	public void setCurrSeats(int currSeats) {
		this.currSeats = currSeats;
	}
	public void setFinTimeM(int finTimeM) {
		this.finTimeM = finTimeM;
	}
	public void setFinTimeH(int finTimeH) {
		this.finTimeH = finTimeH;
	}
	public void setStartTimeM(int startTimeM) {
		this.startTimeM = startTimeM;
	}
	public void setStartTimeH(int startTimeH) {
		this.startTimeH = startTimeH;
	}
	public void addStudent(Student s){
		students.add(s);
	}
	public Set<Student> getStudents(){
		return students;
	}
	public String getSubject_toS(){
		return subject.toString();
	}
	//################ End of getters/Setters #################

	
	public boolean available(){
		if(currSeats >= maxSeats){
			return false;
		}
		
		return true;
	}
	public boolean available(Student s){
		if(currSeats >= maxSeats && !students.equals(s)){
			return false;
		}
		
		return true;
	}
	
	//comparator using course number and subject
	public boolean equals(Course one, Course two){
		if(one.getCourseNum() == two.getCourseNum() && one.getSubject() == two.getSubject()){
			return true;
		}
		
		return false;
	}
}
