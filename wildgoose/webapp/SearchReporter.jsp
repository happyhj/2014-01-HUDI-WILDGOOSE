<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<link type="text/css" rel="stylesheet" href="stylesheet/reset.css" />
<link type="text/css" rel="stylesheet" href="stylesheet/main.css" />
<title>Search Reporter</title>
<!--  
<form action="/HelloServlet" method="get">
<input type="search" id="query" name="query" value="${ requestScope.search_query }"/>
<input type="submit" value="SEARCH" onclick="test()")/>
</form>
-->
<div class="wrap viewport">
	<header class="header"></header>
	<div class="container">
		<div class="logo-container">
			<a href="/SearchReporter"><img src="image/logo.png" alt="wildgoose logo" class="logo-image"/></a>
		</div>
		<div class="search-container">
			<form action="./SearchReporter" method="get" >
				<div class="search-entry column">
					<input type="search" id="query-entry" class="text-entry query-entry" name="q" placeholder="기자를 검색어로 찾아보세요." value="${ requestScope.searchQuery }" />
				</div>
				<div class="search-button column">
					<input type="submit" id="search-action" value ="Search"/>
				</div>
			</form>
		</div>
		<div class="search-result">
			<c:choose>
				<c:when test="${ empty requestScope.webError }">
					<ul>
						<c:forEach var="reporter" items="${ requestScope.reporters }">
							<li class="card" >
								<h3 class="email">${ reporter.email }</h3>
								<p class="sub-email">${ reporter.authorInfo }</p>
								<h4 class="press-name">${ reporter.pressName }</h4>			
								<div class="article-container">
									<a href="${ reporter.articleURL }" target="_blank">${ reporter.articleTitle }</a>
								</div>
							</li>
						</c:forEach>
					</ul>
				</c:when>
				<c:otherwise>
						${ requestScope.webError.cause }
						${ requestScope.webError.notice }
				</c:otherwise>
			</c:choose>
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