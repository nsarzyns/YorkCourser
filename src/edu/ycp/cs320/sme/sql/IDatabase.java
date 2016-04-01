package edu.ycp.cs320.sme.sql;

import java.util.List;

import edu.ycp.cs320.sme.model.Course;

public interface IDatabase {
	List<Course> queryCourses(int CRN,String subject,String title);
	Course getCourseFromCRN(int CRN);
	
}
