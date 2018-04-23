<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="administrator/editSpamWord.do" modelAttribute="spamWord" class="form-signin">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textarea code="master.page.spamWord" path="word" />
	
	
	<acme:submit code="master.page.save"  name="save"/>
	<jstl:if test="${spamWord.id != 0}">
	<acme:submit code="master.page.delete"  name="delete"/>
	</jstl:if>
</form:form>

<acme:cancel code="master.page.cancel" url="administrator/listTabooWords.do"/>