(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var APP = window.APP || {};
	APP.modal = APP.modal || {};
	APP.modal.withdraw = APP.modal.withdraw || {};

	// 의존성 선언 
//	var Account = WILDGOOSE.account;
	var Popup = CAGE.ui.popup;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
	var LeaveAccount = WILDGOOSE.account.withdraw;
	
	function init() {
		var leaveBtn = document.querySelector("#leave");
		
		var leavePopup = new Popup.ajaxPopup({
			element: leaveBtn,
			templateUrl: "/api/v1/templates/withdraw.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				var randNum = JSON.parse(AjaxResponse).data.rand;
				var userId = document.getElementById("userId").textContent;
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum,
					"email": userId
				}, templateStr);		
			}
		});
		
		leavePopup.afteropen.add(function() {
			var args = {
				form: ".form-container",
				types: ["password", "confirm"]
			}
			LeaveAccount.init(args);
		

			var btn = arguments[0].querySelector("#withdraw");
			btn.addEventListener("click", function(evt) {
				
				LeaveAccount.exec(function() {
					leavePopup.afterclose.add(function() {location.reload();});
					leavePopup.close();
				}.bind(this));
				
			}, false);
			
		});
	}
	
	APP.modal.withdraw = {
		init: init
	}

	window.APP = APP;
	
	
	
}(this));