<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card-media.css" />

<c:choose>
	<%-- 보여줄 article이 있는경우 timeline-result를 보여줌 --%>
	<c:when test="${ not empty requestScope.data.articles }">
	<link type="text/css" rel="stylesheet" href="/stylesheet/me-article.css" />
	<link type="text/css" rel="stylesheet" href="/stylesheet/me.css" />
	</c:when>
		
	<%-- 보여줄 article이 없는 경우 intro-result를 보여줌 --%>
	<c:otherwise>
		<link type="text/css" rel="stylesheet" href="/stylesheet/me-intro.css" />
		
	</c:otherwise>
</c:choose>

<style>
.card {
	margin: 10px 0;
	width: 100%;
	box-sizing:border-box;
	-moz-box-sizing:border-box; /* Firefox */
} 
.card {
	transition: all 0.5s ease-in;
	-webkit-transition: all 0.5s ease-in 0;
}
.card.blur {
	opacity: 0.4;
}


</style>
<title>Wildgoose</title>

<div class="wrap">
	<header class="header">
		<%@ include file ="/header.jsp" %>
	</header>
	<%-- session 존재시 --%>
	<c:if test="${ not empty sessionScope.userId }"></c:if>
	<div class="container">
		
		<c:choose>
			<%-- 보여줄 article이 있는경우 timeline-result를 보여줌 --%>
			<c:when test="${ not empty requestScope.data.articles }">
				<div class="dashboard dashboard-left">
					<div class="dashboard-header">
						<h2>나의 기자</h2>
					</div>
					<%@ include file = "/jsp_templates/content-favorite-reporter.jsp" %>
				</div>
				
				
				<div class="content-main">
					<div class="content-main-header">
						<h2>타임라인</h2>
					</div>
					<%@ include file = "/jsp_templates/content-timeline.jsp" %>
				</div>
				
				<div class="dashboard dashboard-right">
					<div class="dashboard-header">
						<h2>추천기자</h2>
					</div>
					<%@ include file = "/jsp_templates/content-recommanded-reporter.jsp" %>
				</div>
			</c:when>
				
			<%-- 보여줄 article이 없는 경우 intro-result를 보여줌 --%>
			<c:otherwise>
				<div class="dashboard dashboard-left"></div>
				
				<div class="content-main">
					<div class="content-main-header">
						<h2>환영합니다!</h2>
					</div>
					<%@ include file = "/jsp_templates/content-recommanded-reporter.jsp" %>
					<%-- <%@ include file = "/jsp_templates/content-intro.jsp" %> --%>
				</div>
				
				<div class="dashboard dashboard-right"></div>
			</c:otherwise>
		</c:choose>
		
	</div>
	<footer class="footer"></footer>
</div>

<c:choose>
	<c:when test="${ initParam.debuggerMode eq 'on' }">
		<script type="text/javascript" src="/CAGE/src/CAGE.util.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.event.emitter.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.ajax.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/user/WILDGOOSE.user.js"></script>

		<script type="text/javascript" src="/scripts/WILDGOOSE/ui/WILDGOOSE.ui.favorite.me.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/ui/WILDGOOSE.drag.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/more/WILDGOOSE.more.super_type.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/more/WILDGOOSE.more.article.js"></script>
		
		<script type="text/javascript" src="/scripts/APP/APP.page.favorite.js"></script>
		<script type="text/javascript" src="/scripts/APP/APP.page.me.js"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript" src="/CAGE/src/CAGE.min.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.min.js"></script>
		<script type="text/javascript" src="/scripts/APP/APP.min.js"></script>
	</c:otherwise>
</c:choose>


<script>
window.addEventListener("load", function(evt){
	APP.page.me.init();
}, false);
</script>