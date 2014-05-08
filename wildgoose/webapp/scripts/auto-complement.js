// searched에 입력하고 있는 글자를 가져옴
var searching = document.querySelector('#query-entry');

searching.onkeyup = function() {
	var url = "/api/v1/search/most_similar_names?name=";
	var searched = searching.value;
	url = url + searched;
	Ajax.GET(url, drawBox);
}

// 추천 검색어를 박스에 써 넣음
function drawBox(data) {
	var Data = JSON.parse(data)["data"];
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

//검색창이 클릭하면 추천 검색어 박스가 보였다 보이지 않았다 함
searching.onclick = function() {
	var box = document.querySelector(".searched-box");
	if(getComputedStyle(box, null).display == "none"){
		box.style.display = "table";
	} else {
		box.style.display = "none";
	}
}

// 클릭하면 검색어 입력
document.querySelector(".searched-box").addEventListener('click', insetText, false);
function insetText(e) {
	if(e.target.tagName == "TD") {
		searching.value = e.target.textContent
		document.querySelector(".searched-box").style.display = "none";
	}
}
