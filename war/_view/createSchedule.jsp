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
		  <li><a href="studentView">View your schedule</a></li>
		  <li><a class="active" href="studentCreate">Create new schedule</a></li>
		  <!-- <li><a href="#about">About</a></li> -->
		</ul>
	</div>
	
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
						  <option value="Summer I">Summer 1</option>
						  <option value="Summer II">Summer 2</option>
						</select>
					</td>
				</tr>
			</table>
			<input type="Submit" name="submit" value="Create schedule">
		</form>
	
	
	</body>
	

</html>