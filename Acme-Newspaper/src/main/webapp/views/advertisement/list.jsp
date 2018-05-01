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
<table id ="myTablePaginate2" data-page-length='5'>
 <thead>
        <tr>
            <th><spring:message code="master.page.actions"/></th>
            <th><spring:message code="master.page.picture"/></th>
            <th><spring:message code="master.page.title"/></th>
            <th><spring:message code="master.page.publicationDate"/></th>
            <th><spring:message code="master.page.description"/></th>
            <th><spring:message code="master.page.user"/></th>
        </tr>
        </thead>
         <tbody>
         <jstl:forEach items="${newspapersWithAdv}" var="rou">
        <tr>
            <td><acme:actionurl url="newspaper/display.do?newspaperId=${rou.id}" code="master.page.view"/>&nbsp;|&nbsp;
			<acme:actionurl url="newspaper/advertisement/agent/create.do?newspaperId=${rou.id}" code="advertisement.place"/>
			</td>
            <td><img src="${rou.pictureUrl}" height="30px" width="auto" alt="${rou.title}"/></td>
            <td>${rou.title}<jstl:if test="${not rou.publicNp}"><b>[<spring:message code="master.page.private"/>]</b></jstl:if></td>
            <td>${rou.publicationDate}</td>
            <td>${rou.description}</td>
            <td><a href="user/display.do?userId=${rou.publisher.id}">${rou.publisher.userAccount.username}</a></td>
        </tr>
        </jstl:forEach>
   		 </tbody>
</table>
<%-- <display:table pagesize="5" class="displaytag" keepStatus="true"
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
	
</display:table> --%>

<spring:message code="newspaper.notHaveAdv"/>
<table id ="myTablePaginate" data-page-length='4'>
 <thead>
        <tr>
            <th><spring:message code="master.page.actions"/></th>
            <th><spring:message code="master.page.picture"/></th>
            <th><spring:message code="master.page.title"/></th>
            <th><spring:message code="master.page.publicationDate"/></th>
            <th><spring:message code="master.page.description"/></th>
            <th><spring:message code="master.page.user"/></th>
        </tr>
        </thead>
         <tbody>
         <jstl:forEach items="${newspapersWithNoAdv}" var="rou">
        <tr>
            <td><acme:actionurl url="newspaper/display.do?newspaperId=${rou.id}" code="master.page.view"/>&nbsp;|&nbsp;
			<acme:actionurl url="newspaper/advertisement/agent/create.do?newspaperId=${rou.id}" code="advertisement.place"/>
			</td>
            <td><img src="${rou.pictureUrl}" height="30px" width="auto" alt="${rou.title}"/></td>
            <td>${rou.title}<jstl:if test="${not rou.publicNp}"><b>[<spring:message code="master.page.private"/>]</b></jstl:if></td>
            <td>${rou.publicationDate}</td>
            <td>${rou.description}</td>
            <td><a href="user/display.do?userId=${rou.publisher.id}">${rou.publisher.userAccount.username}</a></td>
        </tr>
        </jstl:forEach>
   		 </tbody>
</table>
<%-- <display:table pagesize="4" class="displaytag" keepStatus="true"
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
	
</display:table> --%>
