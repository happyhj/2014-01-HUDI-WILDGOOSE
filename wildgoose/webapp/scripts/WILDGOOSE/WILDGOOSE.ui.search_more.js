(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.SearchMore = WILDGOOSE.ui.SearchMore || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Fav = WILDGOOSE.ui.favorite;
	var Template = CAGE.util.template;
	
	var SearchMore = {
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
				debugger;
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
				
				/*
				 * 로그인 상태에 따른 별을 보여주는 로직 추가 필요
				 * 
				if logined
				favorited reporter card's star must be turn on
				and stars have their own event-handler
				else
				stars have to be invisible
				 */
				if(isLogined){
//					var star = card.querySelector(".star");
//					console.log(star);
//					Util.removeClass(star, "invisible");
//					star.addEventListener("click", Fav.toggleFav, false);
				}
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
		},
		
		init: function(args) {
			this.searchMoreBtn = document.querySelector(args.button);
			this.searchResult = document.querySelector(args.container);
			this.requestNum = args.requestNum;
			this.template = Template.get({"url":args.templateUrl});
			
			// 더보기 버튼 클릭이벤트 설정
			if (this.searchMoreBtn != null) {
				this.searchMoreBtn.addEventListener("click", this._more.bind(this), false);
				this._selectStatusOfSearchMoreBtn();
			}
		}	
	};
	
	// 공개 메서드 노출
	WILDGOOSE.ui.search_more = SearchMore;
})();

window.addEventListener("load", function(evt){
	var SearchMore = WILDGOOSE.ui.search_more;
	SearchMore.init({button: ".search-more", container: ".search-result > ul", templateUrl: "/api/v1/templates/reporterCard.html", requestNum: 24});
}, false);

