(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};

	/*
	 * validation action
	 */
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
	var Validator = WILDGOOSE.validation.validator;
	
	
	var Account = {
		init: function(args) {
			this.form = document.querySelector(args.form);
			this.extract(args.types);
			this.addValidationEvent();
			
			this.selected = {};
			this.submit = null;
		},
		extract: function(types) {
			for (var i = this.form.length - 1; i >= 0; --i) {
				var input = this.form[i];
				if (input.type == "button" || input.type == "submit") {
					this.submit = input;
					continue;
				}
				if (types !== undefined && types.indexOf(input.type) != -1) {
					this.selected[input.type] = input;
				}
			}
		},
			
		addValidationEvent: function(types) {
			for (var type in this.selected) {
				var el = this.selected.type;
				el.addEventListener("blur", this.checkValidation.bind(this), false);
			}
		},
		
		checkValidation: function(evt) {
			var inputEl = evt.target;
			if (Validator.check(inputEl)) {
				console.log("validation ok");
			} else {
				console.log("validation no");
			}
			
			// 각 input의 className을 확인하여 sumbit 버튼 활성화
			this.checkStatusOfSubmit.call(this);
		},
		
		
		/*
		 * form에 입력된 내용이 valid한지를 확인하여 회원가입 버튼 활성화 / 비활성화
		*/
		checkStatusOfSubmit: function() {
			var flag = true;
			
			for (var type in this.selected) {
				if (!Dom.hasClass(this.selected.type, "status-approved")) {
					flag = false;
					break;
				}
			}

			Dom[flag?"removeClass":"addClass"](this.selected.submit, "disable");
		}
	};

	
	
	/*
	 * 모두 작성된 정보를 Ajax POST로 서버에 전달
	 */
	
	
	function withdrawAccount(popup){
		var user_email = document.getElementById("userId").innerText;
		var password = document.querySelector(".form-container input[name=password]").value;
		var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
		var hashedPassword = SHA256(password);
		var finalPassword = SHA256(hashedPassword+randomNumber);
		var url = "/api/v1/accounts";
		var payload = "email="+user_email+"&password="+finalPassword+"&check=withdraw";

		Ajax.POST({"url": url, "callback":function(response) {
			if (JSON.parse(response).status == 200) {
				popup.afterclose.add(function() {location.reload();});
				popup.close();
			}
		}, "data":payload});
		
		
//		Ajax.DELETE({
//			"url":'/api/v1/accounts?email=' + user_email,
//			"callback":function() {location.href="/";}
//		});
	}
	
	function changePassword(popup){
		var user_email = document.getElementById("userId").innerText;
		var old_pw = document.querySelector(".form-container input[name=old-pw]").value;
		var new_pw = document.querySelector(".form-container input[name=password]").value;
		var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
		var hashedPassword = SHA256(old_pw);
		var finalPassword = SHA256(hashedPassword+randomNumber);
		console.log(randomNumber);
		console.log(hashedPassword);
		console.log(finalPassword);
		var newHashedPassword = SHA256(new_pw);
		var url = "/api/v1/accounts";
		var payload = "email="+user_email+"&old_pw="+finalPassword+"&new_pw="+newHashedPassword;

		Ajax.PUT({
			"url": url,
			"callback": function(response) {
				if (JSON.parse(response).status == 200) {
					popup.afterclose.add(function() {location.reload();});
					popup.close();
				}
			},
			"data":payload
		});
	}
	WILDGOOSE.account = Account;
//	WILDGOOSE.account = {
//		checkFormStatus: checkFormStatus,
//		checkSignUpForm: checkSignUpForm,
//		addValidationEvent: addValidationEvent,
//		withdrawAccount: withdrawAccount,
//		changePassword: changePassword
//	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));