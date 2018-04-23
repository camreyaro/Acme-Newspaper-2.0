<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="suscription/suscribe.do" modelAttribute="suscription">

	<form:hidden path="newspaper" />
	<form:hidden path="creditCard.brandName" id="brand"/>
	
	
	<input id="newspaperId" name="newspaperId" type="hidden" value="${newspaperId }"/>
	
	
	<acme:icon-textbox code="creditCard.number" path="creditCard.number" extra="GetCreditCard(this.value)"/>
	<acme:textbox  code="creditCard.holderName" path="creditCard.holderName"/>
	
	<acme:textbox  code="creditCard.cvvCode" path="creditCard.cvvCode" />
	<acme:textbox  code="creditCard.expirationMonth" path="creditCard.expirationMonth"/>
	<acme:textbox  code="creditCard.expirationYear" path="creditCard.expirationYear"/>
	
	<acme:submit code="master.page.save"  name="save"/>
</form:form>