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
	var Account = WILDGOOSE.account;

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
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", loginAccount, false);
		});

		function loginAccount(popup) {
			var email = document.querySelector(".form-container input[name=email]").value;
			var password = document.querySelector(".form-container input[name=password]").value;
			var hashedPassword = SHA256(password);	
			var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
			var finalPassword = SHA256(hashedPassword+randomNumber);
			var url = "/api/v1/session";
			var payload = "email="+email+"&password="+finalPassword;
			Ajax.POST({
				"url": url,
				"callback": function(response) {
					var form = document.querySelector(".form-container");
					if (JSON.parse(response).status == 200) {
						popup.afterclose.add(function() {location.reload();});
						popup.close();
					}
				},
				"data": payload
			});
		};
	}

	WILDGOOSE.header.login = {
		init: init
	}

	window.WILDGOOSE = WILDGOOSE;
	
}(this));
