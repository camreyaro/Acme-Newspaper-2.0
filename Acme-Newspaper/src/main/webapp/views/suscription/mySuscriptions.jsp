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
<acme:searchbox action="suscription/mySuscriptions.do"/>

<display:table pagesize="5" class="displaytag"
	name="suscription" requestURI="${requestURI}" id="row">
	
	<spring:message code="master.page.actions" var="actionsH" />
	<display:column title="${actionsH}">
	<acme:actionurl url="newspaper/display.do?newspaperId=${row.newspaper.id}" code="master.page.view"/>
	</display:column>
	
	<spring:message code="master.page.picture" var="pictureUrlH" />
	<display:column title="${pictureUrlH}">
	<img src="${row.newspaper.pictureUrl}" height="30px" width="auto" alt="${row.newspaper.title}"/>
	</display:column>

	<spring:message code="master.page.title" var="titleH" />
	<display:column title="${titleH}">
	<jstl:out value="${row.newspaper.title}"/>
	</display:column>
	
	<spring:message code="master.page.publicationDate" var="publicationdateH" />
	<display:column property="newspaper.publicationDate" title="${publicationdateH}" />
	
	<spring:message code="master.page.description" var="descriptionH" />
	<display:column property="newspaper.description" title="${descriptionH}" />
	
	<spring:message code="master.page.user" var="publisherH" />
	<display:column property="newspaper.publisher.userAccount.username" title="${publisherH}" />
	
</display:table>

