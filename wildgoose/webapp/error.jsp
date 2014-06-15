<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/CAGE/src/CAGE.ui.parallax.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/error.css" />
<title>Wildgoose</title>
<style>
.parallax-stage .msg {
	width: 270px;
	height: 240px;
	
	position: absolute;
	top: 60px;
	left: 50%;
	margin-left: -350px;
}
.parallax-stage .msg > div {
	position: relative;
}

.parallax-stage .msg-code {
	color: #fff;
	font-size: 8em;
	font-weight: 800;
	hieght: 40%;
	line-height:70%;
	text-shadow: 2px 2px rgba(100, 100, 100, .5);
}

.parallax-stage .msg-content {
	width:250px;
	height: 130px;
	background: rgba(255,255,255,.9);
	border-radius: 5px;
	padding: 10px;
	
	color: #3265C0;
	font-size: 1.5em;
	font-weight: 600;
	
	-webkit-box-shadow: inset 1px 1px 1px 0 rgba(50,50,50,0.39);
	-moz-box-shadow: inset 1px 1px 1px 0 rgba(50,50,50,0.39);
	box-shadow: inset 1px 1px 1px 0 rgba(50,50,50,0.39);
	
	-webkit-box-shadow: 2px 2px 2px 0px rgba(50, 50, 50, .7);
	-moz-box-shadow:    2px 2px 2px 0px rgba(50, 50, 50, .7);
	box-shadow:         2px 2px 2px 0px rgba(50, 50, 50, .7);
	
	border: 1px solid rgba(255,255,255,.9);
}

.parallax-stage .msg-content .arrow {
	position:absolute;

	color: #fff;
	z-index: 100;
	top:100px;
	margin-left: 258px;
	font-size: 1em;
	opacity: .9;
	
	text-shadow: 2px 2px rgba(50, 50, 50, .7);
}
</style>

<div class="wrap">
	<header class="header">
		<%@ include file ="/header.jsp" %>
	</header>
	<div class="container">
	<div class="parallax-wrapper">
		<div class="parallax-bg">
			<img src="/image/error404-bg.png" data-rangex="5" data-rangey="5" class="bg parallax-item"/>
		</div>
		<div class="parallax-stage">
			<img src="/image/error404-logo.png"style="opacity:.4" data-rangex="7" data-rangey="4" data-reverse class="logo parallax-item"/>
			<img src="/image/error404-baked.png" data-rangex="12" data-rangey="3" class="primary parallax-item"/>
			<img src="/image/error404-sweatDrop.png"  data-rangex="14" data-rangey="8" class="sweat-drop parallax-item"/>
			
			<div data-rangex="40" data-rangey="18" class="msg parallax-item">
				<c:choose>
					<%-- server에서 처리가능한 에러인 경우 --%>
					<c:when test="${ not empty requestScope.data }">
						<div class="msg-code">
							<span>${ requestScope.data.status }</span>
						</div>
						
						<div class="msg-content">
							<span>${ requestScope.data.message }</span>
							<div class="arrow"><span>►</span></div>
						</div>
					</c:when>
					
					<%-- server에서 처리가능한 불가능한 에러인 경우 --%>
					<c:otherwise>
						<div class="msg-code">
							<span>${ requestScope['javax.servlet.error.status_code'] }</span>
						</div>
						
						<div class="msg-content">
							<span>${ requestScope['javax.servlet.error.request_uri'] }</span>
							<span>${ applicationScope.errorCodeMap['500'] }</span>
						</div>						
					</c:otherwise>
				</c:choose>
			</div>
			
			<!-- <img src="/image/error404-404msg.png"  data-rangex="40" data-rangey="18" class="msg parallax-item"/> -->
		</div>
	</div>
	<div class="search-container">
		<form class="input-query-entry" action="/">
		<label>WILDGOOSE에서 이름이나 이메일, 기사의 URL로 기자를 검색하세요</label><br/>
		<input class="search-input" type="text" name="q" placeholder="Search"/>
		<input type="submit" value="검색"/>
		</form>
	</div>
	</div>
	<footer class="footer"></footer>
</div>
<script src="/CAGE/src/CAGE.ui.parallax.js"></script>
<script>
var parallax = new CAGE.ui.parallax();
</script>