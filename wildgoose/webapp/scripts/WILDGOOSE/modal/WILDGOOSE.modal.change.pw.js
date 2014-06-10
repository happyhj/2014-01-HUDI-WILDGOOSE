(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.modal = WILDGOOSE.modal || {};
	WILDGOOSE.modal.change = WILDGOOSE.modal.change || {};
	WILDGOOSE.modal.change.pw = WILDGOOSE.modal.change.pw || {};

	// 의존성 선언 
//	var Account = WILDGOOSE.account;
	var Popup = CAGE.ui.popup;
	var Ajax = CAGE.ajax;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
//	var ChangePwAccount = WILDGOOSE.account.change.pw;
	var ChangePw = WILDGOOSE.account.change.pw;
	
	function init() {
		var changePwBtn = document.querySelector("#change-password");
		var randNum = null;
		var changePwPopup = new Popup.ajaxPopup({
			element: changePwBtn,
			templateUrl: "/api/v1/templates/changePassword.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				randNum = JSON.parse(AjaxResponse).data.rand;
				var userId = document.getElementById("userId").textContent;
//				console.log(AjaxResponse);
//				console.log("template Rand: " + randNum);
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum,
					"email": userId
				}, templateStr);
			}
		});
		
		changePwPopup.afteropen.add(function() {
			var args = {
				method: "PUT",
				url: "/api/v1/accounts/",
				form: ".form-container",
				rule: {
					oldPassword: {
						type: "password",
						extend: {
							exist: [ function(inputEl, callback) {
								Ajax.POST({
									isAsync: false,
									url: "/api/v1/session",
									success: function(responseObj) {
										console.log("Success!");
										var validity = true;
										var isProgressing = true;
										callback(validity, isProgressing);
									},
									failure: function(responseObj) {
										console.log("Failure!");
										var validity = false;
										var isProgressing = true;
										callback(validity, isProgressing);
									},
									data: (function() {
										var email = escape(document.getElementById("userId").textContent);
										var password = SHA256(SHA256(escape(inputEl.value)) + randNum);
										return "email=" + email + "&password=" + password;
									}())
								});
							}, "비밀번호가 다릅니다."]
						}
					},
					newPassword:{
						type: "password"
					},
					newConfirm: {
						type: "confirm",
						target: "newPassword"
					}
				},
				randNum: randNum
			};
			var ChangePwAccount = new ChangePw(args);
			changePwPopup.afterclose.add(ChangePwAccount.stop.bind(ChangePwAccount));

			var btn = arguments[0].querySelector("#change");
			btn.addEventListener("click", function(evt) {
				
				ChangePwAccount.exec(function() {
					changePwPopup.afterclose.add(function() {location.reload();});
					changePwPopup.close();
				}.bind(this));
				
			}, false);
			
			var oldPwDom = document.querySelector(".form-container input[name=oldPassword]");
			oldPwDom.focus();
			
		});
	}
	
	WILDGOOSE.modal.change.pw = {
		init: init
	};

	window.WILDGOOSE = WILDGOOSE;
	
	
	
}(this));