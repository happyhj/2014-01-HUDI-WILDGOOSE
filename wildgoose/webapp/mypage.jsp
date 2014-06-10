<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/mypage.css" />

<title>mypage</title>

<div class="wrap">
	<header class="header">
		<%@ include file ="jsp_templates/header.jsp" %>
	</header>
	<div class="container">
		<div class="mypage-contents">
			<ul>
				<%-- session 존재시 --%>
				<c:if test="${ not empty sessionScope.userId }">
					<button class="btn" id="leave">탈퇴하기</button>
					<button class="btn" id="change-password">비밀번호 변경하기</button>
				</c:if>
				<%-- session 없을시 --%>
				<c:if test="${ empty sessionScope.userId }">
					로그인 필요
					<button class="hidden" id="leave">탈퇴하기</button>
					<button class="hidden" id="change-password">비밀번호 변경하기</button>
				</c:if>
			</ul>
		</div>
	</div>
	<footer class="footer"></footer>
</div>

<c:choose>
	<c:when test="${ initParam.debuggerMode eq 'on' }">
		<script type="text/javascript" src="/CAGE/src/CAGE.util.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.ajax.js"></script>
		
		<script type="text/javascript" src="/scripts/WILDGOOSE/user/WILDGOOSE.user.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/account/WILDGOOSE.account.withdraw.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/account/WILDGOOSE.account.change.pw.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/modal/WILDGOOSE.modal.withdraw.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/modal/WILDGOOSE.modal.change.pw.js"></script>
		<script type="text/javascript" src="/scripts/APP/APP.page.mypage.js"></script>
		
	</c:when>
	<c:otherwise>
		<script type="text/javascript" src="/CAGE/src/CAGE.min.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.min.js"></script>
		<script type="text/javascript" src="/scripts/APP/APP.min.js"></script>
	</c:otherwise>
</c:choose>
<script>
window.addEventListener("load", function(evt){
	APP.page.mypage.init();
}, false);
</script>
