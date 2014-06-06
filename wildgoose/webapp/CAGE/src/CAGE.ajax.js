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
	
	function _exec(config){
		var method = config.method,
		headerObj = config.headers,
		url = config.url,
		callback = config.callback,
		data = config.data,
		isAsync = config.isAsync;

		if (url == undefined) {
			return;
		}
		
		if (isAsync == undefined) {
			isAsync = true;
		}
				
		var request = _createRequest();
		if (request == undefined) {
			console.log("Unable to create request");
			return;
		}
		
		request.open(method, url, isAsync);
		if (callback !== undefined) {
			request.addEventListener("readystatechange", function (e) {
				_responseData(e, request, callback);
			}, false);
		}
		
		if (headerObj !== undefined) {
			for (var header in headerObj) {
				var content = headerObj[header];
				request.setRequestHeader(header, content);
			}
		}
		
		// send
		request.send(data);
	}

	// 공개 메서드 선언	
	function GET(config) {
		config.method = "GET";
		_exec(config);
	}
		
	function POST(config) {
		config.method = "POST";
		config.headers = {
			"Content-Type": "application/x-www-form-urlencoded"
		};
		_exec(config);
	}

	function PUT(config) {
		config.method = "PUT";
		config.headers = {
			"Content-Type": "application/x-www-form-urlencoded"
		};
		_exec(config);
	}
		
	function DELETE(config) {
		config.method = "DELETE";
		_exec(config);
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
//		_exec: _exec
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));
