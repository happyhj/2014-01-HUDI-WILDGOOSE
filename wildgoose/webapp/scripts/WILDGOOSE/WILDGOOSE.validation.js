(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.validation = WILDGOOSE.validation || {};
	
	var ValidationUI = WILDGOOSE.validation.ui;
	var ValidationLogics = WILDGOOSE.validation.logics;
	
	var Dom = CAGE.util.dom;
	
	var Validation = {
		/*
		 * input: inputEl
		 * output: true, false
		 */
		check: function(inputEl) {
			var fieldName = inputEl.name;
			var logics = ValidationLogics[fieldName];
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
			
			ValidationUI.changeUI(state, inputEl, alertMsg);
			return state;
		},
		
		_isValid: function(logic, inputEl) {
			var value = inputEl.value;
			
			if (logic instanceof RegExp) {
				return logic.test(value);
			}
			else if (logic instanceof Function) {
				var validState = false;
				logic.call(ValidationLogics, inputEl, function(validity, isProgressing) {
					if (isProgressing) {
						Dom.removeClass(inputEl, "isProgressing");
					}
					validState = validity;
				});
				return validState
			}
			return false;
		}
	};
	
	WILDGOOSE.validation.info = Validation;
//	WILDGOOSE.validation = {
//			check: Validation.check
//	}
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
