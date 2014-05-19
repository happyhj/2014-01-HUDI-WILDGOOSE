<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search_reporter.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/search.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card-media.css" />
<title>Wildgoose</title>

<div class="wrap">
	<header class="header">
		<c:if test="${ empty sessionScope.userId }">
			<button class="header-btn" id="join">가입하기</button>
			<button class="header-btn" id="login">로그인하기</button>
		</c:if>
		<c:if test="${ not empty sessionScope.userId }">
			<button class="header-btn">로그아웃</button>
		</c:if>
	</header>
	<div class="container">
		<div class="logo">
			<a href="/"><img src="image/logo.png" alt="wildgoose logo" class="logo-image"/></a>
		</div>
		<div class="search search-column">
			<form action="./" method="get" >
				<li class="search-query-entry">
					<input type="search" autocomplete="off" id="query-entry" name="q" placeholder="기자, URL검색" value="${ requestScope.searchQuery }" />
				</li>
				<li class="search-button">
					<input type="submit" id="search-action" value ="Search"/>
				</li>
			</form>
			<table class="searched-box">
  				<tbody>
    				<tr>
				    </tr>
  				</tbody>
			</table>
			
		</div>
		<div class="search-result">
			<ul>
				<c:choose>
					<%-- message 존재시 --%>
					<c:when test="${ not empty requestScope.message }">
						<span>${ requestScope.message }</span>
					</c:when>
					<%-- searchQuery 존재시 --%>
					<c:when test="${ not empty requestScope.searchQuery }">
						<c:if test="${ empty requestScope.reporterCards }">
							<span>${ requestScope.searchQuery }에 대한 검색 결과가 없습니다.</span>
						</c:if>
						<c:forEach var="reporterCard" items="${ requestScope.reporterCards }">
							<li class="card card-reporter">
								<%@ include file = "template/reporterCard.html" %>
							</li>
						</c:forEach>
					</c:when>
				</c:choose>
			</ul>
		</div>
		<%-- searchQuery 존재시 and 검색 결과가 더 많을 때 --%>
		<c:if test="${ not empty requestScope.searchQuery and requestScope.hasMoreCards == true }">
			<div class="search-more">
				<div class="search-button search-button-ajax">더보기</div>
				<div class="search-state search-state-hidden">
				<div class="state-search-query hidden">${ requestScope.searchQuery }</div>
				<div class="state-search-total hidden">${ requestScope.totalNum }</div>
				</div>
			</div>
		</c:if>
	</div>
	<footer class="footer"></footer>
</div>
<script>
var inputEl = document.getElementById("query-entry");
inputEl.focus();

/*
 * attatch button click event
 */
// 함수가 아무것도 리턴하지 않는 경우 사용할 수 있는
// 즉시실행 함수 패턴 중 하나
!function() {
	var joinBtn = document.querySelector(".header-btn#join");
	joinBtn.addEventListener("click", function() {
		var url = "/api/v1/subhtml/create_account";
		modal.openModalWindow(url, function() {
			addValidationEvent();
			joinBtn.addEventListener("click", signUpAccout, false);
		})
	}, false);
	var loginBtn = document.querySelector(".header-btn#login");
	loginBtn.addEventListener("click", function() {
		var url = "/api/v1/subhtml/authenticate_user";	
		modal.openModalWindow(url, function() {
			loginBtn.addEventListener("click", loginAccount, false);
		})
	}, false);
}();


</script>
<script type="text/javascript" src="/scripts/sha256.js"></script>
<script type="text/javascript" src="/scripts/util.js"></script>
<script type="text/javascript" src="/scripts/modal.js"></script>
<script type="text/javascript" src="/scripts/searchReporter.js"></script>
<script type="text/javascript" src="/scripts/auto-complement.js"></script>
<script type="text/javascript" src="/scripts/validation.js"></script>
<script type="text/javascript" src="/scripts/account.js"></script>
