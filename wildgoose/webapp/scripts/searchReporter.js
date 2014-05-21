
//var loaded_templates = {};

var searchResultContainer = document.querySelector(".search-result");
searchResultContainer.addEventListener("DOMSubtreeModified", function() {
//    alert("DOMSubtreeModified fired!");
	var loginBtn = document.querySelector(".header-btn#login");
	var stars = document.querySelectorAll(".card-reporter h4.favorite");
	if(favs == undefined || favs == "잘못된 접근입니다") {
		 [].forEach.call(
				 stars, 
				 function(el){
					 Util.addClass(el,"invisible");
				 }
		 );
	} else {
		/*
		 [].forEach.call(
				 stars, 
				 function(el){
					 Util.addClass(el,"invisible");
				 }
		 );	
		 */	
	}
}, false);


window.addEventListener("load", function(e) {	
	// 더보기 버튼 링크주소 설정
	var searchMoreBtn = document.querySelector(".search-more .search-button");
	if (searchMoreBtn != null) {
		searchMoreBtn.addEventListener("click", function(e){
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			// template
			var url = "/api/v1/subhtml/create_reporter_card";
/*
			if (loaded_templates.reporter_card === undefined) {
				Ajax.GET(url, function(t) {
					loaded_templates.reporter_card = t;
				});
			}
*/		
			var totalNum = document.querySelector(".search-more .state-search-total").innerText;
			var requestNum = 24;
			
			// search
			var url = "/api/v1/more_reporter_card?q=" + searchQuery + "&last=" + totalNum + "&req=" + requestNum;
			Ajax.GET(url, function(rawD) {
				var pageInfo = {
		            url: location.href
		        }
				
				clickSearchMoreBtn(rawD);
				
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


function clickSearchMoreBtn(rawD) {
	
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
					// html에서 템플릿을 불러온 뒤
					var templateStr = document.getElementById("reporter-card-template").innerHTML;
					var templateCompiler = Util.getTemplateCompiler(templateStr);
					// 해당 템픗릿 컴파일러에 데이터를 담은 객체를 넘겨준다. // 완성된 partial html을 newLi 내부에 채운다.
					newLi.innerHTML = templateCompiler(reporterCards[i]);
					updateFavs([newLi]);
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