<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<link type="text/css" rel="stylesheet" href="/stylesheet/header.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/modal.css" />

<a href="/"><img src="/image/logo.png" alt="wildgoose logo" class="header-logo-image"/></a>
<c:if test="${ empty sessionScope.userId }">
	<span id ="userId" class="hidden"></span>
	<button class="header-btn" id="join">가입하기</button>
	<button class="header-btn" id="login">로그인하기</button>
	<button class="header-btn hidden" id="logout">로그아웃</button>
	<button class="header-btn hidden" id="timeline">Timeline</button>
	<button class="header-btn hidden" id="favorite">favorite</button>
</c:if>
<c:if test="${ not empty sessionScope.userId }">
	<span id ="userId" class="hidden">${sessionScope.userId}</span>
	<button class="header-btn hidden" id="join">가입하기</button>
	<button class="header-btn hidden" id="login">로그인하기</button>
	<button class="header-btn" id="logout">로그아웃</button>
	<button class="header-btn" id="timeline">Timeline</button>
	<button class="header-btn" id="favorite">favorite</button>
</c:if>

<link rel="stylesheet" type="text/css" href="/CAGE/src/CAGE.ui.popup.css">
<script type="text/javascript" src="/scripts/lib/sha256.js"></script>
<c:choose>
	<c:when test="${ initParam.debuggerMode eq 'on' }">
		<script type="text/javascript" src="/CAGE/src/CAGE.ajax.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.util.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.ui.popup.js"></script>
		
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.ui.validation.validCheck.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.account.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.header.js"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript" src="/CAGE/src/CAGE.min.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.min.js"></script>
	</c:otherwise>
</c:choose>
<script>
	var header_component = WILDGOOSE.header;
	header_component.init();
</script>