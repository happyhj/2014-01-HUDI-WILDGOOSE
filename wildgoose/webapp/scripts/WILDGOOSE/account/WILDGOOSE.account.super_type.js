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
	var Ajax = CAGE.ajax;
	var Validator = WILDGOOSE.validator;
	
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
				}
			}
		},
		_getPayload: function() {
			/*
			 * interface
			 * subType에서 _getPayload를 구현해야함.
			 */ 
			return null;
		},
		
		stop: function() {
			this._removeKeyEvent();
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
			var enter = (evt.keyCode == 13)? true : false;
			var targetEl = evt.target;
			this.validator.check(targetEl);
			this._updateSubmit();
			
			if (enter) {
				var clickEvt = new CustomEvent("click", {detail: {"enter": enter}});
				this.submitEl.dispatchEvent(clickEvt);
			}
		},
		
		_updateSubmit: function(enter) {
			var flag = true;
			for(var name in this.selectedEl) {
				var el = this.selectedEl[name].parentNode.parentNode.parentNode;
				if (!Dom.hasClass(el, "is-valid") || Dom.hasClass(el, "is-invalid")) {
					flag = false;
					break;
				}
			}
			
			if (flag) {
				Dom.removeClass(this.submitEl, "disable");
				Dom.addClass(this.submitEl, "able");
			}
			else {
				Dom.removeClass(this.submitEl, "able");
				Dom.addClass(this.submitEl, "disable");
			}
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
		}		
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

