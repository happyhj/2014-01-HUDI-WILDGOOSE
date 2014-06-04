(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};

	// 의존성 선언 
	var Account = WILDGOOSE.account;
	
	var leaveBtn = document.querySelector("#leave");
	leaveBtn.addEventListener("click", function(){
		Account.withdrawAccount();
	}, false);
}(this));