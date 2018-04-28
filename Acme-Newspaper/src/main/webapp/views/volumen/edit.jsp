<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="volumen/user/edit.do" modelAttribute="volumen">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox code="volumen.title" path="title"/>
	<acme:textbox code="volumen.description" path="description"/>
	<acme:textbox code="volumen.price" path="price"/>
	
	<acme:submit name="save" code="master.page.save"/>
	<acme:cancel url="volumen/user/myList.do" code="master.page.cancel"/>

</form:form>


