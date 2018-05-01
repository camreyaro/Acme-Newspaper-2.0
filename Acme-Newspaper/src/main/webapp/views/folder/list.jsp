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

<display:table name="folders" id="row" requestURI="${requestURI }" pagesize="5" class="displaytag">
	
	<spring:message code="folder.action" var="actionHeader" />
	<display:column title="${actionHeader}" sortable = "false" >
		<a href="message/list.do?folderId=${row.id }">
			<spring:message code="folder.messages"></spring:message>
		</a>

		<jstl:if test="${row.predefined == false}">
			<a href="folder/edit.do?folderId=${row.id }">
				<spring:message code="folder.edit"></spring:message>
			</a>
		</jstl:if>
	</display:column>
	
	<spring:message code="folder.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable = "false" />
		

</display:table>

	<input type="button" name="send" value="<spring:message code="message.send" />" 
			onclick="javascript: location.replace('message/create.do?all=0')"/>

	
<security:authorize	access="hasRole('ADMIN')">
	<input type="button" name="send" value="<spring:message code="message.send.all" />" 
			onclick="javascript: location.replace('message/create.do?all=1')"/>
</security:authorize>

	<input type="button" name="back" value="<spring:message code="folder.create" />" 
			onclick="javascript: location.replace('folder/create.do?folderId=0')"/>
