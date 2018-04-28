<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<ul style="list-style-type: disc">

			
	<li><b><spring:message code="agent.name"></spring:message></b>
	<jstl:out value="${agent.name}" /> <jstl:out value="${agent.surname}" /></li>
	
	<li><b><spring:message code="agent.username"></spring:message></b>
	<jstl:out value="${agent.userAccount.username}" /></li>
	
	<li><b><spring:message code="agent.postalAddress"></spring:message></b>
		<jstl:out value="${agent.postalAddress}" /></li>

	<li><b><spring:message code="agent.phoneNumber"></spring:message></b>
		<jstl:out value="${agent.phoneNumber}" /></li>

	<li><b><spring:message code="agent.email"></spring:message></b> <jstl:out
			value="${agent.emailAddress}" /></li>

</ul>