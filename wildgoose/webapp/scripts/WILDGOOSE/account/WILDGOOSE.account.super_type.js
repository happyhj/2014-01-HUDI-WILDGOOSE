(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};
	WILDGOOSE.account.super_type = WILDGOOSE.account.super_type || {};

	/*
	 * validation action
	 */
	var Dom = CAGE.util.dom;
//	var Observer = CAGE.type.observer;
	var Ajax = CAGE.ajax;
	var Validator = WILDGOOSE.validator;
//	var AccountObserver = WILDGOOSE.account.observer;
	
	function Account(args) {
		this.selectedEl = {};
		this.submitEl = null;
		this.randNum = null;
		this.method = null;
		this.rule = null;
		this.names = null;
		this.form = null;
		this.url = null;
		
		this._account(args);
	};

	Account.prototype = {
		constructor: "Account",
		_account: function(args) {
			if (args !== undefined) {
				this.method = args.method;
				this.rule = args.rule;
				this.form = document.querySelector(args.form);
				this.url = args.url;
				this.randNum = args.randNum;
				
				if (this.rule !== undefined) {
					this.names = Object.keys(this.rule);
					this._extract();
					this.validator = new Validator(this.form, this.rule);
					this._addKeyEvent();
					
					// account submit버튼을 serInterval을 이용하여 계속적 탐지
//					this._setObserver();
//					this._addKeyEvent();
//					this._addObserveEvent();
					
//					this._activateObserver();
				}
			}
		},
		stop: function() {
			this._removeKeyEvent();
//			this._removeEnterEvent();
		},
		
		exec: function(callback) {
			if (this.rule !== undefined && Dom.hasClass(this.submitEl, "disable")) {
				console.log("누르지마 바보야");
			}
			else {
				Ajax[this.method]({
					"url": this.url,
					"success": function() {
						callback();
						console.log("Success!");
					},
					"failure": function() {
						console.log("FAIL!");
					},
					"data": this._getPayload()
				});
			}
		},
		
		_addKeyEvent: function() {
			for (var name in this.selectedEl) {
				var el = this.selectedEl[name];
				el.addEventListener("keyup", this._validateField.bind(this), false);
			}
		},
		
		_removeKeyEvent: function() {
			for (var name in this.selectedEl) {
				var el = this.selectedEl[name];
				el.removeEventListener("keyup", this._validateField.bind(this), false);
			}
		},
		
		_validateField: function(evt) {
//			debugger;
			var targetEl = evt.target;
			this.validator.check(targetEl);			
		},
		
//		_deactivateObserver: function() {
//		this.observer.deactivate();
//		this.submitEl.removeEventListener("observe", this._observeEvtHandler, false);
//	},
		
//		_setObserver: function() {
//			var obArgs = {
//				targetElObj: this.selectedEl.valueOf(),
//				observerEl: this.submitEl,
////				interval: 100,
//				validator: new Validator(this.form, this.rule)
//			};
//			this.observer = new AccountObserver(obArgs);
//		},
		
//		_addObserveEvent: function() {
//			this.submitEl.addEventListener("observe", this._observeEvtHandler, false);
//		},
//		_activateObserver: function() {
//			this.submitEl.addEventListener("observe", this._observeEvtHandler, false);
//			this.observer.activate();
//		},
//		_observeEvtHandler: function(evt) {
//			console.log("flag: " + evt.detail.flag);
//			Dom[evt.detail.flag?"removeClass":"addClass"](this, "disable");
//			if (evt.detail.flag && evt.detail.keycode == 13) {
//				debugger;
//				var clickEvt = new CustomEvent("click");
//				this.dispatchEvent(clickEvt);
//			}
//		},
		
		_getPayload: function() {
			/*
			 * interface
			 * subType에서 _getPayload를 구현해야함.
			 */ 
			return null;
		},
		
		_extract: function() {
			for (var i = this.form.length - 1; i >= 0; --i) {
				var el = this.form[i];
				if (el.name == "submit") {
					this.submitEl = el;
					continue;
				}
				
				if (this.names !== undefined && this.names.indexOf(el.name) != -1) {
					this.selectedEl[el.name] = el;
				}
			}
		},
		
//		_keyEventHandler: function(evt) {
//			this.targetEl = evt.target;
//			this.keyCode = evt.keyCode;
//		},
//		_addKeyEvent: function() {
//			for (var name in this.targetElObj) {
//				var el = this.targetElObj[name];
//				el.addEventListener("keydown", this._bindedKeyEventHandler, false);
//			}
//		},
//		_removeKeyEvent: function() {
//			for (var name in this.targetElObj) {
//				var el = this.targetElObj[name];
//				el.removeEventListener("keydown", this._bindedKeyEventHandler, false);
//			}
//		}
		
		
	};
	
	
	WILDGOOSE.account.super_type = Account;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}

}(this));

