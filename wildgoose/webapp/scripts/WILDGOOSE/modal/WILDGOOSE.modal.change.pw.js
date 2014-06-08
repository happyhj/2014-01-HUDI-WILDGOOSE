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
		
		var changePwPopup = new Popup.ajaxPopup({
			element: changePwBtn,
			templateUrl: "/api/v1/templates/changePassword.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				var randNum = JSON.parse(AjaxResponse).data.rand;
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
								Ajax.GET({
									isAsync: false,
									url: "/api/v1/accounts?email=" + inputEl.value,
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
				}
			};
			var ChangePwAccount = new ChangePw(args);		

			var btn = arguments[0].querySelector("#change");
			btn.addEventListener("click", function(evt) {
				
				ChangePwAccount.exec(function() {
					changePwPopup.afterclose.add(function() {location.reload();});
					changePwPopup.close();
				}.bind(this));
				
			}, false);
			
		});
	}
	
	WILDGOOSE.modal.change.pw = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
	
	
	
}(this));