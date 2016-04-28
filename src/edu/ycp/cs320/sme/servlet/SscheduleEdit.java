package edu.ycp.cs320.sme.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.sql.DatabaseProvider;
import edu.ycp.cs320.sme.sql.IDatabase;
import edu.ycp.cs320.sme.controller.SscheduleEditControl;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.User;
import edu.ycp.cs320.sme.model.Course.Subject;
import edu.ycp.cs320.sme.model.Student;

public class SscheduleEdit extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
	//check if system is holding a user in the session, if not redirect to "login" page
	  	Student student = null;
	  	User usrTemp = null;
	  	if((usrTemp = (User) req.getSession().getAttribute("user")) == null){
	  		//don't do anything - student will stay null
	  	}else if(usrTemp instanceof Student){
	  		student = (Student) req.getSession().getAttribute("user");
	  	}
	  	//Redirect if still null
	  	if(student == null){
	  		resp.sendRedirect("./index.html");
	  		return;
	  	}
	  	
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
  
  Student user = (Student) req.getSession().getAttribute("user");
  //CRN of the course that will be added to schedule
  IDatabase db = DatabaseProvider.getInstance();
  int desiredCourseNum;
  Course courseToAdd = null;
  title = controller.parseString( req.getParameter("title") );
  
  //add course (via crn) to student's current schedule
  if((req.getParameter("added_crn")) != null){
	  desiredCourseNum = Integer.parseInt(req.getParameter("added_crn"));
	  courseToAdd = db.getCourseFromCRN(desiredCourseNum);
	  
	  //cheat-y way to hide error message
	  title = "notNull";
	  // Param if course has been added
	  req.setAttribute("done",true);
	  
	  if(courseToAdd.available(user)){
		  //add course to schedule
		  user.getSelectedSchedule().addCourse(courseToAdd);
		  db.updateStudent(user);
		  System.out.println(courseToAdd.getCourseNum() + " added to students schedule");
	  }
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
	  courseList= db.queryCourses(crn, tSub, title);
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
