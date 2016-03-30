<%-- --  Parameters
Get scheudule 
See how to handle objects
Pass list of course

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
		#nav {
	    list-style-type: none;
	    margin: 0;
	    padding: 0;
	    overflow: hidden;
	    background-color: #5dd34f;
		}
		
		li {
		    float: left;
		}
		.active {
		 	background-color: #41aa4b;
		}
		
		li a {
		    display: block;
		    color: white;
		    text-align: center;
		    padding: 14px 16px;
		    text-decoration: none;
		}
		
		li a:hover {
		    background-color: #3c9531;
		}
	</style>
	</head>
	
	<body>
	<div>
		<ul id="nav">
		  <li><a href="studentHome.html">Home</a></li>
		  <li><a href="studentEdit">Add classes</a></li>
		  <li><a class="active" href="studentView">View your schedule</a></li>
		  <li><a href="studentCreate">Create new schedule</a></li>
		  <!-- <li><a href="#about">About</a></li> -->
		</ul>
	</div>
	<div> <p style="font-size:20px">Hello,  ${name}</p> </div>
	<div>Schedule name: ${scheduleName}<br><br>
	
	
	</div>
		
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
					<c:out value="${current.instructor.name}"/> 
						</div>
							</c:forEach>
				</c:if>
			</td>

	</table>
	</body>