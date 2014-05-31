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
			/*
			 * template을 비동기로 요청하더라도 attachReceivedData는 template요청이 완료된 후 진행되므로
			 * Template.get()을 동기로 요청함
			 */
			var template = Template.get({"url":"/api/v1/templates/articleCard.html"});
			attachRecievedData(rawD, template);
		}});
	};
	
	
	function makeReporterCard(isLogined, data, template) {
		var className = "card card-reporter";
		if(isLogined){
			var star = card.querySelector(".star");
			console.log(star);
//			Util.removeClass(star, "invisible");
//			star.addEventListener("click", Fav.toggleFav, false);
		}
//		var newLi = document.createElement("li");
//		newLi.className = "card card-reporter";
		
		var templateCompiler = Template.getCompiler();
		var newLi = '<li class="' + className + '">' + templateCompiler(data, template) + '</li>';
		
		return newLi;
	};
	
	function getUserId() {
		var userId = document.getElementById("userId");
		if(userId != null){
			userId = document.getElementById("userId").getAttribute('email');
		}
		return userId;
	};
	
	
	function attachRecievedData(rawD, template) {	

		var totalNum = parseInt(document.querySelector(".search-more .state-search-totalNum").innerText);
		var searchResult = document.querySelector(".search-result > ul");
		
		var reporters = JSON.parse(rawD)["data"]["reporters"];
		var reporterNum = reporters.length;
		
		var curNumDiv = document.querySelector(".search-more .state-search-curNum");
		var curNum = parseInt(curNumDiv.innerText) + reporterNum;
		curNumDiv.innerText = curNum;
		
		// 로그인여부를 확인
		var isLogined = false;
		var userId = getUserId();
		if (userId != null) {
			var isLogined = true;
		}

		// append cards
		var cards = [];
		for (var i=0; i<reporterNum; i++) {
			var cardData = reporters[i];
			cards.push(makeReporterCard(isLogined, cardData, template));
			
//			if(logined){
//				var star = card.querySelector(".star");
//				console.log(star);
////				Util.removeClass(star, "invisible");
////				star.addEventListener("click", Fav.toggleFav, false);
		}
		searchResult.innerHTML += cards.join("");
	
		
//		searchResult.appendChild(card);
		
		
		
		// hide search-more button
		var searchMore = document.querySelector(".search-more");
		if (totalNum <= curNum) {
			searchMore.setAttribute("style", "display: none;");
		}
		
		// if logined
		// favorited reporter card's star must be turn on
		// and stars have their own event-handler
		// else
		// stars have to be invisible
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