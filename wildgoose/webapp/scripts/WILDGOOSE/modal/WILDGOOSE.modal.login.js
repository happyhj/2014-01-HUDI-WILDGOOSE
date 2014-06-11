(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.modal = WILDGOOSE.modal || {};
	WILDGOOSE.modal.login = WILDGOOSE.modal.login || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 
	var Popup = CAGE.ui.popup;
	var TemplateUtil = CAGE.util.template;
//	var LoginAccount = WILDGOOSE.account.login;
	var Login = WILDGOOSE.account.login;

	function init() {
		var randNum = null;
		var loginBtn = document.querySelector("#login");
		var loginPopup = new Popup.ajaxPopup({
			element: loginBtn,
			templateUrl: "/api/v1/templates/login.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				randNum = JSON.parse(AjaxResponse).data.rand;
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum
				}, templateStr);		
			}
		});
				
		loginPopup.afteropen.add(function() {
			var args = {
				method: "POST",
				url: "/api/v1/session/",
				form: ".form-container",
				rule: {
					email: {
						type: "email",
						extend: {
							usable: [ function(inputEl, callback) {
								Ajax.GET({
									isAsync: false,
									url: "/api/v1/session?email=" + inputEl.value,
									success: function(responseObj) {
										var validity = true;
										var isProgressing = true;
										callback(validity, isProgressing);
									},
									failure: function(responseObj) {
										var validity = false;
										var isProgressing = true;
										callback(validity, isProgressing);
									}
								});
							}, "가입되지 않은 이메일입니다."]
						}
					},
					password: {
						type: "password"
					}
				},
				randNum: randNum
			};
			var LoginAccount = new Login(args);
			loginPopup.afterclose.add(LoginAccount.stop.bind(LoginAccount));
			
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", function(evt) {
				
				LoginAccount.exec(function() {
					loginPopup.afterclose.add(function() {location.reload();});
					loginPopup.close();
				}.bind(this), function() {
					var messageDiv = document.getElementById("result-msg");
					messageDiv.innerText = "비밀번호가 틀렸습니다.";
				});
				
			}, false);
			
			var emailDom = document.querySelector(".form-container input[name=email]");
			emailDom.focus();
		});
	}

	WILDGOOSE.modal.login = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
	
}(this));
