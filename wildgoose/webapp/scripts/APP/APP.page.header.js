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
	
	var HeaderPage = {
		init: function() {
			Join.init();
			Login.init();
			this.userId = User.getId();
			this._logout();
			this._userPage();
		},
		
		_logout: function() {
			var logoutBtn = document.querySelector("#logout");
			logoutBtn.addEventListener("click", function() {
				
				Logout.exec(function() {
					location.href="/";
				});
				
			}.bind(this), false);
		},
		
		_userPage: function() {
			var timelineBtn = document.querySelector("#timeline");
			timelineBtn.addEventListener("click", function() {
				location.href = "/users/?user_id?/timeline".replace("?user_id?", this.userId);
			}.bind(this), false);
			
			var favoriteBtn = document.querySelector("#favorite");
			favoriteBtn.addEventListener("click", function() {
				location.href = "/users/?user_id?/favorites".replace("?user_id?", this.userId);
			}.bind(this), false);
			
			var mypageBtn = document.querySelector("#mypage");
			mypageBtn.addEventListener("click", function() {
				location.href = "/users/?user_id?/mypage".replace("?user_id?", this.userId);
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
