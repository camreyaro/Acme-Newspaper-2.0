<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" 
	name="followUps" requestURI="${requestURI}" id="row">
	
	<spring:message code="master.page.picture" var="pictureUrlH" />
	<display:column title="${pictureUrlH}">
		<img src="${row.pictureUrls}" height="30px" width="auto" alt="${row.title}"/>
	</display:column>

	<spring:message code="master.page.title" var="titleH" />
	<display:column title="${titleH}">
		<jstl:out value="${row.title}"/> 
	</display:column>
	
	<spring:message code="master.page.publicationDate" var="publicationdateH" />
	<display:column property="moment" title="${publicationdateH}" />
	
	<spring:message code="master.page.body" var="bodyH" />
	<display:column title="${bodyH}">
		<jstl:out value="${row.text}"/> 
	</display:column>

</display:table>

<jstl:if test="${createFU}">
	<acme:action code="followup.create"  url="newspaper/article/followup/user/create.do?articleId=${articleId}"/>
</jstl:if>

