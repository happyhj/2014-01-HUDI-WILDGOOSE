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
	var Validation = WILDGOOSE.validation.info;
	
	var selectedDoms = [];
	var button = null;
	
	function addValidationEvent(args) {
		var formContainer = document.querySelector(".form-container");
		for (var i = formContainer.length - 1; i >= 0; --i) {
			var input = formContainer[i];
			if (input.type == "button" || input.type == "submit") {
				button = input;
				continue;
			}
//			debugger;
			if (args !== undefined && args.indexOf(input.type) != -1) {
				selectedDoms.push(input);
				input.addEventListener("blur", checkSignUpForm, false);
			}
		}
	};
	
	function checkSignUpForm(e) {
		var inputEl = e.target;
		if (Validation.check(inputEl)) {
			console.log("validation ok");
		} else {
			console.log("validation no");
		}
		
		// 각 input의 className을 확인하여 sumbit 버튼 활성화
		checkFormStatus(inputEl.parentNode);
	};
	
	
	/*
	 * form에 입력된 내용이 valid한지를 확인하여 회원가입 버튼 활성화 / 비활성화
	*/
	function checkFormStatus() {
//		var btnIndex = form.length-1;
		var flag = true;
		
		for (var i=0; i<selectedDoms.length; i++) {
			if (!Dom.hasClass(selectedDoms[i], "status-approved")) {
				flag = false;
				break;
			}
		}
		
//		
//		for (var i=btn-1; i>=0; --i) {
//			if (!Dom.hasClass(form[i], "status-approved")) {
//				flag = false;
//				break;
//			}
//		}
		Dom[flag?"removeClass":"addClass"](button, "disable");
		
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
	
	WILDGOOSE.account = {
		checkFormStatus: checkFormStatus,
		checkSignUpForm: checkSignUpForm,
		addValidationEvent: addValidationEvent,
		withdrawAccount: withdrawAccount,
		changePassword: changePassword
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));