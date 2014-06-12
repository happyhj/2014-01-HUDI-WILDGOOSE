(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.modal = WILDGOOSE.modal || {};
	WILDGOOSE.modal.withdraw = WILDGOOSE.modal.withdraw || {};

	// 의존성 선언 
//	var Account = WILDGOOSE.account;
	var Popup = CAGE.ui.popup;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
//	var LeaveAccount = WILDGOOSE.account.withdraw;
	var Leave = WILDGOOSE.account.withdraw;
	
	function init() {
		var leaveBtn = document.querySelector("#leave");
		var randNum = null;
		var leavePopup = new Popup.ajaxPopup({
			element: leaveBtn,
			templateUrl: "/api/v1/templates/withdraw.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				randNum = JSON.parse(AjaxResponse).data.rand;
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
				method: "POST",
				url: "/api/v1/accounts/",
				form: ".form-container",
				rule: {
					password: {
						type: "password"
					},
					confirm: {
						type: "confirm",
						target: "password"
					}
				},
				randNum: randNum
			};
			var LeaveAccount = new Leave(args);
			leavePopup.afterclose.add(LeaveAccount.stop.bind(LeaveAccount));

			var btn = arguments[0].querySelector("#withdraw");
			btn.addEventListener("click", function(evt) {
				
				LeaveAccount.exec(function() {
					leavePopup.afterclose.add(function() {location.reload();});
					leavePopup.close();
				}.bind(this));
				
			}, false);
			
			var pwDom = document.querySelector(".form-container input[name=password]");
			pwDom.focus();
			
		});
	}
	
	WILDGOOSE.modal.withdraw = {
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