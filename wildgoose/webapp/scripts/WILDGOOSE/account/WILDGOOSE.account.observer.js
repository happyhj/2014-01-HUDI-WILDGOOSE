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
		this.validator = args.validator;
		this.keypressedEl = null;
		this.evtHandler = this.eventHandler.bind(this);
		
		this.addKeypressEvent();
	};
	
	AccountObserver.prototype = new Observer();
	AccountObserver.prototype.constructor = AccountObserver;
	AccountObserver.prototype.observe = function() {
		var flag = true;
		if (this.keypressedEl !== null) {
			if (!this.validator.check(this.keypressedEl)) {
				flag = false;
			}
			this.keypressedEl = null;
		}
		return flag;
	};
	AccountObserver.prototype.deactivate = function() {
		Observer.prototype.deactivate.call(this);
		this.removeKeypressEvent();
	};
	
	AccountObserver.prototype.eventHandler = function(evt) {
		var targetEl = evt.target;
		this.keypressedEl = targetEl;
	};
	AccountObserver.prototype.addKeypressEvent = function() {
		for (var name in this.targetElObj) {
			var el = this.targetElObj[name];
			el.addEventListener("keypress", this.evtHandler, false);
		}
	};
	AccountObserver.prototype.removeKeypressEvent = function() {
		for (var name in this.targetElObj) {
			var el = this.targetElObj[name];
			el.removeEventListener("keypress", this.evtHandler, false);
		}
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