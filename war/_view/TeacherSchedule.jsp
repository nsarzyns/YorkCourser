
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>View Schedule</title>
		<link rel="stylesheet" type="text/css" href="webCSS.css">
		<link href='https://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
	<style type="text/css">
		table, th, td {
   			border: 1px solid black;
		}
		#nav {
		font-weight: bold;
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
		  <li><a href="teacherHome.html">Home</a></li>
		  <li class="logoImage"><img src="./Images/ycplogo_1.png" width="237.5" style="padding-left: 100%" alt=""/></li>
		</ul>
	</div>
	<div> <p style="font-size:20px">Hello,  ${name}</p> </div>
	<br><br>

	
	<br>					
		
	<table>
		<tr>
			<c:choose>
				<c:when test="${! empty courseList}">
					<th class="label">CRN</th>
					<th class="label">Subject</th>
					<th class="label">Course Number</th>
					<th class="label">Title</th>
					<th class="label">Credits</th>
					<th class="label">Type</th>
					<th class="label">Location</th>
					<th class="label">Days</th>
				</c:when>
			<c:otherwise>
				<h2>ADD COURSES TO BEGIN</h2></c:otherwise>
			</c:choose>
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

	</table>
	</body>