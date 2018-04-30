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
<a class="links" href="${requestURI}?pageNumber=<jstl:out value="${number}"/>"><jstl:out value="${number}"/></a>
</jstl:if>
&nbsp;&nbsp;&nbsp;&nbsp;
</jstl:forEach>

<display:table name="volumens" id="row" requestURI="${requestURI}" pagesize="${pageSize}">
	
	<jstl:if test="${requestURI!='volumen/user/myList.do' }">
		<spring:message code="volumen.creator" var="creatorHeader"/>
		<display:column title="${creatorHeader }" >
			<a href="user/display.do?userId=${row.user.id}">
				<jstl:out value="${row.user.name }"></jstl:out>
				<jstl:out value="${row.user.surname }"></jstl:out>
			</a>
		</display:column>
	</jstl:if>
	
	<spring:message code="volumen.title" var="titleHeader"/>
	<display:column property="title" title="${titleHeader }" />
	
	<spring:message code="volumen.description" var="descriptionHeader"/>
	<display:column property="description" title="${descriptionHeader }" />
	
	<jstl:if test="${requestURI!= 'suscriptionVolumen/myList.do'}" >
		<spring:message code="volumen.price" var="priceHeader"/>
		<display:column property="price" title="${priceHeader }" />
	</jstl:if>
	
	<spring:message code="master.page.newspapers" var="actionHeader"/>
	<display:column title="${actionHeader }" >
		<a href="volumen/newspaper/list.do?volumenId=${row.id}"><spring:message code="master.page.newspapers"></spring:message></a>
	</display:column>
	

	
	<security:authorize access="hasRole('CUSTOMER')">
			<jstl:if test="${requestURI!= 'suscriptionVolumen/myList.do'}" >
		<spring:message code="volumen.suscribe" var="suscribeHeader"/>
		<display:column title="${suscribeHeader }" >
			<a href="suscriptionVolumen/create.do?volumenId=${row.id}"><spring:message code="volumen.suscribe"></spring:message></a>
		</display:column>
	</jstl:if>
	
	</security:authorize>
	

</display:table>

<jstl:if test="${requestURI== 'volumen/user/myList.do'}" >
		<spring:message code="volumen.find"></spring:message> <a href="newspaper/list.do"><spring:message code="volumen.here"></spring:message></a><br>
		<acme:cancel url="volumen/user/edit.do" code="master.page.create"/>
		
	</jstl:if>


