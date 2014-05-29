
//var loaded_templates = {};

var searchResultContainer = document.querySelector(".search-result");
searchResultContainer.addEventListener("DOMSubtreeModified", toggelStar, false);

function toggelStar() {
//  alert("DOMSubtreeModified fired!");
	var stars = document.querySelectorAll(".card-reporter h4.favorite");

	if(favs.length == 0 || favs == "잘못된 접근입니다") {
		console.log("in");
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
}

window.addEventListener("load", function(e) {	
	// 더보기 버튼 링크주소 설정
	var searchMoreBtn = document.querySelector(".search-more .search-button");
	if (searchMoreBtn != null) {
		searchMoreBtn.addEventListener("click", function(e){
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			var curNum = document.querySelector(".search-more .state-search-curNum").innerText;
			var requestNum = 24;
			console.log("curNum: " + curNum);
			// search
			var url = "/api/v1/search?q=" + searchQuery + "&start_item=" + curNum + "&how_many=" + requestNum;
			Ajax.GET(url, function(rawD) {
//				var pageInfo = {
//		            url: location.href
//		        }
//				
				clickSearchMoreBtn(rawD);
				
//				history.pushState(pageInfo, null, pageInfo.url);
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


window.addEventListener("load", searchMoreBtnHandler, false);
function searchMoreBtnHandler() {
	var totalNum = parseInt(document.querySelector(".search-more .state-search-totalNum").innerText);
	var curNum = parseInt(document.querySelector(".search-more .state-search-curNum").innerText);
	
	var searchMore = document.querySelector(".search-more");
	if (totalNum <= curNum) {
		searchMore.setAttribute("style", "display: none;");
	}	
}


function clickSearchMoreBtn(rawD) {	
	var totalNum = parseInt(document.querySelector(".search-more .state-search-totalNum").innerText);
	var searchResult = document.querySelector(".search-result > ul");
	
	var reporters = JSON.parse(rawD)["data"]["reporters"];
	var sizeOfCards = reporters.length;
	
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
					newLi.innerHTML = templateCompiler(reporters[i]);
					attatchEventToFavBtn();
					updateFavs([newLi]);
					return newLi;
				}(reporters[i]))
			)
		}
	}(reporters));;
	
	var curNumDiv = document.querySelector(".search-more .state-search-curNum");
	var curNum = parseInt(curNumDiv.innerText) + sizeOfCards;
	curNumDiv.innerText = curNum;
	
	var searchMore = document.querySelector(".search-more");
	if (totalNum <= curNum) {
		searchMore.setAttribute("style", "display: none;");
	}	
}


window.addEventListener("popstate", historyHandler, false);

function historyHandler(e) {
//	e.preventDefault();
	if (e.state) {
		location.load(e.state.url);
	}
}