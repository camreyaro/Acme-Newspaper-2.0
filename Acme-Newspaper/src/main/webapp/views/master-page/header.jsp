<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<img src="images/logo.png" alt="Sample Co., Inc." />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<li><a class="fNiv"><spring:message	code="master.page.newspaper" />s</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="newspaper/search.do"><spring:message code="master.page.newspaper.search" /></a></li>
					<li><a href="newspaper/article/search.do"><spring:message code="article.search" /></a></li>
					<li><a href="newspaper/list.do"><spring:message code="master.page.newspaper" />s</a></li>
					<security:authorize access="!hasRole('CUSTOMER')">
						<li><a href="volumen/list.do"><spring:message code="master.page.volumens" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="volumen/list.do"><spring:message code="master.page.noSusVolumens" /></a></li>
					</security:authorize>
				</ul>
			</li>
		<li><a href="user/list.do"  class="fNiv"><spring:message code="master.page.listUser" /></a></li>
		
		<security:authorize access="hasRole('USER')">
			<li><a href="chirp/user/list.do"><spring:message	code="master.page.chirp" />s</a></li>
			<li><a class="fNiv"><spring:message	code="master.page.user" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="newspaper/user/avaibleList.do"><spring:message code="newspaper.avaibleList" /></a></li>
					<li><a href="newspaper/user/list.do"><spring:message code="master.page.my" /> <spring:message code="master.page.newspaper" />s</a></li>
					<li><a href="newspaper/article/user/myList.do"><spring:message code="article.myList" /></a></li>
					<li><a href="volumen/user/myList.do"><spring:message code="volumen.menu.user"></spring:message></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('AGENT')">
			<li><a class="fNiv"><spring:message	code="master.page.agent" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="advertisement/agent/list.do"><spring:message code="master.page.my" /> <spring:message code="master.page.advertisement" />s</a></li>
					<li><a href="newspaper/advertisement/agent/list.do"><spring:message code="master.page.newspaperAdv" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message	code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="administrator/listTabooWords.do"><spring:message code="master.page.spamWordList" /></a></li>
					<li><a href="newspaper/article/administrator/spamArticlesList.do"><spring:message code="admin.spamArticlesList" /></a></li>
					<li><a href="newspaper/administrator/spamNewspapersList.do"><spring:message code="admin.spamNewspapersList" /></a></li>
					<li><a href="chirp/administrator/spamChirpsList.do"><spring:message code="admin.spamChirpsList"/></a></li>
					<li><a href="advertisement/administrator/spamAdvertisementsList.do"><spring:message code="admin.spamAdvertisementsList" /></a></li>
					<li><a href="administrator/dashboard.do">Dashboard</a></li>
					<li><a href="legaltext/index.do"><spring:message code="master.page.terms" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('CUSTOMER')">
			<li><a class="fNiv"><spring:message	code="master.page.customer" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="suscriptionVolumen/myList.do"><spring:message code="master.page.my.suscription.volumen" /></a></li>
								
				</ul>
			</li>
		</security:authorize> 
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
			<li><a class="fNiv"><spring:message code="master.page.submit" /></a>
			<ul>
					<li class="arrow"></li>
					<li><a href="user/register.do"><spring:message code="master.page.registerUser" /></a></li>
					<li><a href="customer/register.do"><spring:message code="master.page.registerCustomer" /></a></li>
					<li><a href="agent/register.do"><spring:message code="master.page.registerAgent" /></a></li>
					</ul>
			
			</li>
			
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('USER')">
						<li><a href="user/edit.do"><spring:message code="master.page.editUser" /></a></li>
						<li><a href="user/display.do"><spring:message code="master.page.displayUser" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="customer/display.do"><spring:message code="master.page.customer.display" /></a></li>
						<li><a href="customer/edit.do"><spring:message code="master.page.customer.edit" /></a></li>	
					</security:authorize>
					<security:authorize access="hasRole('AGENT')">
						<li><a href="agent/display.do"><spring:message code="master.page.agent.display" /></a></li>
						<li><a href="agent/edit.do"><spring:message code="master.page.agent.edit" /></a></li>	
					</security:authorize>
					<li><a href="folder/list.do"><spring:message code="master.page.folder" /></a></li>						
					<li><a href="message/create.do?all=0"><spring:message code="master.page.sendMessage" /></a></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="message/create.do?all=1"><spring:message code="message.send.all" /></a></li>
					</security:authorize>
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

