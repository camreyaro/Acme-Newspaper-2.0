<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="newspaper/article/followup/user/create.do" modelAttribute="followUp">
	
	
	<form:hidden path="version" />
	<form:hidden path="id" />
	<form:hidden path="moment" />
	<form:hidden path="article" />
	
	<acme:textbox code="master.page.title" path="title" />
	<br>
	<acme:textbox code="master.page.description" path="text" />
	<br>
	<acme:textbox code="followUp.pictureURLs" path="pictureUrls" />
	<br>		

	<acme:submit code="master.page.save"  name="save"/>
	<acme:cancel code="master.page.return" url="/newspaper/article/display.do?articleId=${articleId}" />
	
</form:form>
