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

<security:authorize access="hasRole('USER')">
<acme:action code="master.page.create"  url="chirp/user/create.do"/>
</security:authorize>

<br/>

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
<a class="links" href="chirp/user/list.do?pageNumber=<jstl:out value="${number}"/>"><jstl:out value="${number}"/></a>
</jstl:if>
&nbsp;&nbsp;&nbsp;&nbsp;
</jstl:forEach>

<table id ="myTable" data-page-length='5'>
 <thead>
        <tr>
            <th><spring:message code="master.page.user"/></th>
            <th><spring:message code="master.page.moment"/></th>
            <th><spring:message code="master.page.title"/></th>
            <th><spring:message code="master.page.description"/></th>
            <security:authorize access="hasRole('ADMIN')">
            <th><spring:message code="master.page.delete"/></th>
            </security:authorize>
        </tr>
        </thead>
         <tbody>
         <jstl:forEach items="${chirps}" var="chirp">
        <tr>
            <td><a href="user/display.do?userId=${chirp.user.id }"><b><jstl:out value="${chirp.user.name } ${chirp.user.surname }"/></b></a> <i><jstl:out value="@${chirp.user.userAccount.username }"/></i></td>
            <td>${chirp.moment}</td>
            <td>${chirp.title}</td>
            <td>${chirp.description}</td>
            <security:authorize access="hasRole('ADMIN')">
            <acme:action code="master.page.delete"  url="chirp/administrator/delete.do?chirpId=${chirp.id}"/>
            </security:authorize>
        </tr>
        </jstl:forEach>
   		 </tbody>
</table>

<%-- <display:table pagesize="5" class="displaytag" keepStatus="true"
	name="chirps" requestURI="${requestURI}" id="chirp">
	<spring:message code="master.page.user" var="publisherH" />
	<display:column title="${publisherH}" >
	<a href="user/display.do?userId=${chirp.user.id }"><b><jstl:out value="${chirp.user.name } ${chirp.user.surname }"/></b></a> <i><jstl:out value="@${chirp.user.userAccount.username }"/></i>
	</display:column>
	
	<spring:message code="master.page.moment" var="momentH" />
	<display:column property="moment" title="${momentH}" sortable="true" />
	
	<spring:message code="master.page.title" var="titleH" />
	<display:column property="title" title="${titleH}"/>
	
	<spring:message code="master.page.description" var="descriptionH" />
	<display:column property="description" title="${descriptionH}" />
	
	<security:authorize access="hasRole('ADMIN')">
	<display:column>
		<acme:action code="master.page.delete"  url="chirp/administrator/delete.do?chirpId=${chirp.id}"/>
	</display:column>
	</security:authorize>

</display:table> --%>

