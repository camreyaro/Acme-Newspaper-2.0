<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="user/edit.do" modelAttribute="userForm" class="form-signin">

	<form:hidden path="username" />
	
	<acme:textbox code="user.name" path="name"/>
	<acme:textbox  code="user.surname" path="surname"/>
	<acme:textbox  code="user.postalAddress" path="postalAddress" />
	<acme:textbox  code="user.phoneNumber" path="phoneNumber"/>
	<acme:textbox  code="user.email" path="emailAddress"/>
	
	
	<acme:submit code="master.page.save"  name="save"/>
	
</form:form>