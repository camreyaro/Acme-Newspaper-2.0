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
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<div style="padding: 30px 30px 30px 30px">
<jstl:if test="${ fn:contains(newspaper.pictureUrl, 'http')}">
<img src="${newspaper.pictureUrl}" width="150px" height="auto" alt="${newspaper.title}" />
<hr>
</jstl:if>

<h1><jstl:out value="${newspaper.title}"/></h1>
<h2><jstl:out value="${newspaper.description}"/></h2>
<h3>
<span><a href="user/display.do?userId=${newspaper.publisher.id}"><jstl:out value="${newspaper.publisher.name}"/> <jstl:out value="${newspaper.publisher.surname}"/></a></span>
</h3>
<h4>
<span><jstl:out value="${newspaper.publicationDate}"/></span></h4>

<security:authorize access="hasRole('CUSTOMER')">
<jstl:if test="${ not newspaper.publicNp and not owner and not suscribe}">
	<!-- SUSCRIPTION -->
	<acme:action code="master.page.suscribe"  url="suscription/suscribe.do?newspaperId=${newspaper.id}"/> <jstl:out value="${newspaper.price}"/> &euro;
</jstl:if>
</security:authorize>
	<p/>

<!-- ARTICLES -->
	
	<display:table pagesize="5" class="displaytag"  keepStatus="true"
	name="articles" requestURI="${requestURI}" id="row">
	

		<spring:message code="master.page.title" var="titleH" />
		<display:column title="${titleH}">
			<jstl:if test="${ (not newspaper.publicNp and (owner or suscribe)) or newspaper.publicNp}">
			<a href="newspaper/article/display.do?articleId=${row.id}"> </jstl:if> <jstl:out value="${row.title}"/> </a> 
			
			<jstl:if test="${ owner && (!row.saved)}">
				<acme:action url="newspaper/article/user/edit.do?articleId=${row.id}" code="master.page.edit"/>
			</jstl:if>
		</display:column>
	
	
		<spring:message code="article.summary" var="summaryH" />
		<display:column title="${summaryH}">
			 <jstl:out value="${row.getReductedSummary()}"/> 
		</display:column>
		
		<spring:message code="article.creator" var="creatorH" />
		<display:column title="${creatorH}">
			<jstl:if test="${ (not newspaper.publicNp and (owner or suscribe)) or newspaper.publicNp}">
				<a href="user//display.do?userId=${row.creator.id}"> </jstl:if> <jstl:out value="${row.creator.userAccount.username}"/> </a>
			
		</display:column>
		
	</display:table>


	<p/>
	
	<!-- PUBLISH -->
	<jstl:if test="${ owner }">
		<jstl:out value="${fn:length(newspaper.articles)-notSavedArticles}"/>/<jstl:out value="${fn:length(newspaper.articles)}"/> <spring:message code="master.page.saved" /> <spring:message code="master.page.articles" />
		<br/>
		<jstl:if test="${ notSavedArticles eq 0 and !newspaper.published and fn:length(newspaper.articles) > 0}">
			<acme:action code="master.page.publish"  url="newspaper/user/publish.do?newspaperId=${newspaper.id}"/>
		</jstl:if>
		
		 <acme:action code="master.page.edit"  url="newspaper/user/edit.do?newspaperId=${newspaper.id}"/> 
	</jstl:if>
	
	<!-- DELETE -->
	<security:authorize access="hasRole('ADMIN')">
		<acme:action code="master.page.delete"  url="newspaper/administrator/delete.do?newspaperId=${newspaper.id}"/>
	</security:authorize>


</div>