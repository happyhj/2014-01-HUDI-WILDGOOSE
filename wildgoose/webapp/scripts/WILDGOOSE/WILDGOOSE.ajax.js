(function(window) {
	'use strict';
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ajax = WILDGOOSE.ajax || {};

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
	
	// 공개 메서드 노출
	WILDGOOSE.ajax = {
		GET: GET,
		POST: POST,
		PUT: PUT,
		DELETE: DELETE,
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
