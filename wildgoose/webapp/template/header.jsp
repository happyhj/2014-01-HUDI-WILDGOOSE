<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<link type="text/css" rel="stylesheet" href="/stylesheet/header.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/modal.css" />

<c:if test="${ empty sessionScope.userId }">
	<button class="header-btn" id="join">가입하기</button>
	<button class="header-btn" id="login">로그인하기</button>
	<button class="header-btn hidden" id="logout">로그아웃</button>
</c:if>
<c:if test="${ not empty sessionScope.userId }">
	<button class="header-btn hidden" id="join">가입하기</button>
	<button class="header-btn hidden" id="login">로그인하기</button>
	<button class="header-btn" id="logout">로그아웃</button>
</c:if>
<script type="text/javascript" src="/scripts/lib/sha256.js"></script>
<script type="text/javascript" src="/scripts/validation.js"></script>
<script type="text/javascript" src="/scripts/account.js"></script>
<script type="text/javascript" src="/scripts/modal.js"></script>
<script>
!function() {
	var joinBtn = document.querySelector(".header-btn#join");
	joinBtn.addEventListener("click", function() {
		var url = "/api/v1/subhtml/create_account";
		WILDGOOSE.ui.modal.openModalWindow(url, function() {
			addValidationEvent();
			var btn = arguments[0];
			btn.addEventListener("click", signUpAccout, false);
		})
	}, false);
	
	var loginBtn = document.querySelector(".header-btn#login");
	loginBtn.addEventListener("click", function() {
		var url = "/api/v1/subhtml/authenticate_user";	
		WILDGOOSE.ui.modal.openModalWindow(url, function() {
			var btn = arguments[0];
			btn.addEventListener("click", loginAccount, false);
		})
	}, false);
	
	var logoutBtn = document.querySelector(".header-btn#logout");
	logoutBtn.addEventListener("click", function() {
		Ajax.DELETE('/api/v1/session');
		updateTopbar(false);
	}, false);
}();

function updateTopbar(isLogined) {
	var joinBtn = document.querySelector(".header-btn#join");
	var loginBtn = document.querySelector(".header-btn#login");
	var logoutBtn = document.querySelector(".header-btn#logout");
	console.log(logoutBtn);
	if (isLogined == true) {
		joinBtn.className = "header-btn hidden";
		loginBtn.className = "header-btn hidden";
		logoutBtn.className = "header-btn";
	} else {
		joinBtn.className = "header-btn";
		loginBtn.className = "header-btn";
		logoutBtn.className = "header-btn hidden";
	}
}
</script>