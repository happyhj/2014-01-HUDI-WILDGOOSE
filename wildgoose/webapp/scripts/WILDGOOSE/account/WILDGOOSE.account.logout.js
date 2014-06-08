(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};
	WILDGOOSE.account.logout = WILDGOOSE.account.logout || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	
	function Logout(args) {
		this.method = args.method;
		this.url = args.url;
	};
	Logout.prototype = {
		constructor: "Logout",
		exec: function(callback) {
			Ajax[this.method]({
				"url":this.url,
				"success": function() {
					callback();
				},
				"failure": function() {
					console.log("FAIL!");
				}
			});
		}
	};
	
	
	WILDGOOSE.account.logout = Logout;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));