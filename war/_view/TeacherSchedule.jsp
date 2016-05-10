
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>View Schedule</title>
		<link rel="stylesheet" type="text/css" href="webCSS.css">
	<style type="text/css"></style>
	</head>
	
	<body>
	<nav>
		<div class="nav">
		<img class="logoImage">
		<ul id="nav">
		  <li><a href="teacherHome.html">Home</a></li>
		</ul>
		</div>
		</nav>
	
	
	
	

	<div> <p class="text-stroke-175">Hello,  ${name}</p> </div>
	<br>

	
	<br>					
		
	<table class="course-Table">
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