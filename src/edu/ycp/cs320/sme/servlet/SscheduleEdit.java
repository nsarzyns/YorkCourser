package edu.ycp.cs320.sme.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.sme.controller.SscheduleEditControl;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.sql.DBmain;

public class SscheduleEdit extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
	  	//Display edit schedule jsp
	      req.getRequestDispatcher("/_view/editSchedule.jsp").forward(req, resp);
}

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
	
	SscheduleEditControl controller = new SscheduleEditControl();
  // Decode form parameters and dispatch to controller
  String errorMessage = null;
  String title = null;
  Subject subject = null;
  int crn = -1;
  List<Course> courseList = new LinkedList<Course>();
  
  Student user = null;
  //CRN of the course that will be added to schedule
  int desiredCourseNum;
  Course courseToAdd = null;
  title = controller.parseString( req.getParameter("title") );
  
  //add course (via crn) to student's current schedule
  if((req.getParameter("added_crn")) != null){
	  desiredCourseNum = Integer.parseInt(req.getParameter("added_crn"));
	 
	  try {
		courseToAdd = DBmain.getCourseFromCRN(desiredCourseNum);
	  } catch (ClassNotFoundException | SQLException e) {
		e.printStackTrace(); }

	  //cheat-y way to hide error message
	  title = "notNull";
	  // Param if course has been added
	  req.setAttribute("done",true);
	  System.out.println(courseToAdd.getCourseNum());
	  
	  req.getRequestDispatcher("/_view/editSchedule.jsp").forward(req, resp);
	  return;
  }
  
  String tSub = controller.parseString( req.getParameter("subject") );
  if(tSub != null ){
	  subject = Subject.valueOf(tSub);
  }
  try {
  	crn = getIntFromParameter(req.getParameter("crn"));
  } catch (NumberFormatException e) {
    //errorMessage = "Invalid Student ID";
  }
  if(title == null && crn < 0 && subject == null){
	  errorMessage = "Give at least one search parameter";
  }else{
	  try {
		courseList=  DBmain.queryCourses(crn, tSub, title);
	} catch (ClassNotFoundException | SQLException e) {
		e.printStackTrace();
	}
  }

  // Add parameters as request attributes
  //req.setAttribute("student_id", req.getParameter("student_id"));
  req.setAttribute("errorMessage", errorMessage);
  
  // Add result objects as request attributes
  req.setAttribute("courseList", courseList);

  // Forward to view to render the result HTML document
  req.getRequestDispatcher("/_view/editSchedule.jsp").forward(req, resp);
}

private int getIntFromParameter(String s) {
  if (s == null || s.equals("")) {
    return -1;
  } else {
    return Integer.parseInt(s);
  }
}
}
