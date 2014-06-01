<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<link type="text/css" rel="stylesheet" href="/stylesheet/header.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/modal.css" />

<a href="/"><img src="/image/logo.png" alt="wildgoose logo" class="header-logo-image"/></a>
<c:if test="${ empty sessionScope.userId }">
	<span id ="userId" class="hidden"></span>
	<button class="header-btn" id="join" href="/api/v1/templates/account.html">가입하기</button>
	<button class="header-btn" id="login" href="/api/v1/templates/login.html">로그인하기</button>
	<button class="header-btn hidden" id="logout">로그아웃</button>
	<button class="header-btn hidden" id="timeline">Timeline</button>
	<button class="header-btn hidden" id="favorite">favorite</button>
</c:if>
<c:if test="${ not empty sessionScope.userId }">
	<span id ="userId" class="hidden">${sessionScope.userId}</span>
	<button class="header-btn hidden" id="join" href="/api/v1/templates/account.html">가입하기</button>
	<button class="header-btn hidden" id="login" href="/api/v1/templates/login.html">로그인하기</button>
	<button class="header-btn" id="logout">로그아웃</button>
	<button class="header-btn" id="timeline">Timeline</button>
	<button class="header-btn" id="favorite">favorite</button>
</c:if>

<link rel="stylesheet" type="text/css" href="/CAGE/src/CAGE.ui.popup.css">
<script type="text/javascript" src="/CAGE/src/CAGE.ajax.js"></script>
<script type="text/javascript" src="/CAGE/src/CAGE.util.js"></script>
<script type="text/javascript" src="/CAGE/src/CAGE.ui.popup.js"></script>

<script type="text/javascript" src="/scripts/lib/sha256.js"></script>
<script type="text/javascript" src="/scripts/validation.js"></script>
<script type="text/javascript" src="/scripts/account.js"></script>
<script>
	var Popup = CAGE.ui.popup;
	var Util = CAGE.util;

	var joinBtn = document.querySelector("#join");
	
	var joinPopup = Popup.ajaxPopup({
		element: joinBtn,
		transitionEffect: "turn", // turn
		callbacks: {
			afteropen: function() {
				addValidationEvent();
				var btn = arguments[0];
				btn.addEventListener("click", signUpAccout, false);
			},
			afterclose: function() {
				//alert("afterclose");	
			}
		},
		templateLoader: function(AjaxResponse) {
			return JSON.parse(AjaxResponse).data.template;
		}
	});
	var loginBtn = document.querySelector("#login");
	
	var loginPopup = Popup.ajaxPopup({
		element: loginBtn,
		transitionEffect: "turn", // turn
		callbacks: {
			afteropen: function() {
				var btn = arguments[0];
				btn.addEventListener("click", loginAccount, false);
			},
			afterclose: function() {
				//alert("afterclose");	
			}
		},
		templateLoader: function(AjaxResponse) {
			var templateStr = JSON.parse(AjaxResponse).data.template;
			var randNum = JSON.parse(AjaxResponse).message;
			var compiler = Util.getTemplateCompiler(templateStr);
			return compiler({
				"randNum": randNum
			});		
		}
	});

	var logoutBtn = document.querySelector(".header-btn#logout");
	logoutBtn.addEventListener("click", function() {
		Ajax.DELETE({"url":'/api/v1/session'});
		updateTopbar(false);
	}, false);
	
	var timelineBtn = document.querySelector(".header-btn#timeline");
	timelineBtn.addEventListener("click", function() {
		location.href = "/users/?user_id?/timeline";
	}, false);
	
	var favoriteBtn = document.querySelector(".header-btn#favorite");
	favoriteBtn.addEventListener("click", function() {
		location.href = "/users/?user_id?/favorite";
	}, false);

function updateTopbar(isLogined) {
	var joinBtn = document.querySelector(".header-btn#join");
	var loginBtn = document.querySelector(".header-btn#login");
	var logoutBtn = document.querySelector(".header-btn#logout");
	var timelineBtn = document.querySelector(".header-btn#timeline");
	var favoriteBtn = document.querySelector(".header-btn#favorite");
	console.log(logoutBtn);
	if (isLogined == true) {
		joinBtn.className = "header-btn hidden";
		loginBtn.className = "header-btn hidden";
		logoutBtn.className = "header-btn";
		timelineBtn.className = "header-btn";
		favoriteBtn.className = "header-btn";
	} else {
		joinBtn.className = "header-btn";
		loginBtn.className = "header-btn";
		logoutBtn.className = "header-btn hidden";
		timelineBtn.className = "header-btn hidden";
		favoriteBtn.className = "header-btn hidden";
	}
}
</script>
