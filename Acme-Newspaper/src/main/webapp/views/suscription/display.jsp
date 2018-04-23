<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<ul style="list-style-type: disc">

			
	<li><b><spring:message code="customer.name"></spring:message></b>
	<jstl:out value="${customer.name}" /> <jstl:out value="${customer.surname}" /></li>
	
	<li><b><spring:message code="customer.username"></spring:message></b>
	<jstl:out value="${customer.userAccount.username}" /></li>
	
	<li><b><spring:message code="customer.postalAddress"></spring:message></b>
		<jstl:out value="${customer.postalAddress}" /></li>

	<li><b><spring:message code="customer.phoneNumber"></spring:message></b>
		<jstl:out value="${customer.phoneNumber}" /></li>

	<li><b><spring:message code="customer.email"></spring:message></b> <jstl:out
			value="${customer.emailAddress}" /></li>

</ul>