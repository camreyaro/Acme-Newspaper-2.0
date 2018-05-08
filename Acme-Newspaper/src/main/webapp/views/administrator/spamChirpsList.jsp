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

<display:table pagesize="5" class="displaytag"
	name="chirps" requestURI="chirp/administrator/spamChirpsList.do" id="row">

	<spring:message code="master.page.chirp" var="titleH" />
	<display:column property="title" title="${titleH}" />
	<spring:message code="master.page.view" var="titleL" />
	<display:column title="${titleL}">
	<a href="chirp/administrator/delete.do?chirpId=${row.id}"><spring:message code="master.page.delete"/></a>
	</display:column>
	
</display:table>
<p>
<spring:message code="admin.viewAll"/> <spring:message code="master.page.chirp"/>s? <a href="chirp/administrator/spamChirpsList.do?viewAll=1"><spring:message code="master.page.view"/></a>
</p>