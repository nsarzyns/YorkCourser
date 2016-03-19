package edu.ycp.cs320.sme.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.controller.SscheduleViewControl;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Student;

public class SscheduleView extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  	//Eventually fetch a real ID, but for now     req.getParameter("student_id")
	  	int userID = 903123456;
	    Student student = null;
	    SscheduleViewControl controller = new SscheduleViewControl(new File("./war/Student_test.csv"));
        student = controller.fetchStudent(userID);
        List<Course> courses = student.getSelectedSchedule().getCourseList();
	    // Add parameters as request attributes
	    req.setAttribute("student_id", userID);

	    // Add result objects as request attributes
	   // req.setAttribute("errorMessage", errorMessage);
	    req.setAttribute("name", student.getName());
	    req.setAttribute("courseList", courses);

	    // Forward to view to render the result HTML document
	    req.getRequestDispatcher("/_view/SscheduleView.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
    // Decode form parameters and dispatch to controller
    String errorMessage = null;
    Student user = null;
    
    try {
      int User = getIntFromParameter(req.getParameter("student_id"));

      if (User <0) {
        errorMessage = "Please specify an actual student id";
      } else {
        SscheduleViewControl controller = new SscheduleViewControl(new File("./war/Student_test.csv"));
        user = controller.fetchStudent(User);
      }
    } catch (NumberFormatException e) {
      errorMessage = "Invalid Student ID";
    }

    // Add parameters as request attributes
    req.setAttribute("student_id", req.getParameter("student_id"));

    // Add result objects as request attributes
    req.setAttribute("errorMessage", errorMessage);
    req.setAttribute("user", user);

    // Forward to view to render the result HTML document
    req.getRequestDispatcher("/_view/SscheduleView.jsp").forward(req, resp);
  }

  private int getIntFromParameter(String s) {
    if (s == null || s.equals("")) {
      return -1;
    } else {
      return Integer.parseInt(s);
    }
  }
}
