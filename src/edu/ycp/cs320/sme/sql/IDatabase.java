package edu.ycp.cs320.sme.sql;

import java.util.List;

import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.Teacher;

public interface IDatabase {
	List<Course> queryCourses(int CRN,String subject,String title);
	Course getCourseFromCRN(int CRN);
	Teacher fetchTeacher(String lastName);
	boolean updateCourse(Course course);
	Student fetchStudent(String firstName, String lastName, String email);
	Boolean updateStudent(Student student);
	
}
