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
			/*
			 * JoinAccount를 생성할때 인자로 전달하기위한 객체
			 */
			var args = {
				method: "POST",
				url: "/api/v1/accounts/",
				form: ".form-container",
				rule: {
					email: {
						type: "email"
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
			
			// Join객체를 생성한다.
			var JoinAccount = new Join(args);
			// joinPopup이 딷히면 JoinAccount.stop()을 호출하여 this.selectedEl에 붙어있던 keyup event를 해제한다.
			joinPopup.afterclose.add(JoinAccount.stop.bind(JoinAccount));

			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", function(evt) {
				
				/*
				 * submit 버튼을 누르면
				 * JoinAccount의 exec()함수를 호출하여 ajax 통신을 한다.
				 * 
				 * exec()함수에 아래의 callback 함수를 전달하여
				 * exec()함수가 호출되면 joinPopup이 닫히도록 한다.
				 */ 
				JoinAccount.exec(function() {
					joinPopup.afterclose.add(function() {location.reload();});
					joinPopup.close();
				}.bind(this));
				
			}, false);
			
			var emailDom = document.querySelector(".form-container input[name=email]");
			emailDom.focus();
		});
	}

	WILDGOOSE.modal.join = {
		init: init
	}

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}
	
}(this));
