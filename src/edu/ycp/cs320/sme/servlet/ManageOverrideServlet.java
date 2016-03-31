package edu.ycp.cs320.sme.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.model.Teacher;
import edu.ycp.cs320.sme.model.User;

public class ManageOverrideServlet extends HttpServlet {
	  private static final long serialVersionUID = 1L;
	  
	  @Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
		  
		  //check if system is holding a user in the session, if not redirect to "login" page
		  	Teacher teacher = null;
		  	User usrTemp = null;
		  	if((usrTemp = (User) req.getSession().getAttribute("user")) == null){
		  		//don't do anything - student will stay null
		  	}else if(usrTemp instanceof Teacher){
		  		teacher = (Teacher) req.getSession().getAttribute("user");
		  	}
		  	//Redirect if still null
		  	if(teacher == null){
		  		resp.sendRedirect("./index.html");
		  		return;
		  	}
		  	
		  	req.getRequestDispatcher("/_view/Override.jsp").forward(req, resp);
		  
	  }
	  
	  @Override
	  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {
		  
	  }

}
