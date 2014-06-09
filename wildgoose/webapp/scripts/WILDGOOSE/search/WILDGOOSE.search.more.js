(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.search = WILDGOOSE.search || {};
	WILDGOOSE.search.more = WILDGOOSE.search.more || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Template = CAGE.util.template;
	var Fav = WILDGOOSE.ui.favorite;
	
	var More = {
		init: function(args) {
			this.searchMoreBtn = args.button;
			this.searchResult = args.container;
			this.requestNum = args.requestNum;
			this.template = args.template;
			
			
			// 더보기 버튼 클릭이벤트 설정
			if (this.searchMoreBtn != null) {
				this.searchMoreBtn.addEventListener("click", this._more.bind(this), false);
				var curNumDiv = document.querySelector(".search-more .state-search-curNum");
				var curNum = parseInt(curNumDiv.innerText);
				this._selectStatusOfSearchMoreBtn(curNum);
			}
		},
		_more: function(evt) {
			// click evt
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			var curNum = document.querySelector(".search-more .state-search-curNum").innerText;
			// search
			var url = "/api/v1/search?q=" + searchQuery + "&start_item=" + curNum + "&how_many=" + this.requestNum;
			Ajax.GET({"url":url, "callback":this._responseHandler.bind(this)});
		},
		
		_responseHandler: function(rawD) {
			var userId = null;
			var reporters = JSON.parse(rawD)["data"]["reporters"];
			var isLogined = ((userId = this._getUserId()) != null)? true : false;
			
			// response data가 존재할 경우만 실행
			if (reporters.length != 0) {	
				var cards = this._makeReporterCards(isLogined, reporters);
				this._attachRecievedData(cards);
				var metaData = this._updateMetaData(cards.length);
				this._selectStatusOfSearchMoreBtn(metaData.curNum);
				Fav.updateFavs(metaData.curNum, this.requestNum);
				Fav.attatchEventToFavBtn(metaData.curNum, this.requestNum);
			}
		},
		
		// 현재 card의 개수를 업데이트
		_updateMetaData: function(updatedNum) {
			var curNumDiv = document.querySelector(".search-more .state-search-curNum");
			var curNum = parseInt(curNumDiv.innerText) + updatedNum;
			curNumDiv.innerText = curNum;
			
			return {"curNum": curNum};
		},
		
		// 더보기 버튼을 보여줄지 말지를 결정
		_selectStatusOfSearchMoreBtn: function(curNum) {
			var searchMore = document.querySelector(".search-more");
			var totalNumDiv = document.querySelector(".search-more .state-search-totalNum");
			if (totalNumDiv === null) {
				searchMore.setAttribute("style", "display: none;");
				return;
			}
			var totalNum = parseInt(totalNumDiv.innerText);
			if (totalNum <= curNum) {
				searchMore.setAttribute("style", "display: none;");
			}
		},
		
		// card template에 데이터를 담은 template array를 반환
		_makeReporterCards: function(isLogined, reporters) {
			var templateCompiler = Template.getCompiler();
			var className = "card card-reporter";
			var reporterNum = reporters.length;
			var cards = [];
		
			// card template을 cards array에 담음
			for (var i=0; i<reporterNum; i++) {
				var cardData = reporters[i];
				var newLi = '<li class="' + className + '">' + templateCompiler(cardData, this.template) + '</li>';
				cards.push(newLi);
			}
			
			return cards;
		},
		
		_getUserId: function() {
			var userId = document.getElementById("userId");
			if(userId != null){
				userId = document.getElementById("userId").getAttribute('email');
			}
			return userId;
		},
		
		_attachRecievedData: function(cards) {
			this.searchResult.innerHTML += cards.join("");
		}	
	};
	
	// 공개 메서드 노출
	WILDGOOSE.search.more = More;
})();
