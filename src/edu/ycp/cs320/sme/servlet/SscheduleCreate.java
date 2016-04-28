package edu.ycp.cs320.sme.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.controller.StudentController;
import edu.ycp.cs320.sme.model.Schedule;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.User;

public class SscheduleCreate extends HttpServlet {
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
	  	
	  req.getRequestDispatcher("/_view/createSchedule.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
    // Decode form parameters and dispatch to controller
    String errorMessage = null;
    String name = null;
    String semester = null;
    
    name = req.getParameter("scheduleName");
    semester = req.getParameter("semester");
    
    if(name == null || name.equals("")){
    	//No schedule name was entered!
    	errorMessage = "Please include a schedule name";
    	req.setAttribute("errorMessage", errorMessage);
    	req.getRequestDispatcher("/_view/createSchedule.jsp").forward(req, resp);
    	return;
    }else{
    	Student s = (Student) req.getSession().getAttribute("user");
    	StudentController control = new StudentController();
       	s = control.createSchedule(s, name, semester);
       	req.getSession().setAttribute("user", s);
        resp.sendRedirect("./studentEdit");
    }
  }

}

