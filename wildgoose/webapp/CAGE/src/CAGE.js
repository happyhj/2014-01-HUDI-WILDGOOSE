(function(window) {
	'use strict';
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var CAGE = window.CAGE || {};
	CAGE.ajax = CAGE.ajax || {};

	// 비공개 메서드 선언
	function _createRequest() {
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
	}
		
	function _responseData(e, request, callback) {
		if (request.readyState == 4) {
			if (request.status == 200) {
				// responseText의 마지막에 포함된 개행문자 제거
				var response = request.responseText;
				if(response.substring(response.length - 1) === "\n") {
					response = response.substring(0, response.length - 1);					
				}
				callback(response);
			}
		}
	}

	// 공개 메서드 선언	
	function GET(config) {
		var url = config.url,
			callback = config.callback,
			isAsync = config.isAsync;

		if (isAsync == undefined) {
			isAsync = true;
		}
		
		if (url == undefined) {
			return;
		}
				
		var request = _createRequest();
		if (request == undefined) {
			console.log("Unable to create request");
			return;
		}
		
		request.open("GET", url, isAsync);
		if (callback !== undefined) {
			request.addEventListener("readystatechange", function (e) {
				_responseData(e, request, callback);
			}, false);
		}
		// send
		request.send();
	}
		
	function POST(config) {
		var url = config.url,
			callback = config.callback,
			data = config.data,
			isAsync = config.isAsync;
		
		if (isAsync === undefined) {
			isAsync = true;
		}

		if (url == undefined) {
			return;
		}
				
		var request = _createRequest();
		if (request === undefined) {
			console.log("Unable to create request");
			return;
		}

		request.open("POST", url, isAsync);
		if (callback !== undefined) {
			request.addEventListener("readystatechange", function (e) {
				_responseData(e, request, callback);
			}, false);
		}
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		// send
		request.send(data);
	}

	function PUT(config) {
		var url = config.url,
			callback = config.callback,
			data = config.data,
			isAsync = config.isAsync;
		
		if (isAsync === undefined) {
			isAsync = true;
		}

		if (url === undefined) {
			return;
		}
				
		var request = _createRequest();
		if (request === undefined) {
			console.log("Unable to create request");
			return;
		}

		request.open("PUT", url, isAsync);
		if (callback !== undefined) {
			request.addEventListener("readystatechange", function (e) {
				_responseData(e, request, func);
			}, false);
		}
		request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		// send
		request.send(data);
	}
		
	function DELETE(config) {
		var url = config.url,
			callback = config.callback,
			data = config.data,
			isAsync = config.isAsync;
		
		if (isAsync === undefined) {
			isAsync = true;
		}

		if (url === undefined) {
			return;
		}
				
		var request = _createRequest();
		if (request === undefined) {
			console.log("Unable to create request");
			return;
		}

		request.open("DELETE", url, isAsync);
		
		if (callback !== undefined) {
			request.addEventListener("readystatechange", function (e) {
				_responseData(e, request, callback);
			}, false);
		}
		// send
		request.send();
	}
	
	
	// 상태 누적
//	window.addEventListener("popstate", function(e) {
//		if (e.state) {
//			location.load(e.state.url);
//		}
//	}.bind(this), false);
	
	// 공개 메서드 노출
	CAGE.ajax = {
		GET: GET,
		POST: POST,
		PUT: PUT,
		DELETE: DELETE
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));
(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var CAGE = window.CAGE || {};
	
	CAGE.ui = CAGE.ui || {};
	CAGE.ui.popup = CAGE.ui.popup || {};


	var Util = CAGE.util.dom;
	var Template = CAGE.util.template;
	var Ajax = CAGE.ajax;

  
    function eventEmitter(eventType) {
    	this.type = eventType;
	    this.eventHandlers = [];
    }
    eventEmitter.prototype.add = function(handler){
	    this.eventHandlers.push(handler);       
    };   
    eventEmitter.prototype.remove = function(handler){
    	var eventHandlers = this.eventHandlers;
    	for(var i in eventHandlers){
	    	if(eventHandlers[i] === handler){
		    	eventHandlers.splice(i, 1);
	    	}
    	}
    };   
    eventEmitter.prototype.dispatch = function(){
    	var eventHandlers = this.eventHandlers;
    	var param = undefined;
    	for(var i in eventHandlers){
    		param = (arguments[i])?arguments[i]:undefined;
	    	eventHandlers[i](param);
    	}
    };
    
	/*
	myPopup2.open.dispatch(param1, param2, ...);   
    */
    
    function popup(config) {
		this.el = config.element;
		this.template = config.template;	
		this.transitionEffect = (config.transitionEffect)?(config.transitionEffect):("zoom");	
		//this.data = (config.data)?(config.data):({});
		this.afteropen = new eventEmitter("afteropen");
		this.afterclose = new eventEmitter("afterclose");
		
		this.status = {
			data: false
		};
		
		this._init();
    }
    
    popup.prototype._init = function() {
		var el = this.el;
		var afteropen = this.afteropen;
		var afterclose = this.afterclose;
		var status = this.status;
		
		var close = this.close;
		
		el.addEventListener("click", openHandler.bind(this), false);

		function openHandler(event) {
			event.preventDefault();
			event.stopPropagation();
			
			var originalTarget;
			if(event.toElement) {
				originalTarget = event.toElement;
			} else if(event.originalTarget){
				originalTarget = event.originalTarget;
			}
			if(originalTarget === el) {
				this._counstructDOM();
				var popupWrapAnimation = document.querySelector(".popup-wrap.popup-animation");	
		        popupWrapAnimation.addEventListener("transitionend", afteropenCallbackRef, false);
		    }
		}

		function afteropenCallbackRef(event){
			//console.log(event);
			if(event.propertyName === "-webkit-transform" && status.data === false){	
				
				
				var popupWrapAnimation = document.querySelector(".popup-wrap.popup-animation");
				popupWrapAnimation.removeEventListener("webkitTransitionEnd", afteropenCallbackRef, false);    
	
				// 오픈 엔드 콜백 실행
				//console.log("왜 두번 실행되지?");
				afteropen.dispatch(document.querySelector(".popup-content"));
				status.data=!status.data;
				
				var popupBg = document.querySelector(".popup-bg");			
				var popupWrap = document.querySelector(".popup-wrap");
				var popupContainer = document.querySelector(".popup-container");
				var popupContent = document.querySelector(".popup-content");
				
				// esc 버튼으로 팝업 닫기 
				var escClose = function(e) {
				  if (e.keyCode == 27) { 
					  close();
				  }
				};
				
				document.addEventListener("keyup", escClose, false);				
				
				popupContainer.addEventListener("click",function(event){
					if(event.target === popupContainer || event.target === popupContent) {												
						var that = this;
						
						// 역 애니메이션 걸기
						Util.removeClass(popupBg, "popup-ready");
						Util.removeClass(popupWrap, "popup-ready");		
	 				        
				        popupBg.addEventListener("transitionend", (function(event){
							if(event.propertyName === "opacity" && status.data === true){	
								afterclose.dispatch(document.querySelector(".popup-content"));
								status.data=!status.data;
	
								var popupBg = document.querySelector(".popup-bg");			
								var popupWrap = document.querySelector(".popup-wrap");
		
						        popupBg?document.body.removeChild(popupBg):undefined;
						        popupWrap?document.body.removeChild(popupWrap):undefined;					        
							}	
				        }).bind(that), false);
					}
				}, false);											
	        }
		}
    }
	popup.prototype.open = function(){
		this.el.click();				
	};
	popup.prototype.close = function(){
		var popupContainer = document.querySelector(".popup-container");
		popupContainer.click();
	};	
    popup.prototype._getTemplate = function() {
	    return this.template;
    }
	popup.prototype._counstructDOM = function(){
		var transitionEffect = this.transitionEffect;
		var popupBg = document.createElement("div");
		
		Util.addClass(popupBg, "popup-bg");
		Util.addClass(popupBg, "popup-animation");

		var popupWrap = document.createElement("div");
		Util.addClass(popupWrap, "popup-wrap");
		Util.addClass(popupWrap, "popup-animation");
	
		var popupContainer = document.createElement("div");
		Util.addClass(popupContainer, "popup-container");
		var popupContent = document.createElement("div");
		Util.addClass(popupContent, "popup-content");

		popupContent.innerHTML = this._getTemplate();	
		popupContainer.appendChild(popupContent);
		
		if(transitionEffect != undefined) {
			Util.addClass(popupContent, transitionEffect + "-animation-dialog");
		}
		
		popupWrap.appendChild(popupContainer);			

		document.body.insertAdjacentHTML("afterbegin", popupWrap.outerHTML);			
		document.body.insertAdjacentHTML("afterbegin", popupBg.outerHTML);			

		//data-role : close 인 엘리먼트에 팝업 닫기 리스너 연결해 줌 
		var closeBtn = document.querySelector(".popup-wrap button[data-role='close']");
		if(closeBtn) {
			closeBtn.addEventListener("click", this.closePopup, false);
		}	
		
		var popupWrapAnimation = document.querySelector(".popup-wrap.popup-animation");
		var popupBgAnimation = document.querySelector(".popup-bg.popup-animation");

		// 크롬 애니메이션 버그해결을 위한 코드					
		popupWrapAnimation.offsetHeight;
		popupBgAnimation.offsetHeight;
		popupBgAnimation.style.transform="translateY(0px)";

		// start opening animation
		Util.addClass(popupWrapAnimation, "popup-ready");
		Util.addClass(popupBgAnimation, "popup-ready");	
	}	    
	   
	// POPUP을 상속받은 AJAX POPUP  
	function ajaxPopup(config){
		this.el = config.element;
		this.template = "!";	// ajaxPopup 은 템플릿을 비동기로 로딩한다.
		this.transitionEffect = (config.transitionEffect)?(config.transitionEffect):("zoom");	
		this.templateUrl = config.templateUrl; // 템플릿을 요청할 주소. 필수 옵션이다.
		this.templateLoader = (config.templateLoader)?(config.templateLoader):(
			function(response){
				return response;
			}
		);
		
		this.afteropen = new eventEmitter("afteropen");
		this.afterclose = new eventEmitter("afterclose");
		
		this.status = {
			data: false
		};
		// preload 기능이 필요하다.		
		Ajax.GET({
			url: this.templateUrl,
			callback: (function(response){
				this.template = this.templateLoader(response);
				this._init();
			}).bind(this)
		});				
	}	
	
	ajaxPopup.prototype = popup.prototype;	
	
	CAGE.ui.popup = {
		popup: popup,
		ajaxPopup: ajaxPopup
	};

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));(function(window) {
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



