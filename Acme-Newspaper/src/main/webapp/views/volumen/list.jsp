<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="volumens" id="row" requestURI="${requestURI}">
	
	<spring:message code="volumen.creator" var="creatorHeader"/>
	<display:column title="${creatorHeader }" >
		<a href="user/display.do?userId=${row.customer.id}">
			<jstl:out value="${row.customer.name }"></jstl:out>
			<jstl:out value="${row.customer.surname }"></jstl:out>
		</a>
	</display:column>
	
	<spring:message code="volumen.title" var="titleHeader"/>
	<display:column property="title" title="${titleHeader }" />
	
	<spring:message code="volumen.description" var="descriptionHeader"/>
	<display:column property="description" title="${descriptionHeader }" />
	
	<jstl:if test="${requestURI!= 'suscriptionVolumen/myList.do'}" >
		<spring:message code="volumen.price" var="pricHeader"/>
		<display:column property="price" title="${priceHeader }" />
	</jstl:if>
	
	<spring:message code="master.page.newspapers" var="actionHeader"/>
	<display:column title="${actionHeader }" >
		<a href="volumen/newspaper/list.do?volumenId=${row.id}"><spring:message code="master.page.newspapers"></spring:message></a>
	</display:column>
	
	
	<jstl:if test="${requestURI== 'volumen/myList.do'}" >
		<spring:message code="master.page.edit" var="editHeader"/>
		<display:column title="${actionHeader }" >
			<a href="volumen/edit.do?volumenId=${row.id}"><spring:message code="master.page.newspapers"></spring:message></a>
		</display:column>
	</jstl:if>
	

</display:table>
