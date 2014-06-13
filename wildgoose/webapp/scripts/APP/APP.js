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
	APP.page.mypage = APP.page.mypage || {};

	// 하위 모듈 import
	var Withdraw = WILDGOOSE.modal.withdraw;
	var ChangePw = WILDGOOSE.modal.change.pw;
	
	var MyPage = {
		init: function() {
			Withdraw.init();
			ChangePw.init();
		}		
	}
		
	APP.page.mypage = MyPage;
	
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
	APP.page.reporter = APP.page.reporter || {};

	// 하위 모듈 import
	var Graph = WILDGOOSE.ui.graph;
	
	var ReporterPage = {
		init: function() {
			Graph.init();
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
			Favorite.init({"userId":User.getId()});
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
