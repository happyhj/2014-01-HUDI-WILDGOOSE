// searched에 입력하고 있는 글자를 가져옴
var searching = document.querySelector('#query-entry');
var searched;
searching.onkeyup = function() {
	var url = "/api/v1/most_similar_names?name=";
	searched = searching.value;
	url = url + searched;
	Ajax.GET(url, drawBox);
}

// 추천 검색어를 박스에 써 넣음
function drawBox(data) {
	Data = JSON.parse(data)["data"];
	var box = document.querySelector(".searched-box");
	box.style.display = "table";
	if(typeof Data != "undefined" && Data.length != 0) {
		var tr_pos = document.querySelector(".searched-box").lastElementChild.lastElementChild;
		var childCount = tr_pos.childElementCount;
		if(childCount != 0){
			for(var i = 0 ; i < childCount; i++){
				tr_pos.removeChild(tr_pos.childNodes[0]);
			}
		}
		
		for(var i = 0 ; i < Data.length; i++){
			tr_pos.insertAdjacentHTML('beforeend', '<td>'+Data[i]["name"]+'</td>');
		}
	}
	
	
}

// 검색창이 focus되어 있을 때만 추천 검색어 박스가 보임
searching.onblur = function() {
	var box = document.querySelector(".searched-box");
	box.style.display = "none";
}