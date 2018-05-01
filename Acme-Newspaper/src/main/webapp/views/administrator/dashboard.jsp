<%--
 * action-2.jsp
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

<!-- LEVEL C -->
<b><spring:message code="admin.avgNewspapersByWriter"/></b></br>
${avgNewspapersByWriter}</br>
<b><spring:message code="admin.stddevNewspapersByWriter"/></b></br>
${stddevNewspapersByWriter}</br>
<b><spring:message code="admin.avgArticlesByWriter"/></b></br>
${avgArticlesByWriter}</br>
<b><spring:message code="admin.stddevArticlesByWriter"/></b></br>
${stddevArticlesByWriter}</br>
<b><spring:message code="admin.avgArticlesByNewspaper"/></b></br>
${avgArticlesByNewspaper}</br>
<b><spring:message code="admin.stddevArticlesByNewspaper"/></b></br>
${stddevArticlesByNewspaper}</br>
<b><spring:message code="admin.newspapersUpper10PerCentArticles"/></b></br>
${newspapersUpper10PerCentArticles}</br>
<b><spring:message code="admin.newspapersLower10PerCentArticles"/></b></br>
${newspapersLower10PerCentArticles}</br>
<b><spring:message code="admin.ratioNewspapersCreated"/></b></br>
${ratioNewspapersCreated}</br>
<b><spring:message code="admin.ratioArticlesCreated"/></b></br>
${ratioArticlesCreated}</br>

<!--LEVEL B -->

<b><spring:message code="admin.avgFollowUpsPerArticle"/></b></br>
${avgFollowUpsPerArticle}</br>
<b><spring:message code="admin.followUpsPerArticleUpOneWeek"/></b></br>
${followUpsPerArticleUpOneWeek}</br>
<b><spring:message code="admin.followUpsPerArticleUpTwoWeek"/></b></br>
${followUpsPerArticleUpTwoWeek}</br>
<b><spring:message code="admin.avgChirpsPerUser"/></b></br>
${avgChirpsPerUser}</br>
<b><spring:message code="admin.stddevChirpsPerUser"/></b></br>
${stddevChirpsPerUser}</br>
<b><spring:message code="admin.ratioUserChirpsUpper75Avg"/></b></br>
${ratioUserChirpsUpper75Avg}</br>

<!-- LEVEL A -->
<b><spring:message code="admin.ratioPublicVsPrivateNewspapers"/></b></br>
${ratioPublicVsPrivateNewspapers}</br>
<b><spring:message code="admin.avgArticlesPerPublicNewspapers"/></b></br>
${avgArticlesPerPublicNewspapers}</br>
<b><spring:message code="admin.avgArticlesPerPrivateNewspapers"/></b></br>
${avgArticlesPerPrivateNewspapers}</br>
<b><spring:message code="admin.ratioSuscribersPerPrivateNewspapersVsCustomers"/></b></br>
${ratioSuscribersPerPrivateNewspapersVsCustomers}</br>
<b><spring:message code="admin.avgRatioPublicVsPrivateNewspapers"/></b></br>
${avgRatioPublicVsPrivateNewspapers}</br>


<!-- 2.0 -->

<!-- LVL C -->

<b><spring:message code="admin.ratioNewspaperWithAdsVsWithoutAds"/></b></br>
${ratioNewspaperWithAdsVsWithoutAds}</br>
<b><spring:message code="admin.rationAdsWithSpamwords"/></b></br>
${rationAdsWithSpamwords}</br>

<!-- LVL B -->

<b><spring:message code="admin.avgNewsPerVol"/></b></br>
${avgNewsPerVol}</br>
<b><spring:message code="admin.ratioSusVolVsSus"/></b></br>
${ratioSusVolVsSus}</br>