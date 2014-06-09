(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.modal = WILDGOOSE.modal || {};
	WILDGOOSE.modal.join = WILDGOOSE.modal.join || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 
	var Popup = CAGE.ui.popup;
//	var JoinAccount = WILDGOOSE.account.join;
	var Join = WILDGOOSE.account.join;

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
			var args = {
				method: "POST",
				url: "/api/v1/accounts/",
				form: ".form-container",
				rule: {
					email: {
						type: "email",
					},
					password: {
						type: "password"
					},
					confirm: {
						type: "confirm",
						target: "password"
					}
				}
			};
			
			var JoinAccount = new Join(args);
			joinPopup.afterclose.add(JoinAccount.stop.bind(JoinAccount));

			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", function(evt) {
				
				JoinAccount.exec(function() {
					joinPopup.afterclose.add(function() {location.reload();});
					joinPopup.close();
				}.bind(this));
				
			}, false);
		});
	}

	WILDGOOSE.modal.join = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
}(this));
