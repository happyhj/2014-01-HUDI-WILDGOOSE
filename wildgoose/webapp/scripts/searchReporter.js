
window.addEventListener("load", function(e) {
	
	// 더보기 버튼 링크주소 설정
	var searchMoreBtn = document.querySelector(".search-more .search-button");
	console.log(searchMoreBtn)
	if (searchMoreBtn != null) {
		searchMoreBtn.addEventListener("click", function(e){
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			var url = "/api/v1/reporters?q=" + searchQuery;
			
			console.log("url: " + url);
			Ajax.requestData(url, clickSearchMoreBtn);
			
			
		},false);
	}
	
} ,false);


function clickSearchMoreBtn(rawD) {
	
	console.log(rawD);
	
}