(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var APP = window.APP || {};
	APP.modal = APP.modal || {};
	APP.modal.change = APP.modal.change || {};
	APP.modal.change.pw = APP.modal.change.pw || {};

	// 의존성 선언 
//	var Account = WILDGOOSE.account;
	var Popup = CAGE.ui.popup;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
	var ChangePwAccount = WILDGOOSE.account.change.pw;
	
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
				form: ".form-container",
				types: ["oldPassword", "newPassword", "newConfirm"]
			}
			ChangePwAccount.init(args);
		

			var btn = arguments[0].querySelector("#change");
			btn.addEventListener("click", function(evt) {
				
				ChangePwAccount.exec(function() {
					changePwPopup.afterclose.add(function() {location.reload();});
					changePwPopup.close();
				}.bind(this));
				
			}, false);
			
		});
	}
	
	APP.modal.change.pw = {
		init: init
	}

	window.APP = APP;
	
	
	
}(this));