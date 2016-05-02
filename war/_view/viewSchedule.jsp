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
		<link rel="stylesheet" type="text/css" href="./webCSS.css">
		
	<style type="text/css"></style>
	</head>
	
	<body>
	<nav>
		<div class="nav">
		<img class="logoImage">
		<ul id="nav">
		  <li><a href="studentHome.html">Home</a></li>
		  <li><a href="studentEdit">Add classes</a></li>
		  <li><a class="active" href="studentView">View your schedule</a></li>
		  <li><a href="studentCreate">Create new schedule</a></li>
		</ul>
		</div>
		</nav>
	<div> <p style="font-size:20px">Hello,  ${name}</p> </div>
	<div>Schedule name: ${scheduleName}
	<br><br>
	Select a new schedule to view and edit: 
			<form method="POST" action="${pageContext.servletContext.contextPath}/studentView" id=changeSch>
							<select name="schedule" form="changeSch" style="height: 23px; width: 150px; ">
								<c:forEach items="${nameList}" var="current"> <br>
									<option value="${current}">${current}</option> 	
								</c:forEach>
							</select>
							<input type="submit" name="submit" value="Select schedule" />
						</form>
	<br>						
	</div>
		
	<table class="course-Table">
		<%-- <tr>
			<td class="label">Student ID:</td>
		</tr>
		
		--%>
		
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
					<th class="label">Instructor</th>
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