(function(window) {
	'use strict';
    var document = window.document,
        console = window.console,
    	WILDGOOSE = window.WILDGOOSE || {},
    	WILDGOOSE.ui.validation = WILDGOOSE.ui.validation || {};
	
	/*
	 * var validate = WILDGOOSE.ui.validation.validate
	 * validate()
	 */
	var warnings = {
		"email" : {
			"required" : "email을 입력해주세요",
			"format" : "email형식을 지켜주세요",
			"usable" : "이미 등록된 email입니다"
		},
		"password" : {
			"required" : "비밀번호를 입력해주세요",
			"letter" : "숫자, 영문자 대소문자, 특수문자만 사용해주세요",
			"size" : "8~15자 사이로 입력해주세요",
			"ampleNumber" : "숫자는 4자리 이상 포함되어야 합니다",
			"ampleLetter" : "문자는 4자리 이상 포함되어야 합니다"
		},
		"confirm" : {
			"required" : "다시 입력해주세요",
			"equal" : "다시 확인해주세요"
		}
	}

	var validFunctions = {
		"email" : [fieldIsFilled, emailIsProper],
		"password" : [fieldIsFilled, numberIsAmple, letterIsProper, letterIsAmple, sizeIsProper],
		"confirm" : [fieldIsFilled, ckeckEquality]
	}

	function validCheck(field) {
		var fieldName = field.name
		var checklist = validFunctions[fieldName]
		for (var i = 0; i < checklist.length; i++) {
			if (checklist[i](field) == false) {
				return false;
			}
		}
		return true
	}

	function existInServer (me) {
		var url = "api/v1/sign/email/" + me.value;
		Ajax.GET(url, showEmailStatus);
		Util.addClass(me, "isProgressing");
	}

	function fieldIsFilled(me) {
		if (me.value != "") {
			unwarn(me, "required");
			return true;
		}
		warn(me, "required");
		return false;	
	}

	function emailIsProper(me) {
		// account@domain.***
		var pattern = /^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/
		if (pattern.test(me.value)) {
			unwarn(me, "format");
			return true;
		}
		warn(me, "format");
		return false;
	}

	function sizeIsProper(me) {
		// 8~15자 입력
		var pattern = /^.{8,15}$/;
		if (pattern.test(me.value)) {
			unwarn(me, "size");
			return true;
		}
		warn(me, "size");
		return false;
	}

	function letterIsProper(me) {
		var pattern = /[a-zA-Z0-9\`\~\!\@\#\$\%\^\&|*\(\)\-\_\=\=\+\\\|\,\.\<\>\/\?\[\]\{\}\;\:\'\"]/;
		if (pattern.test(me.value)) {
			unwarn(me, "letter");
			return true;
		}
		warn(me, "letter");
		return false;
	}

	function numberIsAmple(me) {
		// 숫자 4자리 이상 포함
		var pattern = /(.*\d{1}.*){4,}/;
		if (pattern.test(me.value)) {
			unwarn(me, "ampleNumber");
			return true;
		}
		warn(me, "ampleNumber");
		return false;
	}

	function letterIsAmple(me) {
		// 숫자가 아닌 수 4자리 이상 포함
		var pattern = /(.*\D{1}.*){4,}/;
		if (pattern.test(me.value)) {
			unwarn(me, "ampleLetter");
			return true;
		}
		warn(me, "ampleLetter");
		return false;
	}

	function ckeckEquality(me) {
		var parent = me.parentNode;
		var password = document.querySelector("." + parent.className + " input[name=password]");
		if (me.value == password.value) {
			unwarn(me, "equal");
			return true;
		}
		warn(me, "equal");
		return false;
	}

	/*
	 * response: OK or 공백
	 * OK: 이메일 사용가능
	 *   : 이메일 사용불가
	 */
	function showEmailStatus(response) {	
		var email = document.querySelector(".form-container > input[name=email]");
		
		/*
		 * 이메일 사용여부를 확인한 결과값을
		 * acount.js의 checkSignUpFrom에 전달하기 위해서
		 * 사용자 정의 이벤트를 blur형식으로 만들고
		 * 이벤트 속에 valid라는 이름의 property를 만듦
		 */  
		var blurEvent = document.createEvent("Event");
		blurEvent.initEvent("blur", false, false);
		
		// when response is OK
		if (response) {
			unwarn(email, "usable");
			blurEvent.valid = true;
		}
		else {
			warn(email, "usable");
			blurEvent.valid = false;		
		}
		
		// progressing 상태 해제
		Util.removeClass(email, "isProgressing");
		
		// email에 blurEvent 발생시킴
		email.dispatchEvent(blurEvent);
	}

	/*
	 * 사용에게 메시지를 전달하기 위한 함수 
	 */
	function warn(field, warningType) {
		var name = field.name;
		var target = document.querySelector(".form-container .msg-" + name);
		var warning = warnings[name][warningType];
		
		target.innerText = warning; 
	}

	function unwarn(field, warningType) {
		var name = field.name;
		var target = document.querySelector(".form-container .msg-" + name);
		
		target.innerText = "";
	}

}(this));
