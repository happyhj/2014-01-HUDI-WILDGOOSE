(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.modal = WILDGOOSE.modal || {};
	WILDGOOSE.modal.setting = WILDGOOSE.modal.setting || {};

	// 의존성 선언
	var Ajax = CAGE.ajax; 
	var Popup = CAGE.ui.popup;
	var Template = CAGE.util.template;
	
//	var JoinAccount = WILDGOOSE.account.join;
	var Withdraw = WILDGOOSE.account.withdraw;
	var ChangePw = WILDGOOSE.account.change.pw;

	var Setting = {
		init: function() {
			this.withdrawBtn = null;
			this.changePwBtn = null;
			this.cache = {
				clickHandler: this._clickHandler.bind(this)
			};
			
			this.template = {
				setting: Template.get({"url":"/api/v1/templates/setting.html"}),
				withdraw: null,
				changePw: null
			};
			
			this.userId = document.getElementById("userId").textContent;
			this.randNum = null;
			
			
			console.log(this.template.setting);
			/*
			this.el = config.element;
			this.template = config.template;	
			this.transitionEffect = (config.transitionEffect)?(config.transitionEffect):("zoom");	
			//this.data = (config.data)?(config.data):({});
			this.afteropen = new eventEmitter("afteropen");
			this.afterclose = new eventEmitter("afterclose");
			
			this.status = {
				data: false
			};
			*/
			
			var settingBtn = document.querySelector("#setting");
			var randNum = null;
			
			var settingPopup = new Popup.popup({
				element: settingBtn,
				template: this.template.setting
			});
			
			
//			var settingPopup = new Popup.ajaxPopup({
//				element: settingBtn,
//				templateUrl: "/api/v1/templates/setting.html",
//				templateLoader: function(AjaxResponse) {
//					var templateStr = JSON.parse(AjaxResponse).data.template;
//					return templateStr;
//				}
//			});
//					
			
			settingPopup.afteropen.add(function() {
				this._addClickEvent();
				this._getTemplates();
			}.bind(this));
			
			settingPopup.afterclose.add(function() {
				this._removeClickEvent();
				
			}.bind(this));
//			
			
			
//			this._withdraw();
//			this._changePw();
		},
		_getTemplates: function() {
			if (this.template.withdraw === null) {				
				this.template.withdraw = Template.get({"url":"/api/v1/templates/withdraw.html"});
			}
			
			if (this.template.changePw === null) {				
				this.template.changePw = Template.get({"url":"/api/v1/templates/changePassword.html"});
			}
		},
		_addClickEvent: function() {
			this.withdrawBtn = document.querySelector("#withdraw");
			this.changePwBtn = document.querySelector("#changePw");
			
			this.withdrawBtn.addEventListener("click", this.cache.clickHandler, false);
			this.changePwBtn.addEventListener("click", this.cache.clickHandler, false);
		},
		
		_removeClickEvent: function() {
			this.withdrawBtn.removeEventListener("click", this.cache.clickHandler, false);
			this.changePwBtn.removeEventListener("click", this.cache.clickHandler, false);
			
			this.withdrawBtn = null;
			this.changePwBtn = null;
		},
		
		_clickHandler: function(evt) {
			var targetEl = evt.target;
			console.log(targetEl);
			// targetEl의 template을 가져오고
			// 화면을 바꾼다.
		},
		
		
//		_getTemplate: fucntion(args) {
//			
//		},
		
		
		_withdraw: function() {
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
		},
		
		_changePw: function() {
			var changePwBtn = document.querySelector("#change-password");
			var randNum = null;
			var changePwPopup = new Popup.ajaxPopup({
				element: changePwBtn,
				templateUrl: "/api/v1/templates/changePassword.html",
				templateLoader: function(AjaxResponse) {
					var templateStr = JSON.parse(AjaxResponse).data.template;
					randNum = JSON.parse(AjaxResponse).data.rand;
					var userId = document.getElementById("userId").textContent;
//					console.log(AjaxResponse);
//					console.log("template Rand: " + randNum);
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
	}
	
	

	WILDGOOSE.modal.setting = Setting;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}
}(this));
