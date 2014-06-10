(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};
	WILDGOOSE.account.logout = WILDGOOSE.account.logout || {};

	// 의존성 주입.
	var Observer = CAGE.type.observer;
	var Dom = CAGE.util.dom;
	
	function AccountObserver(args) {
		Observer.call(this, args);
	};
	
	AccountObserver.prototype = new Observer();
	AccountObserver.prototype.constructor = AccountObserver;
	AccountObserver.prototype._observe = function() {
		var detailObj = {};
		detailObj.flag = (Dom.hasClass(this.targetEl, "disable"))? false : true; 
		return detailObj;
	};

	WILDGOOSE.account.observer = AccountObserver;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));