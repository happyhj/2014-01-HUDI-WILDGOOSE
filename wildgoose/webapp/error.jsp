<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card-media.css" />
<title>Wildgoose</title>

<div class="wrap">
	<header class="header">
		<%@ include file ="jsp_templates/header.jsp" %>
	</header>
	<div class="container">
		<div class="logo">
			<a href="/"><img src="/image/logo.png" alt="wildgoose logo" class="logo-image"/></a>
		</div>
		<div class="search search-column">
			<form action="./" method="get" >
				<li class="search-query-entry">
					<input type="search" id="query-entry" name="q" placeholder="기자,URL검색" value="" />
				</li>
				<li class="search-button">
					<input type="submit" id="search-action" value ="Search"/>
				</li>
			</form>
		</div>
		<c:if test="${ not empty requestScope.data }">
		<div class="search-result">
			<span>${ requestScope.data.status }</span>
			<span>${ requestScope.data.message }</span>
		</div>
		</c:if>
		<c:if test="${ empty requestScope.data }">
		<div class="search-result">
			<span>404</span>
			<span>잘못된 접근입니다</span>
		</div>
		</c:if>
	</div>
	<footer class="footer"></footer>
</div>
<script>
var inputEl = document.getElementById("query-entry");
inputEl.focus();
</script>