(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};

	// 하위 모듈 import
	var join = WILDGOOSE.header.join;
	var login = WILDGOOSE.header.login;
	var logout = WILDGOOSE.header.logout;
	var userpage = WILDGOOSE.header.userpage;

	function init(){
		join.init();
		login.init();
		logout.init();
		userpage.init();
	}

	WILDGOOSE.header = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;

}(this));