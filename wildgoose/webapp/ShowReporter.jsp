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

<script type="text/javascript" src="/webjars/d3js/3.4.4/d3.min.js"></script>

<script type="text/javascript" src="/scripts/util.js"></script>
<script type="text/javascript" src="/scripts/graph.js"></script>
<script type="text/javascript" src="/scripts/BrokenLine.js"></script>

<title>${ requestScope.name } 기자</title>
</head>
<body>
	<div class="wrap viewport">
		<header class="header"></header>
		<div class="container test-outline">
		
			<!-- 기자 정보 -->
			<div class="card reporter-card" >
				<h2 class="reporter-name">${ requestScope.reporter.name }</h2>
				<h3 class="email">${ requestScope.reporter.email }</h3>
				<h4 class="press-name">${ requestScope.reporter.pressName }</h4>
			</div>
			
			<!-- 통계 정보 -->
			<div>
				<div class="card stat-card">
					<h1>section 비율</h1>
					<div class="refresh-button">refresh</div>
					<svg id="svg-donut"></svg>
				</div>
				<div class="article-brkline-card">
					<svg id="svg-brokenLine" viewport="0 0 1000 740"></svg>
				</div>
			</div>
			
			<!-- 최신기사 리스트  -->
			<div class="form">
				<h1>${ requestScope.reporter.name }기자의 최신기사</h1>
				<ul class="latest">
					<c:forEach var="article" items="${ requestScope.articles }">
						<%-- <li class="list">
							<h2 class="title"><a href="${ article.url } target="_blank">[${ article.section_id }]${ article.title }</a></h2>
							<div class="date">${ article.datetime }</div>
							<div class="content">${ article.content }</div>
						</li> --%>
						<li class="list">
							<div class="article_title">
								<h2 class="title"><a href="${ article.url } target="_blank">[${ article.sectionId }]${ article.title }</a></h2>
							</div>
							<div class="article_datetime">
								<div class="date">${ article.datetime }</div>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<footer class="footer"></footer>
	</div>
</body>
</html>