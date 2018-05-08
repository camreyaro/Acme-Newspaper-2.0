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
	name="spamAds" requestURI="advertisement/administrator/spamAdvertisementsList.do" id="row">

	<spring:message code="master.page.advertisement" var="titleH" />
	<display:column title="${titleH}">
	<a href="${row.urlTargetPage}">
	<img src="${row.urlBanner}" height="30px" width="auto" alt="${row.title}"/></a>
	<jstl:out value="${row.title}"/> 
	</display:column>
	<spring:message code="master.page.actions" var="actionsH" />
	<display:column title="${actionsH}">
	<a href="advertisement/administrator/delete.do?advertisementId=${row.id}"><spring:message code="master.page.delete"/></a><br/>
	</display:column>
	
</display:table>
<p>
<spring:message code="admin.viewAll"/> <spring:message code="master.page.advertisement"/>s? <a href="advertisement/administrator/spamAdvertisementsList.do?viewAll=1"><spring:message code="master.page.view"/></a>
</p>
