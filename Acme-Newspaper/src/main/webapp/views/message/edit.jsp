
<%--
 * action-2.jsp
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


<form:form action="message/edit.do"
	modelAttribute="message">
	<fieldset>
		<jstl:if test="${message.id != 0 }">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<form:hidden path="date"/>
		<form:hidden path="subject"/>
		<form:hidden path="body"/>
		<form:hidden path="priority"/>
		<form:hidden path="spam"/>
		<form:hidden path="sender"/>
		<form:hidden path="recipient"/>
		
		
		
		
		<legend>Message</legend>
		
		<form:label path="folder">
			<spring:message code="message.parent"/>
		</form:label>
		<form:select id='folder' path="folder">
			<form:option value="0" label="----"/>
			<form:options items="${folders }"
						itemValue="id"
						itemLabel="name"/>
		</form:select>
		<br/>

		<input type="submit" name="save" value="<spring:message code="folder.create.save" />" />
	
		</jstl:if>
		
		
		
		
		
		<jstl:if test="${message.id == 0 }">
		<jstl:if test="${all == 0 }">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="date"/>
		<form:hidden path="spam"/>
		<form:hidden path="folder"/>
		<form:hidden path="sender"/>

		
		<legend>Message</legend>
		
		
		<form:label path="recipient">
			<spring:message code="message.recipient"/>
		</form:label>
		<form:select id='recipient' path="recipient">
			<form:option value="0" label="----"/>
			<form:options items="${recipients }"
						itemValue="id"
						itemLabel="name"/>
		</form:select>
		<br/>
		
		<br/>
		
		<form:label path="subject">
			<spring:message code="message.subject"/>
		</form:label>
		<form:input path="subject" placeholder= "subject"/>
		<form:errors cssClass="error" path="subject"/>
		<br/>
		<br/>

		<form:label path="body">
			<spring:message code="message.body"/>
		</form:label>
		<form:textarea path="body" placeholder= "Body"/>
		<form:errors cssClass="error" path="body"/>
		<br/>
		
		<form:label path="priority">
			<spring:message code="message.priority"/>
		</form:label>
		<select name="priority">
		    <option value="LOW">LOW</option>
		    <option value="NEUTRAL">NEUTRAL</option>
		    <option value="HIGH">HIGH</option>
  		</select>
		<br/>
		<br/>
		
		<input type="submit" name="save" value="<spring:message code="folder.create.save" />" />
		
		
		</jstl:if>
		
		
		<jstl:if test="${all == 1 }">
		
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="date"/>
		<form:hidden path="priority"/>
		<form:hidden path="spam"/>
		<form:hidden path="folder"/>
		<form:hidden path="sender"/>
		

		
		<legend>Message</legend>
		
		<form:label path="subject">
			<spring:message code="message.subject"/>
		</form:label>
		<form:input path="subject" placeholder="Subject...."/>
		<form:errors cssClass="error" path="subject"/>
		<br/>
		
		<form:label path="body">
			<spring:message code="message.body"/>
		</form:label>
		<form:textarea path="body" placeholder ="Body..."/>
		<form:errors cssClass="error" path="body"/>
		<br/>

		<form:label path="priority">
			<spring:message code="message.priority"/>
		</form:label>
		<select name="priority">
		    <option value="LOW">LOW</option>
		    <option value="NEUTRAL">NEUTRAL</option>
		    <option value="HIGH">HIGH</option>
  		</select>
  		<br/>
		
	<input type="submit" name="saveAll" value="<spring:message code="folder.create.save" />" />
		
		
		</jstl:if>
		</jstl:if>

	<jstl:if test="${message.id != 0}">
	
	<input type="submit" name="delete" value="<spring:message code="folder.delete" />" />
	
	</jstl:if>
	
	<input type="button" name="cancel" value="<spring:message code="folder.create.cancel" />" 
				onclick="javascript: location.replace('folder/list.do')"/>
	
	
	</fieldset>
	
	
	
	</form:form>











