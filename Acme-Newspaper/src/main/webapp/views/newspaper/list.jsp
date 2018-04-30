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

<spring:message code="pagination.showing1" />
<jstl:out value="${pageSize}" />
<spring:message code="pagination.showing2" />
.
<spring:message code="pagination.currentPage" />
:
<jstl:out value="${pageNumber}" />
.
<br/>
<jstl:forEach var="number" begin ="1" end="${totalPages}">
<jstl:if test="${number eq pageNumber}">
<jstl:out value="${number}"/>
</jstl:if>
<jstl:if test="${number ne pageNumber}">
<a class="links" href="newspaper/list.do?pageNumber=<jstl:out value="${number}"/>&keyword=<jstl:out value="${keyword}"/>"><jstl:out value="${number}"/></a>
</jstl:if>
&nbsp;&nbsp;&nbsp;&nbsp;
</jstl:forEach>

<display:table class="displaytag" keepStatus="true" pagesize="${pageSize}"
	name="newspapers" id="row">
	
	<spring:message code="master.page.actions" var="actionsH" />
	<display:column title="${actionsH}">
		<jstl:if test="${row.published}"> 
			<acme:actionurl url="newspaper/display.do?newspaperId=${row.id}" code="master.page.view"/>
		</jstl:if>
		
		<jstl:if test="${!row.published}"> 
			<acme:action code="newspaper.createArticle"  url="newspaper/article/user/create.do?newspaperId=${row.id}"/>
		</jstl:if>
		
		<jstl:if test="${row.publisher.id == actor.id}">
			<acme:action url="newspaper/display.do?newspaperId=${row.id}" code="master.page.view"/>
		</jstl:if>
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
	
	<security:authorize access="hasRole('USER')">
		<display:column title="Volumen">
			<a href="volumen/user/add.do?newspaperId=${row.id}"><spring:message code="volumen.add"></spring:message></a>
		</display:column>
		<jstl:if test="${creator}">
		
			<display:column title="${volumen.title}">
			<a href="volumen/user/remove.do?newspaperId=${row.id}&volumenId=${volumen.id}" style="color:red;"><spring:message code="volumen.remove"></spring:message></a>
			</display:column>
		
		</jstl:if>
		
		
	</security:authorize>
	
</display:table>
<security:authorize access="hasRole('USER')">
	<jstl:if test="${requestURI!='volumen/newspaper/list.do' }">
		<acme:action code="master.page.create"  url="newspaper/user/create.do"/>
	</jstl:if>
</security:authorize>
