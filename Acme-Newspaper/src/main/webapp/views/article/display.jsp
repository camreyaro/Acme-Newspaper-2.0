<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<ul style="list-style-type: disc">

			
	<li><b><spring:message code="master.page.title"></spring:message></b>
		<jstl:out value="${article.title}" /></li>

	<li><b><spring:message code="article.moment"></spring:message></b>
		<jstl:out value="${article.moment}" /></li>
		
	<li><b><spring:message code="article.summary"></spring:message></b>
		<jstl:out value="${article.summary}" /></li>
		
	
	<li><b><spring:message code="master.page.text"></spring:message></b>
		<jstl:out value="${article.body}" /></li>
	
	<li><b><spring:message code="master.page.name"></spring:message></b>
		<jstl:out value="${article.creator.name}" />
		 <jstl:out value="${article.creator.surname}" /></li>
		<jstl:forEach var="susURLss" items="${susURLs}">
		<img src="${susURLss}" /><br/>
	</jstl:forEach>
		
</ul>

<jstl:if test="${advertisement != null }">
<p>
<b><spring:message code="master.page.advertisement"/>:</b><br/>
<a href="${advertisement.urlTargetPage}" target="_blank"><img src="${advertisement.urlBanner}" width="300px" height="auto" alt="${advertisement.title}"/></a>
</p>
</jstl:if>



<jstl:if test="${seeFU}">
	<acme:action code="article.followups"  url="newspaper/article/followup/list.do?articleId=${article.id}"/>
</jstl:if>

<acme:action code="article.back"  url="newspaper/article/list.do?newspaperId=${article.newspaper.id}"/>

<jstl:if test="${canEdit}">
	<acme:action code="article.edit"  url="newspaper/article/user/edit.do?articleId=${article.id}"/>

</jstl:if>

<jstl:if test="${createFU}">
	<acme:action code="followup.create"  url="newspaper/article/followup/user/create.do?articleId=${article.id}"/>
</jstl:if>


<!-- DELETE -->
<security:authorize access="hasRole('ADMIN')">
	<acme:action code="master.page.delete"  url="newspaper/article/administrator/delete.do?articleId=${article.id}"/>
</security:authorize>
