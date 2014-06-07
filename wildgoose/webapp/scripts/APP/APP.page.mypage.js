(function() {	
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
})(this);