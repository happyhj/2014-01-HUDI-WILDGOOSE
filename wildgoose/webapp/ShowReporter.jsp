<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/articles.css" />
<link type="text/css" rel="stylesheet"
	href="/stylesheet/show_reporter.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />

<script type="text/javascript" src="/webjars/d3js/3.4.4/d3.min.js"></script>

<script type="text/javascript" src="/scripts/util.js"></script>
<script type="text/javascript" src="/scripts/graph.js"></script>
<script type="text/javascript" src="/scripts/graph-donut.js"></script>
<script type="text/javascript" src="/scripts/graph-brokenline.js"></script>
<script type="text/javascript" src="/scripts/graph-bar.js"></script>
<script type="text/javascript" src="/scripts/graph-radar.js"></script>

<title>${ requestScope.name }기자</title>
</head>
<body>
	<div class="wrap viewport">
		<header class="header"></header>
		<div class="container test-outline">

			<!-- 기자 정보 -->
			<div class="reporter-profile">
				<h2 class="reporter-name">${ requestScope.reporter.name }</h2>
				<h3 class="email">${ requestScope.reporter.email }</h3>
				<h4 class="press-name">${ requestScope.reporter.pressName }</h4>
			</div>

			<!-- 통계 정보 -->
			<div class="card card-graph">
				<div class="card-section card-section-header">
					<h3 class="title">
						섹션 비율
					</h3>
				</div>				
				<div id="donut-graph" class="card-section">
					<div class="graph">
						
					</div>
				</div>
			</div>
			<div class="card card-graph">
				<div class="card-section card-section-header">
					<h3 class="title">
						날짜별 기사 작성
					</h3>
				</div>				
				<div id="brokenline-graph" class="card-section">
					<div class="graph">
						
					</div>
				</div>
			</div>
			<div class="card card-graph">
				<div class="card-section card-section-header">
					<h3 class="title">
						낚시 단어 수
					</h3>
				</div>				
				<div id="bar-graph" class="card-section">
					<div class="graph">
						
					</div>
				</div>
			</div>
			<div class="card card-graph">
				<div class="card-section card-section-header">
					<h3 class="title">
						기자의 특성
					</h3>
				</div>				
				<div id="radar-graph" class="card-section">
					<div class="graph">
						
					</div>
				</div>
			</div>


			<!-- 최신기사 리스트 카드 -->
			<div class="card card-article-list">
				<div class="card-section card-section-header">
					<h3 class="title">
						${ requestScope.reporter.name }기자의최신기사
					</h3>
				</div>
				<div class="card-section card-section-recent-headlines">
					<c:forEach var="article" items="${ requestScope.articles }">
						<div class="article">
							<a href="${ article.url }" target="_blank"> 
							<span class="article_title">${ article.title }</span> 
							<c:set var="date" value="${fn:split(article.datetime, ' ')}" />
							<span class="datetime">${ date[0] }</span>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<footer class="footer"></footer>
	</div>
</body>
</html>
