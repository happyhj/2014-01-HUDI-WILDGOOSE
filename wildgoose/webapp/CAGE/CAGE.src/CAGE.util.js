(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var CAGE = window.CAGE || {};
	var isClassListExist = false
	CAGE.util = CAGE.util || {};

	if('classList' in document.createElement('a')) {
		isClassListExist = true;
	}
	
	var hasClass_common = function(DOM, className) {
		// DOM에 클래스 존재여부 확인
		var pattern = new RegExp("^.*" + className + ".*$");
		if (pattern.test(DOM.className)) {
			return true;
		}
		return false;
	}
	
	var addClass_common = function(DOM, className) {
		// DOM에 클래스 존재여부 확인
		if (this.hasClass(DOM, className)) return;
		
		// DOM에 class가 없는 경우
		if (DOM.className == "") {
			DOM.className = className;
			return;
		}
		
		// DOM에 class가 있는 경우
		DOM.className += " " + className;
	}
	
	var removeClass_common = function (DOM, className) {
		// DOM에 클래스 존재여부 확인
		if (!this.hasClass(DOM, className)) return;
		
		// DOM에 class가 한개만 존재시
		if (DOM.className == className) {
			DOM.className = "";
			return;
		}
		
		// DOM에 class가 두개 이상 존재시
		if(this.hasClass(DOM," "+className)) {
			DOM.className = DOM.className.replace(" " + className, "");
		} else {
			DOM.className = DOM.className.replace(className + " ", "");
		}
	}	
	
	var Util = {
		hasClass : function(DOM, className) {
			// DOM에 클래스 존재여부 확인
			var pattern = new RegExp("^.*" + className + ".*$");
			if (pattern.test(DOM.className)) {
				return true;
			}
			return false;
		},
		
		addClass : function(DOM, className) {
			// DOM에 클래스 존재여부 확인
			if (this.hasClass(DOM, className)) return;
			
			// DOM에 class가 없는 경우
			if (DOM.className == "") {
				DOM.className = className;
				return;
			}
			
			// DOM에 class가 있는 경우
			DOM.className += " " + className;
		},
		removeClass : function (DOM, className) {
			// DOM에 클래스 존재여부 확인
			if (!this.hasClass(DOM, className)) return;
			
			// DOM에 class가 한개만 존재시
			if (DOM.className == className) {
				DOM.className = "";
				return;
			}
			
			// DOM에 class가 두개 이상 존재시
			var pattern = new RegExp("( " + className + "|" + className + " )");
			DOM.className = DOM.className.replace(pattern,"");
		},	
		trim: function (str) {
			return this.ltrim(this.rtrim(str));
		},
		
		rtrim: function (str) {
			str.replace(/\s*$/, "");
			return str;
		},
		
		ltrim: function (str) {
			str.replace(/^\s*/, "");
			return str;
		},
		getTemplateCompiler: function(templateStr) {
		    return function(dataObj) {
		        var resultStr = Util.trim(templateStr);
		        for (var variableName in dataObj)
		        {
		            if (dataObj[variableName]===0||dataObj[variableName]) {
		                resultStr = resultStr.replace("%= "+variableName+" %", dataObj[variableName]);
		            }
		        }
		        return resultStr;
		    };
		} 
	};
	
	CAGE.util = {
		hasClass: Util.hasClass,
		addClass: Util.addClass,
		removeClass: Util.removeClass,
		trim: Util.trim,
		getTemplateCompiler: Util.getTemplateCompiler
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));



