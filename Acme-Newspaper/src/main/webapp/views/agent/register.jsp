<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="agent/register.do" modelAttribute="agent" class="form-signin">
	
	<acme:textbox code="agent.username" path="userAccount.username" />
	<acme:password code="agent.password" path="userAccount.password" />
	<acme:textbox code="agent.name" path="name" />
	<acme:textbox  code="agent.surname" path="surname" />
	<acme:textbox  code="agent.postalAddress" path="postalAddress" />
	<acme:textbox  code="agent.phoneNumber" path="phoneNumber"/>
	<acme:textbox  code="agent.email" path="emailAddress"/><br/>
	
	<form:checkbox path="hasConfirmedTerms" value="true" />
	<spring:message code="user.termsAndConditions" />
	<form:errors cssClass="error" path="hasConfirmedTerms" /><br/><br/>
	
	<acme:submit code="user.register"  name="register" />
</form:form>