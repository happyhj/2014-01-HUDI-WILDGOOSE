(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.validation = WILDGOOSE.ui.validation || {};

	var Ajax = CAGE.ajax
	var Dom = CAGE.util.dom;
//	var Account = WILDGOOSE.account;
	
	var validation_logics = {
		email : {
			sequence : [ "required", "format", "usable" ],
			required : [ /.+/, "email을 입력해주세요" ],
			format : [ /^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/, "email형식을 지켜주세요" ],
			usable : [ function(inputEl, callback) {
				existInServer(inputEl, callback);
			}, "이미 등록된 email입니다" ]
		},
		password : {
			sequence : [ "required", "letter", "size", "ampleNumber", "ampleLetter" ],
			required : [ /.+/, "비밀번호를 입력해주세요" ],
			letter : [
					/[a-zA-Z0-9\`\~\!\@\#\$\%\^\&|*\(\)\-\_\=\=\+\\\|\,\.\<\>\/\?\[\]\{\}\;\:\'\"]/,
					"숫자, 영문자 대소문자, 특수문자만 사용해주세요" ],
			size : [ /^.{8,15}$/, "8~15자 사이로 입력해주세요" ],
			ampleNumber : [ /(.*\d{1}.*){4,}/, "숫자는 4자리 이상 포함되어야 합니다" ],
			ampleLetter : [ /(.*\D{1}.*){4,}/, "문자는 4자리 이상 포함되어야 합니다" ]
		},
		confirm : {
			sequence : [ "required", "equal" ],
			required : [ /.+/, "다시 입력해주세요" ],
			equal : [ function(inputEl, callback) {
				ckeckEquality(inputEl, callback);
			}, "다시 확인해주세요" ]
		}		
	};
	
//	var Validation = {
//		init: function(args) {
//			this.types = args;
//			Account.addValidationEvent(args);
//			
//		}
//	}
//	

	function ckeckEquality(inputEl, callback) {
		var parent = inputEl.parentNode;
		var password = document.querySelector("." + parent.className
				+ " input[name=password]");
		
		callback(inputEl.value == password.value);
	}

	function existInServer(inputEl, callback) {
		var url = "api/v1/accounts/?email=" + inputEl.value;
		Ajax.GET({"url":url, "callback":function(response) {
			console.log(response);
			var validity = (JSON.parse(response).status===200)?true:false;
			var isAjax = true;
			callback(validity, isAjax);
		}});
		Dom.addClass(inputEl, "isProgressing");
	}
	
	
	function validCheck(inputEl) {
		var fieldName = inputEl.name;
		var fieldValue = inputEl.value;
		var checking_sequence = validation_logics[fieldName]["sequence"];

		for ( var i = 0; i<checking_sequence.length; ++i) {
			var cur_sequence = checking_sequence[i];
			console.log(cur_sequence);
			var checking_logic = validation_logics[fieldName][cur_sequence];
			var alert_message = checking_logic[1];
			
			if (checking_logic[0] instanceof RegExp) {
				console.log("RegExp");
				console.log(checking_logic[0].test(fieldValue));
				if (!checking_logic[0].test(fieldValue)) {
					warn(inputEl, alert_message);
					invalidStyle(inputEl);
					return false;
				}
			} else if (checking_logic[0] instanceof Function) {
				console.log("Function");
				var valid_state = true; 
				checking_logic[0](inputEl, function(validity, isAjax) {
					if (isAjax) {
						Dom.removeClass(inputEl, "isProgressing");
					}
					if (!validity) {
						warn(inputEl, alert_message);
						invalidStyle(inputEl);
						valid_state = false;
						return false;
					}
				});
				if (!valid_state) {
					return false;
				}
			}
		}
		unwarn(inputEl);
		validStyle(inputEl);
		return true;
	}
	
	
	/*
	 * 상태에 따른 변경될 style을 모음 
	 */
	function validStyle(inputEl) {
		Dom.removeClass(inputEl, "status-denied");
		Dom.removeClass(inputEl, "isInvalid");
		Dom.addClass(inputEl, "status-approved");
		Dom.addClass(inputEl, "isValid");
	}
	
	function invalidStyle(inputEl) {
		Dom.removeClass(inputEl, "status-approved");
		Dom.removeClass(inputEl, "isValid");
		Dom.addClass(inputEl, "status-denied");
		Dom.addClass(inputEl, "isInvalid");
	}

	/*
	 * 사용에게 메시지를 전달하기 위한 함수 
	 */
	function warn(inputEl, warningMsg) {
		var name = inputEl.name;
		var target = document.querySelector(".form-container .msg-" + name);

		target.innerText = warningMsg;
	}

	function unwarn(inputEl) {
		var name = inputEl.name;
		var target = document.querySelector(".form-container .msg-" + name);

		target.innerText = "";
	}
	
	WILDGOOSE.ui.validation = {
		validCheck: validCheck
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
