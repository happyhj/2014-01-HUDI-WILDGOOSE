<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<c:if test="${ empty sessionScope.userId }">
	<button class="header-btn" id="join">가입하기</button>
	<button class="header-btn" id="login">로그인하기</button>
</c:if>
<c:if test="${ not empty sessionScope.userId }">
	<button class="header-btn" onclick="Ajax.DELETE('/api/v1/session');">로그아웃</button>
</c:if>