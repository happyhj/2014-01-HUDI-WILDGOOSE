(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};
	WILDGOOSE.account.join = WILDGOOSE.account.join || {};

	// 의존성 주입.
	var Account = WILDGOOSE.account.super_type;
	
	function Join(args) {
		Account.call(this, args);
	};
	
	Join.prototype = new Account();
	Join.prototype.constructor = Join;
	Join.prototype._getPayload = function(){
		var email = escape(this.selected.email.value);
		var password = SHA256(escape(this.selected.password.value));
		var payload = "email=" + email + "&password=" + password;
		return payload;
	};
	
	
	
	WILDGOOSE.account.join = Join;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));