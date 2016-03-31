package edu.ycp.cs320.sme.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.controller.SscheduleViewControl;
import edu.ycp.cs320.sme.controller.StudentController;
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
	  	handleRequest(student, req, resp);
       
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
	  Student student = (Student) req.getSession().getAttribute("user");
	  StudentController controller = new StudentController();
	  
	  student = controller.changeSelectedSchedule(student, req.getParameter("schedule"));
	  req.getSession().setAttribute("user", student);
	  
	  handleRequest(student,req,resp);
	  
  }
  private void handleRequest(Student student, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
	  List<Course> courses = null;
	  if (student.getSelectedSchedule().getCourseList() != null){
		  courses = student.getSelectedSchedule().getCourseList();
	  }
      String SelScheduleName = student.getSelectedSchedule().getName();
      List<String> nameList = student.getScheduleNameList();

	    // Add result objects as request attributes
	   // req.setAttribute("errorMessage", errorMessage);
	    req.setAttribute("name", student.getName());
	    req.setAttribute("scheduleName", SelScheduleName);
	    req.setAttribute("courseList", courses);
	    req.setAttribute("nameList", nameList);

	    // Forward to view to render the result HTML document
	    req.getRequestDispatcher("/_view/viewSchedule.jsp").forward(req, resp);
  }

  private int getIntFromParameter(String s) {
    if (s == null || s.equals("")) {
      return -1;
    } else {
      return Integer.parseInt(s);
    }
  }
}
