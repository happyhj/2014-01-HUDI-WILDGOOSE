(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};
	WILDGOOSE.header.logout = WILDGOOSE.header.logout || {};

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

	WILDGOOSE.header.logout = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
}(this));
