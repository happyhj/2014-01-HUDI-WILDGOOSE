(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.validation = WILDGOOSE.validation || {};
	WILDGOOSE.validation.logics = WILDGOOSE.validation.logics || {};
	
	var Ajax = CAGE.ajax;
	
	var ValidationLogics = {
		email : {
			sequence : [ "required", "format", "usable" ],
			required : [ /.+/, "email을 입력해주세요" ],
			format : [ /^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/, "email형식을 지켜주세요" ],
			usable : [ function(inputEl, callback) {
				this.email._existInServer(inputEl, callback);
			}, "이미 등록된 email입니다" ],
			_existInServer: function(inputEl, callback) {
				var url = "/api/v1/accounts?email=" + inputEl.value;
				Ajax.GET({isAsync:false, url:url, callback:function(response) {
					var validity = (JSON.parse(response).status===200)?true:false;
					var isProgressing = true;
					callback(validity, isProgressing);
				}});
//				Dom.addClass(inputEl, "isProgressing");
			}
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
				this.confirm._ckeckEquality(inputEl, callback);
			}, "다시 확인해주세요" ],
			_ckeckEquality: function(inputEl, callback) {
				var parent = inputEl.form;
				var password = document.querySelector("." + parent.className
						+ " input[name=password]");
				callback(inputEl.value == password.value);
			}

		}
	};
	
	
	WILDGOOSE.validation.logics = ValidationLogics;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
