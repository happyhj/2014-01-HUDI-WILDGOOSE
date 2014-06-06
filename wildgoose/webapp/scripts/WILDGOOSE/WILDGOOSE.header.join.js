(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};
	WILDGOOSE.header.join = WILDGOOSE.header.join || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 
	var Popup = CAGE.ui.popup;
	var Account = WILDGOOSE.account;

	function init() {

		// 회원가입 버튼을 찾는다
		var joinBtn = document.querySelector("#join");
		
		// 버튼에 가입창을 연결시킨다
		var joinPopup = new Popup.ajaxPopup({
			element: joinBtn,
			templateUrl: "/api/v1/templates/account.html",
			templateLoader: function(AjaxResponse) {
				return JSON.parse(AjaxResponse).data.template;
			}
		});
		
		// 가입창에 스크립트를 적용한다.
		// Ajax로 불러온 팝업창에는 스크립트를 넣을 수 없기 때문이다.
		joinPopup.afteropen.add(function() {
			var params = ["email", "password"];
/*    */	Account.addValidationEvent(params);
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", joinAccount, false);
		});

		// 가입하기 버튼을 눌렀을 때 실행될 내용을 담는다.
		function joinAccount() {
			var url = "/api/v1/accounts/";
			var form = document.querySelector(".form-container");
			
			var email = escape(form[0].value)
			var password = escape(form[1].value);
			var payload = "email=" + email + "&password=" + SHA256(password);
			Ajax.POST({
				"url": url,
				"callback": function(response) {
					var form = document.querySelector(".form-container");
					if (JSON.parse(response).status == 200) {
						joinPopup.afterclose.add(function() {location.reload();});
						joinPopup.close();
					} else {
						console.log("FAIL!");
					}
				},
				"data": payload
			});
		};
	}

	WILDGOOSE.header.join = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
}(this));
