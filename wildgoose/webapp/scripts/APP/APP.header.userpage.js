(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var APP = window.APP || {};
	APP.header = APP.header || {};
	APP.header.userpage = APP.header.userpage || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 
	var Etc = WILDGOOSE.etc;

	function init() {
		var timelineBtn = document.querySelector("#timeline");
		timelineBtn.addEventListener("click", function() {
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/timeline".replace("?user_id?", userId);;
		}, false);
		
		var favoriteBtn = document.querySelector("#favorite");
		favoriteBtn.addEventListener("click", function() {
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/favorites".replace("?user_id?", userId);
		}, false);
		
		var mypageBtn = document.querySelector("#mypage");
		mypageBtn.addEventListener("click", function() {
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/mypage".replace("?user_id?", userId);
		}, false);
	}

	APP.header.userpage = {
		init: init
	}

	window.APP = APP;
}(this));