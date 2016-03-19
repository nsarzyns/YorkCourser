<!--  Parameters
Get scheudule 
See how to handle objects
Pass list of course

<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
  <head>
    <title>Add Numbers</title>
    <style type="text/css">
    .error {
      color: red;
    }

    td.label {
      text-align: right;
    }
    </style>
  </head>

  <body>
    <c:if test="${! empty errorMessage}">
      <div class="error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.servletContext.contextPath}/addNumbers" method="post">
      <table>
        <tr>
          <td class="label">First number:</td>
          <td><input type="text" name="first" size="12" value="${first}" /></td>
        </tr>
        <tr>
          <td class="label">Second number:</td>
          <td><input type="text" name="second" size="12" value="${second}" /></td>
        </tr>
        <tr>
          <td class="label">Result:</td>
          <td>${result}</td>
        </tr>
      </table>
      <input type="Submit" name="submit" value="Add Numbers!">
    </form>
  </body>
</html>

-->

<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>

<form name="editrecord">

    <% ResultSet rs =(ResultSet)session.getAttribute("resultset"); 
      out.println("this is getAttribute of resultset"+rs);

    %>
     <TABLE BORDER="1">
        <TR>
            <TH>ID</TH>
            <TH>FIRSTNAME</TH>
            <TH>LASTNAME</TH>
            <TH>SUBJECT</TH>
            <TH>YEARS</TH>
        </TR>
        <% while(rs.next()){ %>
        <tr>
 <!--               <td>ID</td> -->
            <td> <input type="text" name="id" value="<%=rs.getString(1) %>"></td>
        </tr>
        <tr>
<!--                <td>FirstName</td> -->
            <td><input type="text" name="firstname" value="<%=rs.getString(2) %>"></td>
        </tr>
        <tr>
<!--                <td>LastName</td> -->
            <td><input type="text" name="lastname" value="<%=rs.getString(3) %>"></td>
        </tr>
        <tr>
<!--                <td>Subject</td> -->
            <td><input type="text" name="subject" value="<%=rs.getString(4) %>"></td>
        </tr>
        <tr>
<!--                <td>years</td> -->
            <td><input type="text" name="years" value="<%=rs.getString(5) %>"></td>

        </tr>           
        <% } %>
    </TABLE>    



 </form>