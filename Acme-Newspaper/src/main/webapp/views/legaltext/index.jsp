

<%--
   * index.jsp
   *
   * Copyright (C) 2017 Universidad de Sevilla
   * 
   * The use of this project is hereby constrained to the conditions of the 
   * TDG Licence, a copy of which you may download from 
   * http://www.tdg-seville.info/License.html
   --%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
   uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<h3><spring:message code="master.page.terms"/></h3>
${termsAndConditions.body}

<security:authorize access="hasRole('ADMIN')">
   <br />
   <acme:cancel code="master.page.edit"
      url="legaltext/administrator/edit.do?legalTextId=${termsAndConditions.id}" />
</security:authorize>

<h3><spring:message code="master.page.cookies"/></h3>
${cookies.body}

<security:authorize access="hasRole('ADMIN')">
   <br />
   <acme:cancel code="master.page.edit"
      url="legaltext/administrator/edit.do?legalTextId=${cookies.id}" />
</security:authorize>

