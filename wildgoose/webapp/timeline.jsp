<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search_reporter.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="../stylesheet/article_card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card-media.css" />

<title>Wildgoose</title>

<div class="wrap">
	<header class="header">
		<%@ include file ="template/header.jsp" %>
	</header>
	<div class="container">
		<div class="timeline-result">
			<ul>
				<%-- session 존재시 --%>
				<c:if test="${ not empty sessionScope.userId }">
					<c:forEach var="article" items="${ requestScope.articles }">
					<li class="card">
						<%@ include file = "/template/articleCard.jsp" %>
					</li>
					</c:forEach>
				</c:if>
				<%-- session 없을시 --%>
				<c:if test="${ empty sessionScope.userId }">
					로그인 필요
				</c:if>
			</ul>
		</div>
	</div>
	<footer class="footer"></footer>
</div>
<script type="text/javascript" src="/scripts/util.js"></script>
<script type="text/javascript" src="/scripts/article.js"></script>
