var Ajax = (function(){
	return {
		createRequest : function () {
			try {
				var request = new XMLHttpRequest();
			} catch (tryMS) {
				try {
					request = new ActiveXObject("Msxml2.XMLHTTP");
				} catch (otherMS) {
					try {
						request = new ActiveXObject("Microsoft.XMLHTTP");
					} catch (failed) {
						request = null;
					}
				}
			}
			return request;
		},
		
		responseData : function(e, request, func) {
			if (request.readyState == 4) {
				if (request.status == 200) {
					
					// responseText의 마지막에 포함된 개행문자 제거
					var response = request.responseText;
					response = response.substring(0, response.length - 1);
					func(response);
				}
			}
		},
		
		GET : function (url, func, async) {
			if (async == null) {
				async = true;
			}
			
			var request = this.createRequest();
			if (request == null) {
				console.log("Unable to create request");
				return;
			}
			
			request.open("GET", url, async);
			request.addEventListener("readystatechange", function (e) {
				Ajax.responseData(e, request, func);
			}, false);
			// send
			request.send(null);
		},
		
		POST : function (url, func, payload, async) {
			if (async == null) {
				async = true;
			}
			
			var request = this.createRequest();
			if (request == null) {
				console.log("Unable to create request");
				return;
			}

			request.open("POST", url, async);
			request.addEventListener("readystatechange", function (e) {
				Ajax.responseData(e, request, func);
			}, false);
			request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			// send
			request.send(payload);
		}, 
		DELETE : function (url, func, async) {
			if (async == null) {
				async = true;
			}
			
			var request = this.createRequest();
			if (request == null) {
				console.log("Unable to create request");
				return;
			}

			request.open("DELETE", url, async);
			
			if (func != null) {
				request.addEventListener("readystatechange", function (e) {
					Ajax.responseData(e, request, func);
				}, false);
			}
			// send
			request.send(null);
		}
	}
}());

var Util = (function() {
	return {
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
			DOM.className = DOM.className.replace(" " + className, "");
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
}());
