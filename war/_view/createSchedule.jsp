<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Create a new schedule option</title>
		<link rel="stylesheet" type="text/css" href="webCSS.css">
		<link href='https://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
		<style type="text/css">
		
			body{
				margin-left: 25px;
			}
			 .error {
				 color: red;
			}
			
			 .course {
				background-color: #66CCFF;
				padding: 15px;
				border-style: groove;
				width: 250px;
				size: 10;
			}
		  
			
		</style>
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
	
	<c:if test="${! empty errorMessage}">
		<div class="error">${errorMessage}</div>
		<br>
	</c:if>
	
	<form action="${pageContext.servletContext.contextPath}/studentCreate" method="post">
			<table>
				<tr>
					<td><input type="text" name="scheduleName" size="12" placeholder="Schedule Name" /></td>
				</tr>
				<tr>
					<td>
						<select name="semester">
						  <option value="Fall 2016">Fall 2016</option>
						</select>
					</td>
				</tr>
			</table>
			<input type="Submit" name="submit" value="Create schedule">
		</form>
	
	
	</body>
	

</html>