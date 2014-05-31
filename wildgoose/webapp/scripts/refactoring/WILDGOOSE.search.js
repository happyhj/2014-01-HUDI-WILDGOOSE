(function() {
	var Ajax = CAGE.ajax;
	var Fav = WILDGOOSE.ui.favorite;
	var Template = WILDGOOSE.util.template;
	
	// searchMoreBtn
	function searchMore(clickEvent) {
		var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
		var curNum = document.querySelector(".search-more .state-search-curNum").innerText;
		var requestNum = 24;
		// search
		var url = "/api/v1/search?q=" + searchQuery + "&start_item=" + curNum + "&how_many=" + requestNum;
		
		Ajax.GET({"url":url, "callback":function(rawD) {
			
			var userId = null;
			var reporters = JSON.parse(rawD)["data"]["reporters"];
			/*
			 ******* WILDGOOSE.util사용함. ****** 
			 * template을 비동기로 요청하더라도 attachReceivedData는 template요청이 완료된 후 진행되므로
			 * Template.get()을 동기로 요청함
			 */
			var template = Template.get({"url":"/api/v1/templates/articleCard.html"});
			var isLogined = ((userId = getUserId()) != null)? true : false;
			
			// response data가 존재할 경우만 실행
			if (reporters.length != 0) {	
				var cards = makeReporterCards(isLogined, reporters, template);
				attachRecievedData(cards);

				var metaData = updateMetaData(cards.length);
				selectStatusOfSearchMoreBtn(metaData.curNum);
			}
		}});
	};
	
	// 현재 card의 개수를 업데이트
	function updateMetaData(updatedNum) {
		var curNumDiv = document.querySelector(".search-more .state-search-curNum");
		var curNum = parseInt(curNumDiv.innerText) + updatedNum;
		curNumDiv.innerText = curNum;
		
		return {"curNum": curNum};
	};
	
	// 더보기 버튼을 보여줄지 말지를 결정
	function selectStatusOfSearchMoreBtn(curNum) {
		// hide search-more button
		var totalNum = parseInt(document.querySelector(".search-more .state-search-totalNum").innerText);
		var searchMore = document.querySelector(".search-more");
		if (totalNum <= curNum) {
			searchMore.setAttribute("style", "display: none;");
		}
	};
	
	// card template에 데이터를 담은 template array를 반환
	function makeReporterCards(isLogined, reporters, template) {
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
//				var star = card.querySelector(".star");
//				console.log(star);
//				Util.removeClass(star, "invisible");
//				star.addEventListener("click", Fav.toggleFav, false);
			}
			var newLi = '<li class="' + className + '">' + templateCompiler(cardData, template) + '</li>';
			cards.push(newLi);
		}
		
		return cards;
	};
	
	function getUserId() {
		var userId = document.getElementById("userId");
		if(userId != null){
			userId = document.getElementById("userId").getAttribute('email');
		}
		return userId;
	};
	
	
	function attachRecievedData(cards) {	
		var searchResult = document.querySelector(".search-result > ul");
		searchResult.innerHTML += cards.join("");
	}

	function init() {
		// 첫 검색 결과가 결과의 전부일 경우
		window.addEventListener("load", function(e) {
			var searchMore = document.querySelector(".search-more");
			var totalNum = parseInt(document.querySelector(".search-more .state-search-totalNum").innerText);
			var curNum = parseInt(document.querySelector(".search-more .state-search-curNum").innerText);

			if (totalNum <= curNum) {
				searchMore.setAttribute("style", "display: none;");
			}	
		}, false);
		// 더보기 버튼 클릭이벤트 설정
		window.addEventListener("load", function(e) {	
			var searchMoreBtn = document.querySelector(".search-more");
			if (searchMoreBtn != null) {
				searchMoreBtn.addEventListener("click", searchMore, false);
			}
		} ,false);
		// 상태 누적
		window.addEventListener("popstate", function(e) {
			if (e.state) {
				location.load(e.state.url);
			}
		}, false);

	}
		
	
	init();
})();