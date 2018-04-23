<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table pagesize="5" class="displaytag"  keepStatus="true"
	name="articles" requestURI="${requestURI}" id="row">

	<spring:message code="master.page.title" var="titleH" />
	<display:column title="${titleH}">
		<jstl:out value="${row.title}"/> 
	</display:column>
	
	<spring:message code="master.page.publicationDate" var="publicationdateH" />
	<display:column property="moment" title="${publicationdateH}" />
	
	<spring:message code="article.summary" var="summaryH" />
	<display:column title="${summaryH}">
		<jstl:out value="${row.summary}"/> 
	</display:column>
	<spring:message code="article.creator" var="creatorH" />
	<display:column title="${creatorH}">
		<jstl:out value="${row.creator.userAccount.username}"/> 
	</display:column>
	
	<spring:message code="master.page.actions" var="display" />
	<display:column title="${display}">
	
		<jstl:if test="${!row.saved}">
			<acme:action code="article.edit"  url="newspaper/article/user/edit.do?articleId=${row.id}"/>
		</jstl:if>
	</display:column>

</display:table>
