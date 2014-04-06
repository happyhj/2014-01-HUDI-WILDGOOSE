<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

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
<div class="wrap">
	<header class="header"></header>
	<div class="container">
		<div class="logo-container">
			<img src="image/logo.png" alt="wildgoose logo" class="logo-image"/>
		</div>
		<div class="search-container">
			<form action="./SearchReporter" method="get">
				<div class="search-entry">
					<input type="search" id="query-entry" class="text-entry query-entry" name="q" placeholder="기자를 검색어로 찾아보세요." value="${ requestScope.search_query }" />
				</div>
				<div class="search-button">
					<input type="submit" id="search-action" value ="Search"/>
				</div>
			</form>
		</div>
		<div class="search-result container">
			<ul>
				<c:forEach var="reporter" items="${ requestScope.reporters }">
					<li class="card" >
						<h3>${ reporter.email }</h3>
						<p>${ reporter.author_info }</p>
						${ reporter.press_name }					
						<a href="${ reporter.article_URL }" target="_blank">${ reporter.article_title }</a>
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