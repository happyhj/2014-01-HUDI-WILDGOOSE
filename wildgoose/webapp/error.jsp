<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/reset.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/main.css" />
<title>Error Page</title>

<div class="wrap">
	<header class="header"></header>
	<div class="container">
		<div class="logo-container">
			<a href="/SearchReporter"><img src="image/logo.png" alt="wildgoose logo" class="logo-image"/></a>
		</div>
		<div class="search-container">
			<form action="./SearchReporter" method="get" >
				<div class="search-entry column">
					<input type="search" id="query-entry" class="text-entry query-entry" name="q" placeholder="기자검색" value="${ requestScope.searchQuery }" />
				</div>
				<div class="search-button column">
					<input type="submit" id="search-action" value ="Search"/>
				</div>
			</form>
		</div>
		<div class="search-result">
			error page
		</div>
	</div>
	<footer class="footer"></footer>
</div>