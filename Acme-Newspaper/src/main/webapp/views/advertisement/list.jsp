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

<spring:message code="newspaper.haveAdv"/>
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="newspapersWithAdv" requestURI="${requestURI}" id="row">
	
	<spring:message code="master.page.actions" var="actionsH" />
	<display:column title="${actionsH}">
	<acme:actionurl url="newspaper/display.do?newspaperId=${row.id}" code="master.page.view"/>&nbsp;|&nbsp;
	<acme:actionurl url="newspaper/advertisement/agent/create.do?newspaperId=${row.id}" code="advertisement.place"/>
	</display:column>
	
	<spring:message code="master.page.picture" var="pictureUrlH" />
	<display:column title="${pictureUrlH}">
	<img src="${row.pictureUrl}" height="30px" width="auto" alt="${row.title}"/>
	</display:column>

	<spring:message code="master.page.title" var="titleH" />
	<display:column title="${titleH}">
	<jstl:out value="${row.title}"/> <jstl:if test="${not row.publicNp}"><b>[<spring:message code="master.page.private"/>]</b></jstl:if>
	</display:column>
	
	<spring:message code="master.page.publicationDate" var="publicationdateH" />
	<display:column property="publicationDate" title="${publicationdateH}" />
	
	<spring:message code="master.page.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<spring:message code="master.page.user" var="publisherH" />
	<display:column title="${publisherH}">
	<a href="user/display.do?userId=${row.publisher.id}">${row.publisher.userAccount.username}</a>
	</display:column>
	
</display:table>

<spring:message code="newspaper.notHaveAdv"/>
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="newspapersWithNoAdv" requestURI="${requestURI}" id="rou">
	
	<spring:message code="master.page.actions" var="actionsH" />
	<display:column title="${actionsH}">
	<acme:actionurl url="newspaper/display.do?newspaperId=${rou.id}" code="master.page.view"/>&nbsp;|&nbsp;
	<acme:actionurl url="newspaper/advertisement/agent/create.do?newspaperId=${rou.id}" code="advertisement.place"/>
	</display:column>
	
	<spring:message code="master.page.picture" var="pictureUrlH" />
	<display:column title="${pictureUrlH}">
	<img src="${rou.pictureUrl}" height="30px" width="auto" alt="${rou.title}"/>
	</display:column>

	<spring:message code="master.page.title" var="titleH" />
	<display:column title="${titleH}">
	<jstl:out value="${rou.title}"/> <jstl:if test="${not rou.publicNp}"><b>[<spring:message code="master.page.private"/>]</b></jstl:if>
	</display:column>
	
	<spring:message code="master.page.publicationDate" var="publicationdateH" />
	<display:column property="publicationDate" title="${publicationdateH}" />
	
	<spring:message code="master.page.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<spring:message code="master.page.user" var="publisherH" />
	<display:column title="${publisherH}">
	<a href="user/display.do?userId=${rou.publisher.id}">${rou.publisher.userAccount.username}</a>
	</display:column>
	
</display:table>
