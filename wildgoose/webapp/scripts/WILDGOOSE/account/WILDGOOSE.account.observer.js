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
		
		this.targetEl = null;
		this.keyCode = null;
		this._bindedKeyEventHandler = this._keyEventHandler.bind(this);
		this.statusObj = {};
		
		this._addKeyEvent();
	};
	
	AccountObserver.prototype = new Observer();
	AccountObserver.prototype.constructor = AccountObserver;
	AccountObserver.prototype.deactivate = function() {
		Observer.prototype.deactivate.call(this);
		this._removeKeyEvent();
	};
	AccountObserver.prototype._observe = function() {
		// keydown된 el의 validation작업 수행
		if (this.targetEl !== null) {
			this.validator.check(this.targetEl);
			this.targetEl = null;
		}
		
		// 관찰대상인 targetElObj가 validation작업을 수행하지 않았거나, 적절한 내용이 아닐 경우 flag는 false
		for (var name in this.targetElObj) {
			var el = this.targetElObj[name];
			this.statusObj[name] = Dom.hasClass(el, "is-valid");
		}
		
		var flag = true;
		var detailObj = {};
		detailObj.flag = true;
		
		for (var name in this.statusObj) {
			if (this.statusObj[name] == false) {
				detailObj.flag = false;
				break;
			}
		}
		detailObj.keyCode = this.keyCode;
		return detailObj;
	};
	AccountObserver.prototype._keyEventHandler = function(evt) {
		debugger;
//		this.targetEl = evt.target;
		this.keyCode = evt.keyCode;
		this.activate();
	};
	AccountObserver.prototype._addKeyEvent = function() {
		for (var name in this.targetElObj) {
			var el = this.targetElObj[name];
			el.addEventListener("keyup", this._bindedKeyEventHandler, false);
		}
	};
	AccountObserver.prototype._removeKeyEvent = function() {
		for (var name in this.targetElObj) {
			var el = this.targetElObj[name];
			el.removeEventListener("keyup", this._bindedKeyEventHandler, false);
		}
	};

	
//	AccountObserver.prototype._trigger = function(flag) {
//		var observeEvt = new CustomEvent("observe", { "detail": { "flag": flag, "keycode": this.keyCode } });
//		this.observerEl.dispatchEvent(observeEvt);
//		this.keyCode = null;
//	},
	

	WILDGOOSE.account.observer = AccountObserver;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));