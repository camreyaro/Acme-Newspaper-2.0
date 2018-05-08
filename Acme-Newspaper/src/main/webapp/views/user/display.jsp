<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<ul style="list-style-type: disc">

			
	<li><b><spring:message code="user.name"></spring:message></b>
		<jstl:out value="${user.name}" /></li>

	<li><b><spring:message code="user.surname"></spring:message></b>
		<jstl:out value="${user.surname}" /></li>
			
	<li><b><spring:message code="user.postalAddress"></spring:message></b>
		<jstl:out value="${user.postalAddress}" /></li>

	<li><b><spring:message code="user.phoneNumber"></spring:message></b>
		<jstl:out value="${user.phoneNumber}" /></li>

	<li><b><spring:message code="user.email"></spring:message></b> <jstl:out
			value="${user.emailAddress}" /></li>
	
	<!-- Lista de articulos publicos publicados en periodicos -->		
	<jstl:if test="${not empty articles}">
	
	<h3><spring:message code="article.publicArticles"/></h3>
	
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="articles" requestURI="user/display.do" id="row1">

	<spring:message code="article.title" var="titleHeader" />
	<display:column title="${titleHeader}">
	<a href="newspaper/article/display.do?articleId=${row1.id}"><jstl:out value="${row1.title}" /></a>
	</display:column>
	
	<spring:message code="article.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" />
	
	<spring:message code="article.newspaper" var="newspaperHeader" />
	<display:column property="newspaper.title" title="${newspaperHeader}" />
	
	
	</display:table>
	</jstl:if>
	
	
	<security:authorize access="hasRole('USER')">
	<jstl:if test="${ owner }">
	<acme:action url="user/followers.do" code="master.page.followers"/>
	<acme:action url="user/following.do" code="master.page.following"/>
	</jstl:if>

	<jstl:out value=" ${numFollowers}"/> <spring:message code="master.page.followers"/>
	<jstl:if test="${ not owner }">
	<p />
	<jstl:if test="${ not isFollower }">
	<acme:action url="user/follow.do?userId=${ user.id }" code="master.page.follow"/>
	</jstl:if>
	<jstl:if test="${ isFollower }">
	<acme:action url="user/unfollow.do?userId=${ user.id }" code="master.page.unfollow"/>
	</jstl:if>
	</jstl:if>
	</security:authorize>
		
	<!-- Lista de articulos privados que estoy suscrito -->		
	<jstl:if test="${not empty privateArticlesSuscribed}">
	
	<h3><spring:message code="article.suscribedArticles"/></h3>
	
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="privateArticlesSuscribed" requestURI="user/display.do" id="row2">

	<spring:message code="article.title" var="titleHeader" />
	<display:column title="${titleHeader}">
	<a href="newspaper/article/display.do?articleId=${row2.id}"><jstl:out value="${row2.title}"/></a>
	</display:column>
	
	<spring:message code="article.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" />
	
	<spring:message code="article.newspaper" var="newspaperHeader" />
	<display:column property="newspaper.title" title="${newspaperHeader}" />
	
	
	</display:table>
	</jstl:if>
	
	<!-- Lista de articulos privados que NO estoy suscrito -->		
	<jstl:if test="${not empty privateArticlesNotSuscribed}">
	<h3><spring:message code="article.notSuscribedArticles"/></h3>
	
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="privateArticlesNotSuscribed" requestURI="user/display.do" id="row3">

	<spring:message code="article.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />
	
	<spring:message code="article.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" />
	
	<spring:message code="article.newspaper" var="newspaperHeader" />
	<display:column property="newspaper.title" title="${newspaperHeader}" />
	
	</display:table>
	</jstl:if>
	
	<!-- Lista de articulos pricvados publicados en periodicos -->		
	<jstl:if test="${not empty AllPrivateArticles}">
	
	<h3><spring:message code="article.privateArticles"/></h3>
	
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="AllPrivateArticles" requestURI="user/display.do" id="row4">

	<spring:message code="article.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />
	
	<spring:message code="article.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" />
	
	<spring:message code="article.newspaper" var="newspaperHeader" />
	<display:column property="newspaper.title" title="${newspaperHeader}" />
	
	</display:table>
	</jstl:if>
	
	<!--  Lista de chirps del usuario -->
	<jstl:if test="${not empty chirps}">
	<h3>Chirps:</h3>
	
	<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="chirps" requestURI="user/display.do" id="row5">

	<spring:message code="chirp.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" />
	
	<spring:message code="chirp.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" />
	
	<spring:message code="chirp.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}" />
	
	</display:table>
	</jstl:if>
	
	<acme:action code="master.page.return"  url="user/list.do"/>

</ul>