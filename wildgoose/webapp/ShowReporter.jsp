<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="/stylesheet/reset.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/main.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/reporter.css" />
<title>${ requestScope.name } 기자</title>
</head>
<body>
	<div class="wrap viewport">
		<header class="header"></header>
		<div class="container test-outline">
			<div class="card" >
				<h2 class="reporter-name">${ requestScope.name }</h2>
				<h3 class="email">${ requestScope.reporter.email }</h3>
				<p class="sub-email">${ requestScope.reporter.authorInfo }</p>
				<h4 class="press-name">${ requestScope.reporter.pressName }</h4>
			</div>
			
			<c:if test="${ empty requestScope.webError }">
			<div class="form">
				<h1>${ requestScope.name }기자의 최신기사</h1>
				<ul class="latest">
					<c:forEach var="article" items="${ requestScope.articles }">
						<li class="list">
							<h2 class="title"><a href="${ article.url } target="_blank">[${ article.section_id }]${ article.title }</a></h2>
							<div class="date">${ article.datetime }</div>
							<div class="content">${ article.content }</div>
						</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
		<footer class="footer"></footer>
	</div>
</body>
</html>