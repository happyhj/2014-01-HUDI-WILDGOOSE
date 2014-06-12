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
			var randNum = null;
			this.userId = document.getElementById("userId").textContent;
			this.randNum = Ajax.GET({
				"uri": "/api/v1/sessions/rand",
				"success": function(responseObj) {
					this.randNum = responseObj.data.randNum;
				}.bind(this),
				"failure": function(responseObj) {
					console.log("randomnumber can't downloaded");
				},
			});
			
			var settingBtn = document.querySelector("#setting");
			
			
			var settingPopup = new Popup.popup({
				element: settingBtn,
				template: this.template.setting
			});
			
			
			settingPopup.afteropen.add(function() {
				this._addClickEvent();
				this._getTemplates();
			}.bind(this));
			
			settingPopup.afterclose.add(function() {
				this._removeClickEvent();
				
			}.bind(this));

		},
		_updateUI: function(name) {
			var template = this.template[name];
			var popupWrap = document.querySelector(".popup-wrap");
			if (template !== undefined) {
				popupWrap.innerHTML = template;
			}
		},
		
		_getTemplates: function() {
			if (this.template.withdraw === null) {
				var template = Template.get({"url":"/api/v1/templates/withdraw.html"});
				var compiler = Template.getCompiler(template);
				this.template.withdraw = comiler({
					"randNum": randNum,
					"email": userId
				}, template);
			}
			
			if (this.template.changePw === null) {				
				var template = Template.get({"url":"/api/v1/templates/changePassword.html"});
				var compiler = Template.getCompiler(template);
				this.template.withdraw = comiler({
					"randNum": randNum,
					"email": userId
				}, template);
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
			this._updateUI(targetEl.name);
		},
		
		_withdraw: function() {
			var leaveBtn = document.querySelector("#leave");
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
