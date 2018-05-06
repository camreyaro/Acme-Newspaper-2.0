<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<acme:searchbox action="newspaper/article/searchedList.do"/>

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
<jstl:out value="${number}"/></jstl:if>
<jstl:if test="${number ne pageNumber}">
<a class="links" href="newspaper/article/searchedList.do?pageNumber=<jstl:out value="${number}"/>&keyword=<jstl:out value="${keyword}"/>"><jstl:out value="${number}"/></a>
</jstl:if>
&nbsp;&nbsp;&nbsp;&nbsp;
</jstl:forEach>

<display:table pagesize="${pageSize}" class="displaytag"
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
	
	<spring:message code="article.display" var="display" />
	<display:column title="${display}">
		<acme:action code="article.display"  url="newspaper/article/display.do?articleId=${row.id}"/>
	</display:column>

</display:table>
