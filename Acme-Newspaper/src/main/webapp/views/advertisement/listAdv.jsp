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
	name="advertisements" requestURI="${requestURI}" id="row">
	
	<spring:message code="master.page.actions" var="actionsH" />
	<display:column title="${actionsH}">
	<acme:actionurl url="advertisement/agent/edit.do?advertisementId=${row.id}" code="master.page.edit"/>
	</display:column>
	
	<spring:message code="master.page.picture" var="pictureUrlH" />
	<display:column title="${pictureUrlH}">
	<a href="${row.urlTargetPage}" target="_blank"><img src="${row.urlBanner}" height="30px" width="auto" alt="${row.title}"/></a>
	</display:column>

	<spring:message code="master.page.title" var="titleH" />
	<display:column property="title" title="${titleH}"/>
	
	<spring:message code="master.page.newspaper" var="newspaperH" />
	<display:column title="${newspaperH}">
		<a href="newspaper/display.do?newspaperId=${row.newspaper.id}"><jstl:out value="${row.newspaper.title}"/></a>
	</display:column>
	
	<spring:message code="master.page.price" var="priceH" />
	<display:column property="price" title="${priceH}" />
	
	<spring:message code="master.page.creditcard" var="creditCard" />
	<display:column property="creditCard.number" title="${creditCard}" />
	
</display:table>

<security:authorize access="hasRole('AGENT')">
<acme:action code="master.page.create"  url="newspaper/advertisement/agent/list.do"/>
</security:authorize>
