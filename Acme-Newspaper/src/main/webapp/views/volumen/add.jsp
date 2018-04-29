<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="volumen/user/add.do" modelAttribute="newsToVolForm">

	<form:hidden path="newspaper"/>
	
	<jstl:if test="${volumens!=null && volumens.size()>0 }">
		<form:select path="volumen">
			<form:options items="${volumens}" itemLabel="title" itemValue="id" />
		</form:select>
		<form:errors path="volumen" cssClass="error"></form:errors>
		<acme:submit name="save" code="master.page.save"/>
	</jstl:if>
	
	<jstl:if test="${volumens==null || volumens.size()==0 }">
		<spring:message code="volumen.noVolumens1"></spring:message> <a href="volumen/user/edit.do"><spring:message code="volumen.noVolumens2"></spring:message></a>?<br>
	</jstl:if>
	
	
	<acme:cancel url="volumen/user/myList.do" code="master.page.cancel"/>

</form:form>

