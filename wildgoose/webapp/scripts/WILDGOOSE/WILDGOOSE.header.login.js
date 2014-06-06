(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};
	WILDGOOSE.header.login = WILDGOOSE.header.login || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 
	var Popup = CAGE.ui.popup;
	var TemplateUtil = CAGE.util.template;
	var LoginAccount = WILDGOOSE.account.login;

	function init() {
		var loginBtn = document.querySelector("#login");
		var loginPopup = new Popup.ajaxPopup({
			element: loginBtn,
			templateUrl: "/api/v1/templates/login.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				var randNum = JSON.parse(AjaxResponse).data.rand;
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum
				}, templateStr);		
			}
		});
				
		loginPopup.afteropen.add(function() {
			var args = {
				form: ".form-container",
				types: ["email", "password"]
			};
			LoginAccount.init(args);
			
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", function(evt) {
				
				LoginAccount.exec(function() {
					loginPopup.afterclose.add(function() {location.reload();});
					loginPopup.close();
				}.bind(this));
				
			}, false);
		});
	}

	WILDGOOSE.header.login = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
	
}(this));
