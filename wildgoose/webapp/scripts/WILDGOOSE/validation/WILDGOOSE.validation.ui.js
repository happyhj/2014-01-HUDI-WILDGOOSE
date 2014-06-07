(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.validation = WILDGOOSE.validation || {};
	WILDGOOSE.validation.ui = WILDGOOSE.validation.ui || {};

	var Dom = CAGE.util.dom;
	
	var ValidationUI = {
		changeUI: function(condition, inputEl, alertMsg) {
			if (!condition) {
				this._warn(inputEl, alertMsg);
				this._invalidStyle(inputEl);
			}
			else {
				this._unwarn(inputEl);
				this._validStyle(inputEl);
			}
		},
		/*
		 * 상태에 따른 변경될 style을 모음 
		 */
		_validStyle: function(inputEl) {
			Dom.removeClass(inputEl, "status-denied");
			Dom.removeClass(inputEl, "isInvalid");
			Dom.addClass(inputEl, "status-approved");
			Dom.addClass(inputEl, "isValid");
		},
		
		_invalidStyle: function(inputEl) {
			Dom.removeClass(inputEl, "status-approved");
			Dom.removeClass(inputEl, "isValid");
			Dom.addClass(inputEl, "status-denied");
			Dom.addClass(inputEl, "isInvalid");
		},

		/*
		 * 사용에게 메시지를 전달하기 위한 함수 
		 */
		_warn: function(inputEl, _warningMsg) {
			var name = inputEl.name;
			var target = document.querySelector(".form-container .msg-" + name);

			target.innerText = _warningMsg;
		},

		_unwarn: function(inputEl) {
			var name = inputEl.name;
			var target = document.querySelector(".form-container .msg-" + name);

			target.innerText = "";
		}
	};
	
	
	WILDGOOSE.validation.ui = ValidationUI;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
