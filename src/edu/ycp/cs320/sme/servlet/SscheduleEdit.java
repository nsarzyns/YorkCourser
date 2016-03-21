package edu.ycp.cs320.sme.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.controller.SscheduleEditControl;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.Student;

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
  
  //Create dummy course to test display
  Course c = new Course();
  c.setTitle("Test Course");
  c.setCRN(10234);
  c.setSubject(Subject.ACC);
  c.setCourseNum("151.101");
  courseList.add(c);
  
  Student user = null;
  //CRN of the course that will be added to schedule
  int desiredCourse;
  
  title = req.getParameter("title");
  
  if((req.getParameter("added_crn")) != null){
	  System.out.println(req.getParameter("added_crn"));
	  
	  //cheat-y way to hide error message
	  title = "notNull";
	  // Param if course has been added
	  req.setAttribute("done",true);
  }
  
   String tSub = req.getParameter("subject");
  if(tSub != null && !tSub.equals("") ){
	  subject = Subject.valueOf(tSub);
  }
  try {
  	crn = getIntFromParameter(req.getParameter("crn"));
  } catch (NumberFormatException e) {
    //errorMessage = "Invalid Student ID";
  }
  if((title == null || title.equals("")) && crn < 0 && subject == null){
	  errorMessage = "Give at least one search parameter";
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
