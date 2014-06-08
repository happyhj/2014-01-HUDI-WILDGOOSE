(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.validation = WILDGOOSE.validation || {};
	
	var ValidationUI = WILDGOOSE.validation.ui;
//	var ValidationLogics = WILDGOOSE.validation.logics;
	
	var Dom = CAGE.util.dom;
	var Ajax = CAGE.ajax;
	
	function Validator(rule) {
		this.rule = rule;
		this.myLogics = {};
		
		this.defineLogics();
//		defineSequence();
	};
	
	Validator.prototype = {
		constructor: "Validator",
		defineLogics: function() {
			for (var name in this.rule) {
				var curRule = this.rule[name];
				var curType = curRule.type; 
				this.myLogics[name] = this.normLogics[curType];

				if (curType == "confirm") {
					this.myLogics[name].target = this.rule[name].target;						
				}
			}
		},
		
		
		check: function(inputEl) {
			var fieldName = inputEl.name;
			var logics = this.myLogics[fieldName];
			var sequence = logics.sequence;
			var state = true;
			
			for ( var i = 0; i<sequence.length; ++i) {
				var curSeq = sequence[i];
				var logic = logics[curSeq][0];
				var alertMsg = logics[curSeq][1];

				if (!this._isValid(logic, inputEl)) {
					state = false;
					break;
				}
			}
			
			debugger;
			ValidationUI.changeUI(state, inputEl, alertMsg);
			return state;
		},
		
		_isValid: function(logic, inputEl) {
			var name = inputEl.name;
			var value = inputEl.value;
			
			if (logic instanceof RegExp) {
				return logic.test(value);
			}
			else if (logic instanceof Function) {
				var validState = false;
				logic.call(this.myLogics[name], inputEl, function(validity, isProgressing) {
					if (isProgressing) {
						Dom.removeClass(inputEl, "isProgressing");
					}
					validState = validity;
				});
				return validState;
			}
			return false;
		},
		
		
		
		normLogics: {
				email : {
					sequence : [ "required", "format", "usable" ],
					required : [ /.+/, "email을 입력해주세요" ],
					format : [ /^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/, "email형식을 지켜주세요" ],
					usable : [ function(inputEl, callback) {
						this._existInServer(inputEl, callback);
					}, "이미 등록된 email입니다" ],
					_existInServer: function(inputEl, callback) {
						var url = "/api/v1/accounts?email=" + inputEl.value;
						Ajax.GET({isAsync:false, url:url, callback:function(response) {
							var validity = (JSON.parse(response).status===200)?true:false;
							var isProgressing = true;
							callback(validity, isProgressing);
						}});
//						Dom.addClass(inputEl, "isProgressing");
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
						this._ckeckEquality(inputEl, callback);
					}, "다시 확인해주세요" ],
					_ckeckEquality: function(inputEl, callback) {
						var parent = inputEl.form;
						var targetEl = document.querySelector("." + parent.className
								+ " input[name=" + this.target + "]");
						callback(inputEl.value == targetEl.value);
					}

				}
			}
	};
	
	WILDGOOSE.validation.validator = Validator;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}	

}(this));
