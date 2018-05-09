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

	<form:form action="advertisement/agent/edit.do" modelAttribute="advertisement">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="creditCard.brandName" id="brand"/>
	
	<jstl:if test="${advertisement.id == 0 }">
	<form:hidden path="newspaper" />
	<form:hidden path="agent" />
	</jstl:if>

	<acme:textbox code="master.page.title" path="title"/>
	<acme:textbox code="master.page.picture" path="urlBanner" />
	<acme:textbox code="master.page.url" path="urlTargetPage"/>
	<acme:textbox code="master.page.price" path="price"/>
	<p/>
	<acme:icon-textbox code="creditCard.number" path="creditCard.number" extra="GetCreditCard(this.value)"/>
	<acme:textbox  code="creditCard.holderName" path="creditCard.holderName"/>
	
	<acme:textbox  code="creditCard.cvvCode" path="creditCard.cvvCode" />
	<acme:textbox  code="creditCard.expirationMonth" path="creditCard.expirationMonth"/>
	<acme:textbox  code="creditCard.expirationYear" path="creditCard.expirationYear"/>
	
	<acme:submit code="master.page.save"  name="save" />
	<jstl:if test="${advertisement.id == 0 }">
	<acme:cancel code="master.page.return" url="/newspaper/advertisement/agent/list.do" />
	</jstl:if>
	<jstl:if test="${advertisement.id != 0 }">
	<acme:cancel code="master.page.return" url="/advertisement/agent/list.do" />
	</jstl:if>
</form:form>