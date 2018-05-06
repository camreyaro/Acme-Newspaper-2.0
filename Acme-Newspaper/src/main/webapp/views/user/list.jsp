<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

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
<a class="links" href="user/list.do?pageNumber=<jstl:out value="${number}"/>"><jstl:out value="${number}"/></a>
</jstl:if>
&nbsp;&nbsp;&nbsp;&nbsp;
</jstl:forEach>

<!-- Listing grid -->

<display:table pagesize="${pageSize}" class="displaytag" 
	name="users" requestURI="user/list.do" id="row">

	<spring:message code="user.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" />

	<spring:message code="user.surname" var="surnameHeader" />
	<display:column property="surname" title="${surnameHeader}" />

	<spring:message code="user.email" var="emailHeader" />
	<display:column property="emailAddress" title="${emailHeader}" />
	
	<display:column>
	<a href="user/display.do?userId=${row.id}"><spring:message code="user.seeProfile"/></a>
	</display:column>
	

</display:table>