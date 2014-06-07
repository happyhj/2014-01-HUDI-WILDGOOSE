(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};
	WILDGOOSE.account.withdraw = WILDGOOSE.account.withdraw || {};

	/*
	 * validation action
	 */
	var Dom = CAGE.util.dom;
	var Ajax = CAGE.ajax;
	var Validator = WILDGOOSE.validation.validator;
	
	var Withdraw = {
		init: function(args) {
			this.selected = {};
			this.submit = null;
			this.randomNumber = null;
			
			this.form = document.querySelector(args.form);
			this._extract(args.types);
			this._addValidationEvent();
			this.url = "/api/v1/accounts/";
		},
		
		exec: function(callback) {
			var email = escape(document.getElementById("userId").textContent);
			var password = SHA256(SHA256(escape(this.selected.password.value)) + this.randomNumber);
			var payload = "email=" + email + "&password=" + password + "&check=withdraw";
			
			Ajax.POST({
				"url": this.url,
				"success": function() {
					callback();
					console.log("Success!");
				},
				"failure": function() {
					console.log("FAIL!");
				},
				"data": payload
			});
		},
		
		_extract: function(names) {
			for (var i = this.form.length - 1; i >= 0; --i) {
				var el = this.form[i];
				if (el.name == "submit") {
					this.submit = el;
					continue;
				}
				if (el.name == "randomNumber") {
					this.randomNumber = el.value;
					continue;
				}
				
				if (names !== undefined && names.indexOf(el.type) != -1) {
					this.selected[el.name] = el;
				}
			}
		},
			
		_addValidationEvent: function(types) {
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

	
	WILDGOOSE.account.withdraw = Withdraw;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));