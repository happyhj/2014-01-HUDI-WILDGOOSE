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
	
	var Join = {
		init: function(args) {
			this.selected = {};
			this.submit = null;
			this.form = document.querySelector(args.form);
			this._extract(args.types);
			this._addValidationEvent();
			this.url = "/api/v1/accounts/";
		},
		
		exec: function(callback) {
			var email = escape(this.selected.email.value);
			var password = SHA256(escape(this.selected.password.value));
			var payload = "email=" + email + "&password=" + password;
			Ajax.POST({
				"url": this.url,
				"success": function() {
					callback();
				},
				"failure": function() {
					console.log("FAIL!");
				},
				"data": payload
			});
		},
		
//		
//		function withdrawAccount(popup){
//			var user_email = document.getElementById("userId").innerText;
//			var password = document.querySelector(".form-container input[name=password]").value;
//			var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
//			var hashedPassword = SHA256(password);
//			var finalPassword = SHA256(hashedPassword+randomNumber);
//			var url = "/api/v1/accounts";
//			var payload = "email="+user_email+"&password="+finalPassword+"&check=withdraw";
//
//			Ajax.POST({"url": url, "callback":function(response) {
//				if (JSON.parse(response).status == 200) {
//					popup.afterclose.add(function() {location.reload();});
//					popup.close();
//				}
//			}, "data":payload});
//		},
//			
//		
		
		
		_extract: function(names) {
			for (var i = this.form.length - 1; i >= 0; --i) {
				var el = this.form[i];
				if (el.name == "submit") {
					this.submit = el;
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
	
	WILDGOOSE.account.join = Join;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));