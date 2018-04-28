<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="agent/edit.do" modelAttribute="agentForm" class="form-signin">

	<form:hidden path="userName" />
	
	<acme:textbox code="agent.name" path="name"/>
	<acme:textbox  code="agent.surname" path="surname"/>
	<acme:textbox  code="agent.postalAddress" path="postalAddress" />
	<acme:textbox  code="agent.phoneNumber" path="phoneNumber"/>
	<acme:textbox  code="agent.email" path="emailAddress"/>
	
	
	<acme:submit code="master.page.save"  name="save"/>
</form:form>