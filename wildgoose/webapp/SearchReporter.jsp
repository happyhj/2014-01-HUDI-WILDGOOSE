<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search_reporter.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card-media.css" />
<title>Wildgoose</title>

<div class="wrap">
	<header class="header"></header>
	<div class="container">
		<div class="logo">
			<a href="/"><img src="image/logo.png" alt="wildgoose logo" class="logo-image"/></a>
		</div>
		<div class="search search-column">
			<form action="./" method="get" >
				<li class="search-query-entry">
					<input type="search" id="query-entry" name="q" placeholder="기자, URL검색" value="${ requestScope.searchQuery }" />
				</li>
				<li class="search-button">
					<input type="submit" id="search-action" value ="Search"/>
				</li>
			</form>
		</div>
		<div class="search-result">
			<ul>
				<c:choose>
					<%-- message 존재시 --%>
					<c:when test="${ not empty requestScope.message }">
						<span>${ requestScope.message }</span>
					</c:when>
					
					<%-- searchQuery 존재시 --%>
					<c:when test="${ not empty requestScope.searchQuery }">
						<c:if test="${ empty requestScope.reporterCards }">
							<span>${ requestScope.searchQuery }에 대한 검색 결과가 없습니다.</span>
						</c:if>
						<c:forEach var="reporterCard" items="${ requestScope.reporterCards }">				
							<li class="card card-reporter">
								<div class="card-section card-section-identity">
									<h3 class="name">
										<a href="/reporters/${ reporterCard.id }">${ reporterCard.name }</a>
									</h3>
									<p class="email">${ reporterCard.email }</p>
									<div class="${ reporterCard.pressName } press-tag"></div>
								</div>
								<div class="card-section card-section-headline">
									<h4 class="headline">${ reporterCard.articleTitle }</h4>
								</div>
							</li>
						</c:forEach>
					</c:when>
				</c:choose>
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
