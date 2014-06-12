(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.user = WILDGOOSE.user || {};
	
	var Ajax = CAGE.ajax;
	
	var User = {
		randNum: null,
		userId: undefined,
		getId: function() {
			if (this.userId !== undefined) {
				return this.userId;
			}
			
			var userIdDiv = document.getElementById("userId");
			if (userIdDiv !== undefined) {
				this.userId = userIdDiv.innerText;
			}
			return this.userId;
		},
		isLogined: function() {
			if (this.getId() == "") {
				return false;
			}
			return true;
		},
		getRandomNumber: function() {
			if (this.randNum !== null) {
				return this.randNum;
			}

			Ajax.GET({
				isAsync: false,
				url:"/api/v1/session/rand",
				success: function(responseObj){
					this.randNum = responseObj.data.rand;
				}.bind(this)
			});
			return this.randNum;
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.user = User;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	
})();