(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var APP = window.APP || {};
	APP.header = APP.header || {};

	// 하위 모듈 import
	var join = APP.modal.join;
	var login = APP.modal.login;
	var logout = APP.header.logout;
	var userpage = APP.header.userpage;

	function init(){
		join.init();
		login.init();
		logout.init();
		userpage.init();
	}

	APP.header = {
		init: init
	}

	window.APP = APP;

}(this));