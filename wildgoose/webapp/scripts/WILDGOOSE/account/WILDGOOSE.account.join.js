(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};
	WILDGOOSE.account.join = WILDGOOSE.account.join || {};

	/*
	 * validation action
	 */
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
	var Validator = WILDGOOSE.validation.validator;
	
	function Join(args) {
		this.selected = {};
		this.submit = null;
		this.method = args.method;
		this.names = args.names;
		this.form = document.querySelector(args.form);
		this.url = args.url;
		
		this._extract();
		this._addValidationEvent();
	};
	
	Join.prototype = {
		constructor: "Join",
		exec: function(callback) {
			Ajax[this.method]({
				"url": this.url,
				"success": function() {
					callback();
				},
				"failure": function() {
					console.log("FAIL!");
				},
				"data": this._getPayload()
			});
		},
		_getPayload: function() {
			var email = escape(this.selected.email.value);
			var password = SHA256(escape(this.selected.password.value));
			var payload = "email=" + email + "&password=" + password;
			return payload;
		},
		
		_extract: function() {
			for (var i = this.form.length - 1; i >= 0; --i) {
				var el = this.form[i];
				if (el.name == "submit") {
					this.submit = el;
					continue;
				}
				if (this.names !== undefined && this.names.indexOf(el.name) != -1) {
					this.selected[el.name] = el;
				}
			}
		},
			
		_addValidationEvent: function() {
			for (var name in this.selected) {
				var el = this.selected[name];
				el.addEventListener("blur", this._checkValidation.bind(this), false);
			}
		},
		
		_checkValidation: function(evt) {
			var inputEl = evt.target;
			if (Validator.check(inputEl)) {
				console.log("validation ok");
			}
			
			// 각 input의 className을 확인하여 sumbit 버튼 활성화
			this._checkStatusOfSubmit.call(this);
		},
		
		// form에 입력된 내용이 valid한지를 확인하여 회원가입 버튼 활성화 / 비활성화
		_checkStatusOfSubmit: function() {
			var flag = true;
			for (var name in this.selected) {
				if (!Dom.hasClass(this.selected[name], "status-approved")) {
					flag = false;
					break;
				}
			}
			Dom[flag?"removeClass":"addClass"](this.submit, "disable");
		}
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