<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	<form:form action="chirp/user/create.do" modelAttribute="chirp">
	<form:hidden path="id"/>
	<form:hidden path="version"/>


	<acme:textbox code="master.page.title" path="title"/>
	<acme:textarea code="master.page.description" path="description"/>
	<p/>
	
	
	<acme:submit code="master.page.save"  name="save" />

	<acme:cancel code="master.page.return" url="/chirp/user/list.do" />
</form:form>