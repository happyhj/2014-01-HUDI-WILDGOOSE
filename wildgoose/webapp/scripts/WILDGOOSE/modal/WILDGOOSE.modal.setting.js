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
	var User = WILDGOOSE.user;
	
	var SettingModal = {
		init: function() {
			this.settingBtn = document.querySelector("#setting");
			this.withdrawBtn = null;
			this.changePwBtn = null;
			
			this.cache = {
				clickHandler: this._settingHandler.bind(this)
			};
			
			this.template = {
				setting: Template.get({"url":"/api/v1/templates/setting.html"}),
				withdraw: null,
				changePw: null
			};
			
						
			this.settingPopup = new Popup.popup({
				element: this.settingBtn,
				template: this.template.setting
			});
			
			this.settingPopup.afteropen.add(function() {
				this._addClickEvent();
				this._getTemplates();
			}.bind(this));
			
			this.settingPopup.afterclose.add(function() {
				this._removeClickEvent();
			}.bind(this));
			
		},		
		
		_openPopup: function() {
			this._AccountInit();
			
			var btn = arguments[0].querySelector("#withdraw");
			btn.addEventListener("click", this._clickHandler.bind(this), false);
			
			var pwDom = document.querySelector(".form-container input[name=password]");
			pwDom.focus();
			
			
			
			var btn = arguments[0].querySelector("#change");
			btn.addEventListener("click", this._clickHandler.bind(this), false);
			var oldPwDom = document.querySelector(".form-container input[name=oldPassword]");
			oldPwDom.focus();
			
		},
		
		_closePopup: function() {
			this.settingPopup.afterclose.add(function() {location.reload();});
			this.settingPopup.close();
		},
		
		_AccountInit: function() {
		
			// Join객체를 생성한다.
			this.joinAccount = new Join({
				method: "POST",
				url: "/api/v1/accounts/",
				form: ".form-container",
				rule: {
					email: {
						type: "email"
					},
					password: {
						type: "password"
					},
					confirm: {
						type: "confirm",
						target: "password"
					}
				}
			});
			// joinPopup이 딷히면 JoinAccount.stop()을 호출하여 this.selectedEl에 붙어있던 keyup event를 해제한다.
			this.joinPopup.afterclose.add(this.joinAccount.stop.bind(this.joinAccount));
		},
		
		
		_clickHandler: function(evt) {
			if (this.leaveAccount !== null) {
				this.leaveAccount.exec(this._closePopup.bind(this));
			}
			
			else if (this.changePwAccount !== null) {
				this.changePwAccount.exec(this._closePopup.bind(this));
			}
		},
		
		/////////////
		
		
		_withdraw: function() {
//			this.leaveBtn = document.querySelector("#leave");
			this.leaveAccount = new Leave({
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
				randNum: User.getRandomNumber()
			});
			
			this.settingPopup.afterclose.add(this.leaveAccount.stop.bind(this.leaveAccount));
		},
		
		_changePw: function() {
			
			this.changePwAccount = new ChangePw({
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
				randNum: User.getRandomNumber()
			});
			
			
			this.settingPopup.afterclose.add(this.changePwAccount.stop.bind(this.changePwAccount));
		},
		
		
		/////////////
		
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
				this.template.withdraw = compiler({
					"email": User.getId()
				}, template);
			}
			
			if (this.template.changePw === null) {				
				var template = Template.get({"url":"/api/v1/templates/changePassword.html"});
				var compiler = Template.getCompiler(template);
				this.template.withdraw = compiler({
					"email": User.getId()
				}, template);
			}
		},
		_addClickEvent: function() {
			this.withdrawBtn = document.querySelector("#withdraw");
			this.changePwBtn = document.querySelector("#changePw");
			
			this.withdrawBtn.addEventListener("click", this.cache.settingHandler, false);
			this.changePwBtn.addEventListener("click", this.cache.settingHandler, false);
		},
		
		_removeClickEvent: function() {
			this.withdrawBtn.removeEventListener("click", this.cache.settingHandler, false);
			this.changePwBtn.removeEventListener("click", this.cache.settingHandler, false);
			
			this.withdrawBtn = null;
			this.changePwBtn = null;
		},
		
		_settingHandler: function(evt) {
			var targetEl = evt.target;
			this._updateUI(targetEl.name);
		}
	}
	

	WILDGOOSE.modal.setting = SettingModal;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}
}(this));
