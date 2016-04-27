package edu.ycp.cs320.sme.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.controller.StudentController;
import edu.ycp.cs320.sme.controller.TeacherController;
import edu.ycp.cs320.sme.model.Course;
import edu.ycp.cs320.sme.model.Student;
import edu.ycp.cs320.sme.model.Teacher;
import edu.ycp.cs320.sme.model.User;

public class TscheduleView extends HttpServlet {
	 
	@Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
		  
		  	//check if system is holding a user in the session, if not redirect to "login" page
		  	Teacher teacher = null;
		  	User usrTemp = null;
		  	if((usrTemp = (User) req.getSession().getAttribute("Hake")) == null){
		  		//don't do anything - teacher will stay null
		  	}else if(usrTemp instanceof Teacher){
		  		teacher = (Teacher) req.getSession().getAttribute("Hake");
		  	}
		  	//Redirect if still null
		  	if(teacher == null){
		  		resp.sendRedirect("./index.html");
		  		return;
		  	}
		  	handleRequest(teacher, req, resp);
	       
	  }

	 private void handleRequest(Teacher teacher, HttpServletRequest req, HttpServletResponse resp) {
		 List<Course> courses = null;
		  if (teacher.getSelectedSchedule().getCourseList() != null){
			 // courses = teacher.getSelectedSchedule().getCourseList();
		  }
	      String SelScheduleName = teacher.getSelectedSchedule().getName();
	      List<String> nameList = teacher.getScheduleNameList();

		    // Add result objects as request attributes
		   // req.setAttribute("errorMessage", errorMessage);
		    req.setAttribute("name", teacher.getName());
		    req.setAttribute("scheduleName", SelScheduleName);
		    req.setAttribute("courseList", courses);
		    req.setAttribute("nameList", nameList);

		    // Forward to view to render the result HTML document
		    req.getRequestDispatcher("/_view/Teacher.jsp").forward(req, resp);
		
	}
	
	@Override
	  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
		  
		  Teacher teacher = (Teacher) req.getSession().getAttribute("Hake");
		  TeacherController controller = new TeacherController();
		  
		  teacher = controller.changeSelectedSchedule(teacher, req.getParameter("schedule"));
		  req.getSession().setAttribute("Hake", teacher);
		  
		  handleRequest(teacher,req,resp);
		  
	  }
	
	
}
