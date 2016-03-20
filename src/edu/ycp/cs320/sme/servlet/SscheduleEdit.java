package edu.ycp.cs320.sme.servlet;

import java.io.File;
import java.io.IOException;
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
  
  //Create dummy course to test display
  Course c = new Course();
  c.setTitle("Girls, Girls, Girls");
  
  Student user = null;
  
  try {
    int User = getIntFromParameter(req.getParameter("student_id"));

  } catch (NumberFormatException e) {
    errorMessage = "Invalid Student ID";
  }

  // Add parameters as request attributes
  req.setAttribute("student_id", req.getParameter("student_id"));

  // Add result objects as request attributes
  req.setAttribute("errorMessage", errorMessage);
  req.setAttribute("user", user);

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
