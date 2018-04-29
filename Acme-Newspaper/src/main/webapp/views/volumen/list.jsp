<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="volumens" id="row" requestURI="${requestURI}" pagesize="5">
	
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
		
		<acme:cancel url="volumen/user/edit.do" code="master.page.create"/>
	</jstl:if>


