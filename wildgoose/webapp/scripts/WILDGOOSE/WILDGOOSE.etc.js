(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.etc = WILDGOOSE.etc || {};	
	
	var Etc = {			
		getUserId: function() {
			this.userId = document.getElementById("userId").innerText;
			return this.userId;
		},
		isUserLogined: function() {
			if (this.getUserId() == "") {
				return false;
			}
			return true;
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.etc = Etc;
	window.WILDGOOSE = WILDGOOSE;
})();