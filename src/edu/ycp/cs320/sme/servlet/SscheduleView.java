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
import edu.ycp.cs320.sme.model.User;

public class SscheduleView extends HttpServlet {
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

        List<Course> courses = student.getSelectedSchedule().getCourseList();
        String scheduleName = student.getSelectedSchedule().getName();

	    // Add result objects as request attributes
	   // req.setAttribute("errorMessage", errorMessage);
	    req.setAttribute("name", student.getName());
	    req.setAttribute("scheduleName", scheduleName);
	    req.setAttribute("courseList", courses);

	    // Forward to view to render the result HTML document
	    req.getRequestDispatcher("/_view/viewSchedule.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
    // Decode form parameters and dispatch to controller
    String errorMessage = null;
    Student user = (Student) req.getSession().getAttribute("user");
    
   /* This webpage doesn't require handling posts **YET**
    try {

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
    req.getRequestDispatcher("/_view/viewSchedule.jsp").forward(req, resp);
    */
  }

  private int getIntFromParameter(String s) {
    if (s == null || s.equals("")) {
      return -1;
    } else {
      return Integer.parseInt(s);
    }
  }
}
