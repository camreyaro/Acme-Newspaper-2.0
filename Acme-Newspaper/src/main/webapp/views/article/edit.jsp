<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="newspaper/article/user/edit.do" modelAttribute="article">
	
	
	<form:hidden path="version" />
	<form:hidden path="id" />

	
	
	<acme:textbox code="master.page.title" path="title" />
	<br>
	<acme:textbox code="article.summary" path="summary" />
	<br>
	<acme:textbox code="master.page.description" path="body" />
	<br>
	<acme:textbox code="article.pictureURLs" path="pictureURLs" />
	<i><spring:message code="article.help.urls" /></i>
	<br><br>
	<acme:textbox code="article.saved" path="saved" />
	<i><spring:message code="article.help.saved" /></i>
	<br>		

	<acme:submit code="master.page.save"  name="save" />
	<acme:cancel code="master.page.return" url="/newspaper/list.do" />
	
</form:form>
