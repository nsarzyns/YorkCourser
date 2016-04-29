package edu.ycp.cs320.sme.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/*
 * Remember: compare courses with subject and courseNum
 */
public class Course implements Serializable{

	private static final long serialVersionUID = 1L;
	//time <hour(1-24), minute>
	private int startTimeH,startTimeM;
	private int finTimeH,finTimeM;
	private String days;
	private String timeStr = "";
	//A must for all courses 
	private String title;
	private String courseNum;
	private double credits;
	private Subject subject;
	private int adr;
	private String type;
	
	private int CRN;
	private int currSeats =0, openSeat=0, maxSeats=1;
	private String roomNum;
	private Teacher instructor;
	//TODO set this up properly
	//private Set<Course> p;
	private List<Student> students = new LinkedList<Student>();
	private List<Student> sAllowedOverride = new LinkedList<Student>();
	private List<Student> pendingOverride = new LinkedList<Student>();
	
	public Course(){
	
	}
	
	public enum Subject {
		ACC,ANT,ART,BEH,BIO,BUS,CHM,MLS,CM,CS,CRW,CJA,ESS,ECO,EDU,ECH,MLE,SE,SPE,EGR,
		ECE,ME,ENT,FLM,FIN,FYS,FCM,G,GER,HIS,HSP,HSV,HUM,IFS,IA,IBS,INT,FRN,GRM,
		ITL,LAT,RUS,SPN,LIT,MBA,MED,MGT,MKT,MAT,MUS,NM,NUR,PHL,PE,PSC,PHY,PS,PMD,PSY,QBA,RAD,
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
	public double getCredits() {
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
	public String getDays() {
		return days;
	}
	public String getType () {
		return type;
	}
	public List<Student> getStudentOverride() {
		return sAllowedOverride;
	}
	public int getOpenSeat() {
		return openSeat;
	}
	public String getTimeStr() {
		String result = "";
		result = startTimeH+ ":" +startTimeM+ " - "+finTimeH+":"+finTimeM; 
		return result;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public void setOpenSeat(int openSeat) {
		this.openSeat = openSeat;
	}
	public void addToStudentOverride(Student s) {
		sAllowedOverride.add(s);
	}
	public void setStudentOverride(List<Student> sAllowedOverride) {
		this.sAllowedOverride = sAllowedOverride;
	}
	public void setDays(String days) {
		this.days = days;
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
	public void setCredits(double d) {
		credits = d;
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
		if(!students.contains(s)){
			students.add(s);
		}
	}
	public List<Student> getStudents(){
		return students;
	}
	public String getSubject_toS(){
		return subject.toString();
	}
	public void setType(String type) {
		this.type = type;
	}
	//################ End of getters/Setters #################


	public boolean available(Student s){
		//if student is all ready in this course (in another schedule)
		if(students.contains(s)){
			return true;
		}
		if(sAllowedOverride.contains(s)){
			return true;
		}
		if(currSeats >= maxSeats){
			return false;
		}
		
		return true;
	}
	public String time_ToS(){
		String result = "";
		result = startTimeH+ ":" +startTimeM+ " - "+finTimeH+":"+finTimeM; 
		return result;
	}
	
	//comparator using course number and subject
	public boolean equals(Course one, Course two){
		if(one.getCourseNum() == two.getCourseNum() && one.getSubject() == two.getSubject()){
			return true;
		}
		
		return false;
	}
}
