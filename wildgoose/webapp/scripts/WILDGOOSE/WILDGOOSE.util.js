(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.util = WILDGOOSE.util || {};

	var Util = {
		dom: {
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
				if(this.hasClass(DOM," "+className)) {
					DOM.className = DOM.className.replace(" " + className, "");
				} else {
					DOM.className = DOM.className.replace(className + " ", "");
				}
			}	
		},
		string: {
			trim: function (str) {
				return this.ltrim(this.rtrim(str));
			},
			
			rtrim: function (str) {
				return str.replace(/\s*$/, "");
			},
			
			ltrim: function (str) {
				return str.replace(/^\s*/, "");
			},
		},
		template: {
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
		}	
	};
	
	WILDGOOSE.util = Util;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
