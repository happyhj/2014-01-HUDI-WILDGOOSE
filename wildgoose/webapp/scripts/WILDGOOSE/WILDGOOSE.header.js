(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};

	// 의존성 선언 
	var Ajax = CAGE.ajax;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
	var Popup = CAGE.ui.popup;
	var Account = WILDGOOSE.account;
	
	function init(){
		var joinBtn = document.querySelector("#join");
		
		var joinPopup = new Popup.ajaxPopup({
			element: joinBtn,
			templateUrl: "/api/v1/templates/account.html",
			templateLoader: function(AjaxResponse) {
				return JSON.parse(AjaxResponse).data.template;
			}
		});
		
		joinPopup.afteropen.add(function() {
			Account.addValidationEvent();
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", Account.signUpAccount.bind(this, joinPopup), false);
		});
		
		var loginBtn = document.querySelector("#login");
		
		var loginPopup = new Popup.ajaxPopup({
			element: loginBtn,
			templateUrl: "/api/v1/templates/login.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				var randNum = JSON.parse(AjaxResponse).data.rand;
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum
				}, templateStr);		
			}
		});
		
		loginPopup.afteropen.add(function() {
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", Account.loginAccount.bind(this, loginPopup), false);
		});
		
		var logoutBtn = document.querySelector("#logout");
		logoutBtn.addEventListener("click", function() {
			Ajax.DELETE({
				"url":'/api/v1/session',
				"callback":function() {location.href="/";}
			});
		}, false);
		
		var timelineBtn = document.querySelector("#timeline");
		timelineBtn.addEventListener("click", function() {
			var Etc = WILDGOOSE.etc;
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/timeline".replace("?user_id?", userId);;
		}, false);
		
		var favoriteBtn = document.querySelector("#favorite");
		favoriteBtn.addEventListener("click", function() {
			var Etc = WILDGOOSE.etc;
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/favorites".replace("?user_id?", userId);
		}, false);
		
		var mypageBtn = document.querySelector("#mypage");
		console.log(mypageBtn);
		mypageBtn.addEventListener("click", function() {
			var Etc = WILDGOOSE.etc;
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/mypage".replace("?user_id?", userId);
		}, false);
		
		function updateTopbar(isLogined) {
			var joinBtn = document.querySelector("#join");
			var loginBtn = document.querySelector("n#login");
			var logoutBtn = document.querySelector("#logout");
			var timelineBtn = document.querySelector("#timeline");
			var favoriteBtn = document.querySelector("#favorite");
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
	}

	WILDGOOSE.header = {
		init: init
	}

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));