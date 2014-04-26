<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/reset.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/main.css" />
<title>Search Reporter</title>
<!--  
<form action="/HelloServlet" method="get">
<input type="search" id="query" name="query" value="${ requestScope.search_query }"/>
<input type="submit" value="SEARCH" onclick="test()")/>
</form>
-->
<div class="wrap">
	<header class="header"></header>
	<div class="container">
		<div class="logo-container">
			<a href="/"><img src="image/logo.png" alt="wildgoose logo" class="logo-image"/></a>
			<!-- <a href="/SearchReporter"><img src="image/logo.png" alt="wildgoose logo" class="logo-image"/></a> -->
		</div>
		<div class="search-container">
			<!-- <form action="./SearchReporter" method="get" > -->
			<form action="./" method="get" >
				<div class="search-entry column">
					<input type="search" id="query-entry" class="text-entry query-entry" name="q" placeholder="기자검색" value="${ requestScope.searchQuery }" />
				</div>
				<div class="search-button column">
					<input type="submit" id="search-action" value ="Search"/>
				</div>
			</form>
		</div>
		<div class="search-result">
			<ul>
				<c:if test="${ not empty requestScope.searchQuery }">
					<c:if test="${ empty requestScope.reporterCards }">
						<span>${ requestScope.searchQuery }에 대한 검색 결과가 없습니다.</span>
					</c:if>
				</c:if>
				<c:forEach var="reporterCard" items="${ requestScope.reporterCards }">				
					<li class="card reporter-card">
						<div class="email-container">
							<h3 class="email">
								<a href="/reporters/${ reporterCard.id }">${ reporterCard.name }</a>
							</h3>
							<p class="sub-email">${ reporterCard.email }</p>
							<div class="${ reporterCard.pressName } press-tag"></div>
						</div>
						<div class="article-container">
							${ reporterCard.articleTitle }
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<footer class="footer"></footer>
</div>
<script>

var inputEl = document.getElementById("query-entry");
//var linkEl = document.getElementById("search-action");
inputEl.focus();
/*
inputEl.addEventListener("keyup",function(e) {	
	if(document.activeElement === this && e.keyCode === 13) {
		linkEl.click();
	}
},false);
*/
</script>