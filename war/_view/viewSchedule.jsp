<%-- --  Parameters
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

--%>

<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>View Schedule</title>
		<link rel="stylesheet" type="text/css" href="webCSS.css">
		<link href='https://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
	<style>
		table, th, td {
   			border: 1px solid black;
		}
	</style>
	</head>
	
	<body>
		<form action="${pageContext.servletContext.contextPath}/studentView" method="post" id="view">
					<table>
						<%-- <tr>
							<td class="label">Student ID:</td>
						</tr>
						
						--%>
						
						<tr>
							<th class="label">CRN</th>
							<th class="label">Subject</th>
							<th class="label">Course Number</th>
							<th class="label">Title</th>
							<th class="label">Credits</th>
							<th class="label">Type</th>
							<th class="label">Location</th>
							<th class="label">Days</th>
							<th class="label">Instructor</th>
						</tr>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.CRN}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
													<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.subject}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.courseNum}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.title}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.credits}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${null}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.room}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${null}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>
							<td>
								<c:if test="${! empty courseList}">
		 	 						<c:forEach items="${courseList}" var="current">
										<div class="course"> 
									<c:out value="${current.instructor}"/> 
										</div>
	  								</c:forEach>
								</c:if>
							</td>

	</table>
	</body>