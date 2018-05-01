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


	<fieldset>	
	<legend>Message</legend>
	
	<fieldset>	
	<legend><spring:message code="message.date"/></legend>
	<jstl:out value = "${messages.date }"/>
	</fieldset>
	<br/>

	

	
	<fieldset>
	<legend><spring:message code="message.sender"/></legend>
	<jstl:out value = "${sender.userAccount.username }"/>
	</fieldset>
	<br/>
	
	<fieldset>
	<legend><spring:message code="message.recipient"/></legend>
	<jstl:out value = "${recipient.userAccount.username }"/>
	</fieldset>
	<br/>
	<fieldset>
	<legend><spring:message code="message.subject"/></legend>
	<jstl:out value = "${messages.subject }"/>
	</fieldset>
	<br/>
	
	<fieldset>
	<legend><spring:message code="message.body"/></legend>
	<br/>
	<jstl:out value = "${messages.body }"/>
	</fieldset>
	<br/>
	
	<input type="button" name="back" value="<spring:message code="message.back" />" 
				onclick="javascript: location.replace('folder/list.do')"/>
	
	
	</fieldset>
	
