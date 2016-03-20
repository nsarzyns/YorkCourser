<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- Finish this, get db started, make edit call itself a bunch -->
<html>
	<head>
		<title>Add Courses</title>
		<link rel="stylesheet" type="text/css" href="webCSS.css">
		<link href='https://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
	</head>
	
	<body>
	<%@ page import="edu.ycp.cs320.sme.model.Course" %>

			<form action="${pageContext.servletContext.contextPath}/studentEdit" method="post" id="search">
				<table>
					<tr>
						<td class="label">Course name:</td>
						<td><input type="text" name="title" size="12" value="${title}" /></td>
					</tr>
					<tr>
						<td class="label">Subject</td>
						<td>
							<select name="subject" form="search" style="height: 23px; width: 95px; ">
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
		
		
		<c:if test="${! empty courseList}">
		  <c:forEach items="${courseList}" var="current">
			<div class="course"> 
				<c:out value="${current.title}"/> 
			
					<%-- Subject: <c:out value="${current.getSubject_toS()}"/> <c:out value="${current.getCourseNum()}"/> <br>
					CRN: <c:out value="${current.getCRN()}"/> <br> 
					
					--%>
			</div>
		  </c:forEach>
		</c:if>
		
	</body>