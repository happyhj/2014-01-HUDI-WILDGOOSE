
window.addEventListener("load", function(e) {
	
	// 더보기 버튼 링크주소 설정
	var searchMoreBtn = document.querySelector(".search-more .search-button");
	if (searchMoreBtn != null) {
		searchMoreBtn.addEventListener("click", function(e){
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			
			// template
			var url = "/api/v1/subhtml/create_reporter_card";
			var template;
			Ajax.requestData(url, function(t) {
				template = t;
			});
			
			var totalNum = document.querySelector(".search-more .state-search-total").innerText;
			var requestNum = 24;
			
			// search
			var url = "/api/v1/reporters?q=" + searchQuery + "&last=" + totalNum + "&req=" + requestNum;
			Ajax.requestData(url, function(rawD) {
				clickSearchMoreBtn(rawD, template);
			});
			
		},false);
	}
	
} ,false);


function clickSearchMoreBtn(rawD, template) {
	
	var hasMoreCards = rawD.indexOf(0);
	var searchMore = document.querySelector(".search-more");
	searchMore.setAttribute("style", "display: none;");
	if (hasMoreCards != "0") {
		searchMore.setAttribute("style", "");
	}
	
	rawD = rawD.substring(1, rawD.length);
	console.log(rawD);
	
	var searchResult = document.querySelector(".search-result > ul");
	var reporterCards = JSON.parse(rawD);
	var sizeOfCards = reporterCards.length;
	console.log(reporterCards);
	
	(function(cards) {
		for (var i=0; i<sizeOfCards; i++) {
			searchResult.appendChild(
				(function(card) {
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "card card-reporter");
					newLi.innerHTML = template;
					
					// name of li
					newLi.children[0].children[0].children[0].innerHTML = card.name;
					// a_href of li
					newLi.children[0].children[0].children[0].setAttribute("href", "/reporters/" + card.id);
					// email of li
					newLi.children[0].children[1].innerHTML = card.email;
					// pressTag_class of li
					newLi.children[0].children[2].setAttribute("class", card.pressName += " press-tag");
					// headline of li
					newLi.children[1].children[0].innerHTML = card.articleTitle;
					
					return newLi;
				}(reporterCards[i]))
			)
		}
	}(reporterCards));;
	
	var total = document.querySelector(".search-more .state-search-total").innerText;
	var total = parseInt(total);
	
	document.querySelector(".search-more .state-search-total").innerText = total + sizeOfCards;
	
//	console.log(total + sizeOfCards);
//	console.log(document.querySelector(".search-more .state-search-total"));
}





