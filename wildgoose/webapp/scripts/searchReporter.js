
var loaded_templates = {};

window.addEventListener("load", function(e) {
	
	// 더보기 버튼 링크주소 설정
	var searchMoreBtn = document.querySelector(".search-more .search-button");
	if (searchMoreBtn != null) {
		searchMoreBtn.addEventListener("click", function(e){
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			
			// template
			var url = "/api/v1/subhtml/create_reporter_card";
			if (loaded_templates.reporter_card === undefined) {
				Ajax.GET(url, function(t) {
					loaded_templates.reporter_card = t;
				});
			}
			
			var totalNum = document.querySelector(".search-more .state-search-total").innerText;
			var requestNum = 24;
			
			
			// search
			var url = "/api/v1/more_reporter_card?q=" + searchQuery + "&last=" + totalNum + "&req=" + requestNum;
			Ajax.GET(url, function(rawD) {
				var pageInfo = {
		            url: location.href
		        }
				
				clickSearchMoreBtn(rawD, loaded_templates.reporter_card);
				
				history.pushState(pageInfo, null, pageInfo.url);
//				history.pushState(pageInfo);
//				document.title = oPageInfo.title = vMsg.page;
//                document.getElementById(sTargetId).innerHTML = vMsg.content;
//                if (bUpdateURL) {
//                    history.pushState(oPageInfo, oPageInfo.title, oPageInfo.url);
//                    bUpdateURL = false;
//                }
//                break;
				
				
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
	
	var searchResult = document.querySelector(".search-result > ul");
	var reporterCards = JSON.parse(rawD)["data"];
	var sizeOfCards = reporterCards.length;
	
	(function(cards) {
		for (var i=0; i<sizeOfCards; i++) {
			searchResult.appendChild(
				(function(card) {
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "card card-reporter");
					newLi.innerHTML = template;
					
					var nameAnchor = newLi.querySelector(".name a");
					nameAnchor.innerHTML = card.name;
					nameAnchor.setAttribute("href", "/reporters/" + card.id);
					
					var email = newLi.querySelector(".email");
					email.innerHTML = card.email;
					
					var pressTag = newLi.querySelector(".press-tag");
					pressTag.className = card.pressName + " press-tag";

					var headline = newLi.querySelector(".headline");
					headline.innerHTML = card.articleTitle;
					
					return newLi;
				}(reporterCards[i]))
			)
		}
	}(reporterCards));;
	
	var total = document.querySelector(".search-more .state-search-total").innerText;
	var total = parseInt(total);
	
	document.querySelector(".search-more .state-search-total").innerText = total + sizeOfCards;
	
}


window.addEventListener("popstate", historyHandler, false);

function historyHandler(e) {
//	e.preventDefault();
	if (e.state) {
		location.load(e.state.url);
	}
}