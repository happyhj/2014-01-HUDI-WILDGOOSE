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
		dom : {
			hasClass : (function() {
				if(isClassListExist) {
					return function(DOM, className) {
						return DOM.classList.contains(className);
					}
				} else return hasClass_common;
			})(),
			
			addClass : (function() {
				if(isClassListExist) {
					return function(DOM, className) {
						return DOM.classList.add(className);
					}
				} else return addClass_common;
			})(),
			
			removeClass : (function() {
				if(isClassListExist) {
					return function(DOM, className) {
						return DOM.classList.remove(className);
					}
				} else return addClass_common;
			})()
		},
		
		string: {
			trim: (function() {
				if('trim' in String.prototype) {
					return function(str) {
						return String.prototype.trim.call(str);
					};
				} else return function (str) {
					return this.ltrim(this.rtrim(str));
				}
			})(),
			
			rtrim: (function(str) {
				if('trimRight' in String.prototype) {
					return function(str) {
						return String.prototype.trimRight.call(str);
					};
				} else return function (str) {
					return str.replace(/\s*$/, "");
				}
			})(),
			
			ltrim: (function(str) {
				if('trimLeft' in String.prototype) {
					return function(str) {
						return String.prototype.trimLeft.call(str);
					};
				} else return function (str) {
					return str.replace(/^\s*/, "");
				}
			})()
		},
		
		template: {
			compiler: function(dataObj, templateString) {
		        var resultStr = Util.string.trim(templateString);
		        for (var variableName in dataObj) {
		            if (dataObj[variableName]===0||dataObj[variableName]) {
		                resultStr = resultStr.replace("<%= "+variableName+" %>", dataObj[variableName]);
		            }
		        }
		        return resultStr;
		    },
				
			getCompiler: function() {
			    return this.compiler;
			},
			
			// xhr, using synchronized get method
			get: function(orgs) {
				var Ajax = CAGE.ajax;
				var url = orgs.url;
				var template = null;
				Ajax.GET({"url":url, "isAsync":false, "callback":function(templateResponse) {
					template = JSON.parse(templateResponse)["data"]["template"];
				}});
				
				return template;
			}
		}
	};
	
	CAGE.util = Util;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));


