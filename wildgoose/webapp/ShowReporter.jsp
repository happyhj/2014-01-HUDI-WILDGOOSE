<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/show_reporter.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />

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
			<div class="reporter-profile" >
				<h2 class="reporter-name">${ requestScope.reporter.name }</h2>
				<h3 class="email">${ requestScope.reporter.email }</h3>
				<h4 class="press-name">${ requestScope.reporter.pressName }</h4>
			</div>
			
			<!-- 통계 정보 -->
			<div id="donut-graph" class="card card-graph">
				<span class="graph-title">섹션 비율</span>
				<div class="graph"></div>
			</div>
			<div id="brokenline-graph" class="card card-graph">
				<span class="graph-title">날짜별 기사 작성 </span>
				<div class="graph"></div>
			</div>
			<div id="bar-graph" class="card card-graph" style="height:200px">
				<span class="graph-title">낚시 단어 수</span>
				<div class="graph"></div>
			</div>
			<div id="radar-graph" class="card card-graph" style="height:200px">
				<span class="graph-title">기자의 특성</span>
				<div class="graph"></div>

			</div>
			
			<!-- 최신기사 리스트 카드 -->
			<div class="card card-article-list">
				<h1>${ requestScope.reporter.name }기자의 최신기사</h1>
				<c:forEach var="article" items="${ requestScope.articles }">
					<li class="article">
						<div class="article-title">
							<h2 class="title"><a href="${ article.url }" target="_blank">
							<span style="display:none">[${ article.sectionId }]</span>${ article.title }</a></h2>
						</div>
						<div class="article-datetime">
							<div class="date">${ article.datetime }</div>
						</div>
					</li>
				</c:forEach>
			</div>
		</div>
		<footer class="footer"></footer>
	</div>
</body>
</html>