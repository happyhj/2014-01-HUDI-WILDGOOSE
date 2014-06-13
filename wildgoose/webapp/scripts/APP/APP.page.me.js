(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var APP = window.APP || {};
	APP.page = APP.page || {};
	APP.page.me = APP.page.me || {};

	// 하위 모듈 import
	var Drag = WILDGOOSE.drag;
	var Fav = WILDGOOSE.ui.favorite;
	var MyPage = {
		init: function() {
			var args = {
					body: document.querySelector('.dashboard-left ul'),
					tagName: "LI",
					movedClassName : "moving"
					}

			Drag.exe(args);
			Fav.init();
		}		
	}
		
	APP.page.me = MyPage;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = APP;
		// browser export
	} else {
		window.APP = APP;
	}
})(this);