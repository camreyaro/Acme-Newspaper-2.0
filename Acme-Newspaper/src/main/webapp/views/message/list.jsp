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

<jstl:out value="${folder.name }"/>

<fieldset>
<legend>
		<spring:message code="folder.childrens"></spring:message>
</legend>
<display:table name="childrens" id="row" requestURI="${requestURI }" pagesize="5" class="displaytag">
	
	<spring:message code="message.action" var="actionHeader" />
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
</fieldset>


<fieldset>
<legend>
		<spring:message code="message"></spring:message>
</legend>

<display:table name="messages" id="row" requestURI="${requestURI }" pagesize="5" class="displaytag">

<spring:message code="message.action" var="actionHeader" />
	<display:column title="${actionHeader}" sortable = "false" >
		<a href="message/display.do?messageId=${row.id }">
			<spring:message code="message.display"></spring:message>
		</a>
		<a href="message/edit.do?messageId=${row.id }">
			<spring:message code="message.edit"></spring:message>
		</a>
	</display:column>
	
	
	<spring:message code="message.subject" var="subjectHeader" />
	<display:column property="subject" title="${subjectHeader}" sortable = "false" />
	
		<spring:message code="message.sender" var="senderHeader" />
	<display:column property="sender.userAccount.username" title="${senderHeader}" sortable = "false" />
	
		<spring:message code="message.recipient" var="recipientHeader" />
	<display:column property="recipient.userAccount.username" title="${recipientHeader}" sortable = "false" />
	
		<spring:message code="message.priority" var="priorityHeader" />
	<display:column property="priority" title="${priorityHeader}" sortable = "true" />
	<br/>

</display:table>
</fieldset>

	<input type="button" name="send" value="<spring:message code="message.send" />" 
			onclick="javascript: location.replace('message/create.do?all=0')"/>

<input type="button" name="back" value="<spring:message code="folder.create" />" 
	onclick="javascript: location.replace('folder/create.do?folderId=${folder.id}')"/>
<security:authorize	access="hasRole('ADMIN')">
	<input type="button" name="send" value="<spring:message code="message.send.all" />" 
			onclick="javascript: location.replace('message/create.do?all=1')"/>
</security:authorize>

	<input type="button" name="back" value="<spring:message code="message.back" />" 
			onclick="javascript: location.replace('folder/list.do')"/>
