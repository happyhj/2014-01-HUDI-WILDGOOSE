(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.header = APP.page.header || {};

	// 하위 모듈 import
	var User = WILDGOOSE.user;
	var Join = WILDGOOSE.modal.join;
	var Login = WILDGOOSE.modal.login;
	var Logout = WILDGOOSE.account.logout;
	var Setting = WILDGOOSE.modal.setting;
	
	var HeaderPage = {
		init: function() {
			Join.init();
			Login.init();
			Setting.init();
			this.userId = User.getId();
			this._logout();
			this._userPage();
		},
		
		_logout: function() {
			var args = {
				method: "DELETE",
				url: "/api/v1/session"
			};
			var LogoutAccount = new Logout(args);
			
			var logoutBtn = document.querySelector("#logout");
			logoutBtn.addEventListener("click", function() {
//				LogoutAccount.stop();
				LogoutAccount.exec(function() {
					location.href="/";
				});
				
			}.bind(this), false);
		},
		
		_userPage: function() {
			var mypageBtn = document.querySelector("#me");
			mypageBtn.addEventListener("click", function() {
				location.href = "/me/?user_id?".replace("?user_id?", this.userId);
			}.bind(this), false);
		}
	};

	APP.page.header = HeaderPage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);
(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.favorite = APP.page.favorite || {};

	// 하위 모듈 import
	var Favorite = WILDGOOSE.ui.favorite;
	var User = WILDGOOSE.user;
	
	var FavoritePage = {
		init: function() {
			Favorite.init({"userId":User.getId()});
		}		
	}
		
	APP.page.favorite = FavoritePage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.me = APP.page.me || {};

	// 하위 모듈 import
	var ArticleMore = WILDGOOSE.more.article;
	var Drag = WILDGOOSE.drag;
	var MeFav = WILDGOOSE.ui.favorite.me;
	var Template = CAGE.util.template;
	var User = WILDGOOSE.user;
	var Dom = CAGE.util.dom;
	var StartMe = WILDGOOSE.ui.startme;
	
	var MePage = {
		init: function() {
			this._articleMoreModule();
			this._dragModule();
			this._startMeModule();
			MeFav.init();
		},
		
		_articleMoreModule: function() {
			var moreEl = document.querySelector(".article-more button");
			var curNumDiv = document.querySelector(".article-more .state-article-curNum");
			var totalNumDiv = document.querySelector(".article-more .state-article-totalNum");
			var templateCompiler = Template.getCompiler();
			
			this.articleMore = new ArticleMore({
				more: {
					button: moreEl,
					curNum: (curNumDiv !== null)? parseInt(curNumDiv.innerText) : 0,
					totalNum: (totalNumDiv !== null)? parseInt(totalNumDiv.innerText) : 0
				},
				container: document.querySelector(".timeline-result ul"),
				template: Template.get({"url":"/api/v1/templates/articleCard.html"}),
				requestNum: 10
			});
		},
		
		_dragModule: function() {
			Drag.exe({
				body: document.querySelector('.dashboard-left ul'),
				tagName: "LI",
				movedClassName : "moving"
			});
		},
		
		_startMeModule: function() {
			var startBtn = document.querySelector(".start-me");
			if (startBtn !== null) {
				var starEls = document.getElementsByClassName("star");
				
				StartMe.init({
					button: startBtn,
					target: starEls
				});
			}				
		}
	}
		
	APP.page.me = MePage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);
(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.reporter = APP.page.reporter || {};

	// 하위 모듈 import
	var Graph = WILDGOOSE.ui.graph;
	var Fav = WILDGOOSE.ui.favorite;
	
	var ReporterPage = {
		init: function() {
			Graph.init();
			Fav.init();
		}		
	}
		
	APP.page.reporter = ReporterPage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);
(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.search = APP.page.search || {};

	// 하위 모듈 import
	var Search = WILDGOOSE.search;
	var Favorite = WILDGOOSE.ui.favorite;
	var User = WILDGOOSE.user;
	
	var SearchPage = {
		init: function() {
			this._searchModule();
			this._favoriteModule();
		},
		
		_searchModule: function() {
			Search.init({
				search: {
					box: "#query-entry",
					container: ".search-result > ul",
					templateURL: "/api/v1/templates/reporterCard.html",
					requestNum: 24
				},
				autocompletion: {
					list: ".search .auto-completion-list",
					requestNum: 7
				},
				more: {
					button: ".search-more button",
					curNum: ".search-more .state-search-curNum",
					totalNum: ".search-more .state-search-totalNum"
				}
			});
		},
		
		_favoriteModule: function() {
			Favorite.init();
		}
	}
		
	APP.page.search = SearchPage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);
(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.error = APP.page.error || {};

	// 하위 모듈 import
	var Ajax = CAGE.ajax; 
	var User = WILDGOOSE.user;
	var Join = WILDGOOSE.account.join;
	var Login = WILDGOOSE.account.login;
	
	var ErrorPage = {
		init: function(args) {
			this.accountName = args.accountName;
			
			if (args !== undefined && this.accountName !== undefined) {
				var curAccountName = "_" + this.accountName;
				this.curAccount = this[curAccountName]();
				
				
				if (this.accountName == "join") {
					var submitBtn = document.querySelector(".error-container .form-container button[name=submit]");
					submitBtn.addEventListener("click", function(evt) {
						this.curAccount.exec(function(){
							location.href="/";
						});
					}.bind(this), false);
				}
				else if (this.accountName == "login") {
					var submitBtn = document.querySelector(".error-container .form-container button[name=submit]");
					submitBtn.addEventListener("click", function(evt) {
						this.curAccount.exec(function(){
							location.reload();
						});
					}.bind(this), false);
				}
			}
			
			var emailEl = document.querySelector(".error-container .form-container input[name=email]");
			emailEl.focus();
		},
		
		_join: function() {
			return new Join({
				method: "POST",
				url: "/api/v1/accounts/",
				form: ".error-container .form-container",
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
		},
		
		_login: function() {
			return new Login({
				method: "POST",
				url: "/api/v1/session/",
				form: ".error-container .form-container",
				rule: {
					email: {
						type: "email",
						extend: {
							usable: [ function(inputEl, callback) {
								Ajax.GET({
									isAsync: false,
									url: "/api/v1/session?email=" + inputEl.value,
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
									error: function(responseObj) {
										console.log("Error!")
									}
								});
							}, "가입되지 않은 이메일입니다."]
						}
					},
					password: {
						type: "password"
					}
				},
				randNum: User.getRandomNumber()
			});
		}
	};

	APP.page.error = ErrorPage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);
