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
	var Validation = WILDGOOSE.ui.validation;
	
	function addValidationEvent() {
		var formContainer = document.querySelector(".form-container");
		for (var i = formContainer.length - 1; i >= 0; --i) {
			var input = formContainer[i];
			if (input.type == "email" || input.type == "password") {
				// blur event
	//			var dataCheck = input.getAttribute("data-check");
		//		if(dataCheck == "true") {
					input.addEventListener("blur", checkSignUpFrom, false);
			//	}
			}
		}
	};
	
	function checkSignUpFrom(e) {
		var inputEl = e.target;
		if (Validation.validCheck(inputEl)) {
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
	function checkFormStatus(form) {
		var btn = form.length-1;
		var flag = true;
		for (var i=btn-1; i>=0; --i) {
			if (!Dom.hasClass(form[i], "status-approved")) {
				flag = false;
				break;
			}
		}
	
		flag ? Dom.removeClass(form[btn], "hidden") : Dom.addClass(form[btn], "hidden");
		
	};
	
	/*
	 * 모두 작성된 정보를 Ajax POST로 서버에 전달
	 */
	function signUpAccount() {
		var url = "/api/v1/accounts/";
		var form = document.querySelector(".form-container");
		
		var email = escape(form[0].value)
		var password = escape(form[1].value);
		var payload = "email=" + email + "&password=" + SHA256(password);
		Ajax.POST({"url":url, "callback":showSignUpResult, "data":payload});
	//	domUtil.addClass(form, "isProgressing");
	
	};
	
	/*
	 * signUpAccount 실행 후
	 * 서버에서 전달된 결과값 확인
	 */
	function showSignUpResult(response) {
		var form = document.querySelector(".form-container");
		Dom.removeClass(form, "isProgressing");
		
		if (response == "success") {
			// close modal. and update login panel
			WILDGOOSE.ui.modal.closeModal(function(){
				updateTopbar(true);
			});
		}
	};
	
	
	function loginAccount(popup) {
		var email = document.querySelector(".form-container input[name=email]").value;
		var password = document.querySelector(".form-container input[name=password]").value;
		var hashedPassword = SHA256(password);	
		var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
		var finalPassword = SHA256(hashedPassword+randomNumber);
		var url = "/api/v1/session/";
		var payload = "email="+email+"&password="+finalPassword;
		Ajax.POST({"url": url, "callback":function(response) {
			var form = document.querySelector(".form-container");
			Dom.removeClass(form, "isProgressing");
			console.log(response);
			console.log(JSON.parse(response).status);
			if (JSON.parse(response).status == 200) {
				popup.afterclose.add(function() {location.reload();});
				popup.close();
			}
		}, "data":payload});
	};
	
	function withdrawAccount(){
		var user_email = document.getElementById("userId").innerText;
		Ajax.DELETE({
			"url":'/api/v1/accounts?email=' + user_email,
			"callback":function() {location.href="/";},
		});
	}
	
	WILDGOOSE.account = {
		loginAccount: loginAccount,
		signUpAccount: signUpAccount,
		showSignUpResult: showSignUpResult,
		checkFormStatus: checkFormStatus,
		checkSignUpFrom: checkSignUpFrom,
		addValidationEvent: addValidationEvent,
		withdrawAccount: withdrawAccount
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
