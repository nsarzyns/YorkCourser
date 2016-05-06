<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Add Courses</title>
		<link rel="stylesheet" type="text/css" href="webCSS.css">
		
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
		
		<link href='https://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
	</head>
	
	<body>
	<%@ page import="edu.ycp.cs320.sme.model.Course" %>

		<nav>
		<div class="nav">
		<img class="logoImage">

	<jsp:useBean id="Course" class="edu.ycp.cs320.sme.model.Course" scope="page"/>

		<ul id="nav">
		  <li><a href="studentHome.html">Home</a></li>
		  <li><a href="studentEdit">Add classes</a></li>
		  <li><a class="active" href="studentView">View your schedule</a></li>
		  <li><a href="studentCreate">Create new schedule</a></li>
		</ul>
		</div>
		</nav>
	
		<div id="header">
			<h1>Search for courses to fit your schedule</h1>
			<h3> ${Csemester}</h3>
		</div>
	
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
			<form action="${pageContext.servletContext.contextPath}/studentEdit" method="post" id="search">
				<table>
					<tr>
						<td class="label">Course name:</td>
						<td><input type="text" name="title" size="12" value="${title}" /></td>
					</tr>
					<tr>
						<td class="label">Subject</td>
						<td>
							<select name="subject" form="search" style="height: 23px; width: 150px; ">
							  <option value="">Search by subject</option>
							  <option value="ACC">ACC</option>
							  <option value="ART">ART</option>
							  <option value="BEH">BEH</option>
							  <option value="BIO">BIO</option>
							  <option value="BUS">BUS</option>
							  <option value="CHM">CHM</option>
							  <option value="MLS">MLS</option>
							  <option value="CM">CM</option>
							  <option value="CS">CS</option>
							  <option value="CJA">CJA</option>
							  <option value="ESS">ESS</option>
							  <option value="ECO">ECO</option>
							  <option value="EDU">EDU</option>
							  <option value="ECH">ECH</option>
							  <option value="MLE">MLE</option>
							  <option value="SE">SE</option>
							  <option value="SPE">SPE</option>
							  <option value="EGR">EGR</option>
							  <option value="ECE">ECE</option>
							  <option value="ME">ME</option>
							  <option value="ENT">ENT</option>
							  <option value="FLM">FLM</option>
							  <option value="FIN">FIN</option>
							  <option value="FYS">FYS</option>
							  <option value="FCM">FCM</option>
							  <option value="G">G</option>
							  <option value="GER">GER</option>
							  <option value="HIS">HIS</option>
							  <option value="HSP">HSP</option>
							  <option value="HSV">HSV</option>
							  <option value="HUM">HUM</option>
							  <option value="IFS">IFS</option>
							  <option value="IA">IA</option>
							  <option value="IBS">IBS</option>
							  <option value="INT">INT</option>
							  <option value="FRN">FRN</option>
							  <option value="GRM">GRM</option>
							  <option value="ITL">ITL</option>
							  <option value="RUS">RUS</option>
							  <option value="SPN">SPN</option>
							  <option value="LIT">LIT</option>
							  <option value="MGT">MGT</option>
							  <option value="MKT">MKT</option>
							  <option value="MAT">MAT</option>
							  <option value="MUS">MUS</option>
							  <option value="NM">NM</option>
							  <option value="NUR">NUR</option>
							  <option value="PHL">PHL</option>
							  <option value="PE">PE</option>
							  <option value="PSC">PSC</option>
							  <option value="PHY">PHY</option>
							  <option value="PS">PS</option>
							  <option value="PMD">PMD</option>
							  <option value="PSY">PSY</option>
							  <option value="QBA">QBA</option>
							  <option value="RAD">RAD</option>
							  <option value="REC">REC</option>
							  <option value="REL">REL</option>
							  <option value="RT">RT</option>
							  <option value="SOC">SOC</option>
							  <option value="SPM">SPM</option>
							  <option value="SCM">SCM</option>
							  <option value="SES">SES</option>
							  <option value="THE">THE</option>
							  <option value="WGS">WGS</option>
							  <option value="FCO">FCO</option>
							  <option value="WRT">WRT</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="label">CRN</td>
						<td><input type="text" name="crn" size="12" value="${crn}" /></td>
					</tr>
				</table>
				<input type="Submit" name="submit" value="Search Courses">
			</form>
		<br>
		<c:choose>
		<c:when test = "${ empty done}">
			<c:if test="${! empty courseList}">
			  <c:forEach items="${courseList}" var="current">
				<div class="course"> 
					Title: <c:out value="${current.title}"/> <br>
					CRN: <c:out value="${current.CRN}"/> <br>
					<c:out value="${current.subject}"/>  <c:out value="${current.courseNum}"/>  <br>
					<%--   <jsp:useBean id="exam" class="com.example.Exam" scope="page"/> 
					 <jsp:getProperty name="Course" property="time_ToS"/>--%>
					<c:out value="${current.timeStr}"/> <br>
					
					<form method="POST" action="${pageContext.servletContext.contextPath}/studentEdit">
					  <input type="hidden" name="added_crn" value="${current.CRN}" />
					  <input type="submit" name="submit" value="Add Class" />
					</form>
					
				</div>
				<br>
			  </c:forEach>
			</c:if>
		</c:when>
		<c:otherwise>
			<h2><c:out value="${ AddMessage}"/></h2></c:otherwise>
		</c:choose>
	</body>
