(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var APP = window.APP || {};
	APP.header = APP.header || {};
	APP.header.logout = APP.header.logout || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 

	function init() {
		var logoutBtn = document.querySelector("#logout");
		logoutBtn.addEventListener("click", function() {
			Ajax.DELETE({
				"url":'/api/v1/session',
				"callback": function() {location.href="/";}
			});
		}, false);
	}

	APP.header.logout = {
		init: init
	}

	window.APP = APP;
}(this));
