package edu.ycp.cs320.sme.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.sme.controller.SscheduleViewControl;
import edu.ycp.cs320.sme.model.User;

public class SscheduleEdit extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/_view/SscheduleView.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Decode form parameters and dispatch to controller
    String errorMessage = null;
    User user = null;
    
    
    try {
      int User = getIntFromParameter(req.getParameter("student_id"));

      if (User <0) {
        errorMessage = "Please specify an actual student id";
      } else {
        SscheduleViewControl controller = new SscheduleViewControl();
        user = controller.fetchUser(User);
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
    req.getRequestDispatcher("/_view/addNumbers.jsp").forward(req, resp);
  }

  private int getIntFromParameter(String s) {
    if (s == null || s.equals("")) {
      return -1;
    } else {
      return Integer.parseInt(s);
    }
  }
}
