var searchBox = {
	init : function() {
		this.maxRow = 5;
		this.isSearching = null;
		
		this.cache = {
			searchedQuery : "",
			callbackRef : {
				notify : this.notify.bind(this),
				expired : this.expired.bind(this),
				boxHandler : this.boxHandler.bind(this),
				listHandler : this.listHandler.bind(this),
				drawList : this.drawList.bind(this)
			}
		};
		
		this.box = document.querySelector('#query-entry');
		this.list = document.querySelector(".auto-completion-list");
		
		this.box.addEventListener("focus", this.cache.callbackRef.notify);
		this.box.addEventListener("blur", this.cache.callbackRef.expired);
		
		this.box.focus();
	},

	expired : function(evt) {
		console.log("searchBox expired");
		if (this.isSearching != null) {
			this.off();
			this.list.innerHTML = "";
			clearInterval(this.isSearching);
			this.box.removeEventListener("keydown", this.cache.callbackRef.listHandler);
			this.isSearching = null;
			currentQuery = "";
		}
	},

	notify : function(evt) {
		console.log("searchBox standBy");
		this.currentRow = 0;
//		debugger;
		if (this.isSearching == null) {
			this.isSearching = setInterval(this.cache.callbackRef.boxHandler, 100);
			this.box.addEventListener("keydown", this.cache.callbackRef.listHandler);
		}
	},
	on : function() {
		this.list.style.display = "inline-block";
	},
	off : function() {
		this.list.style.display = "none";
	},

	boxHandler : function(evt) {
		var currentQuery = this.box.value;
		if (currentQuery != "" && currentQuery != this.cache.searchedQuery) {
//			this.find(currentQuery);
			this.cache.searchedQuery = currentQuery;
		}
	},

	listHandler : function(evt) {
		var keyID = evt.keyCode;
		// UP/DOWN Key
		if (keyID == 38 || keyID == 40) {
			console.log("press up down key");
			this.highlightRow(39 - keyID);
			return;
		}
		if (keyID == 13) {
			console.log("select row");
//			this.selectRow();
		}
	},

	find : function(searched) {
		var url = "/api/v1/search/most_similar_names?name=" + searched;
		Ajax.GET(url, this.cache.callbackRef.drawList);
	},

	drawList : function(response) {
		var data = JSON.parse(response)["data"];
		if (data.length == 0) {
			this.off();
		}
		
		if (data !== undefined && data.length != 0) {
			this.on();
			var li_template = "";
			for (var i = 0; i < data.length; i++) {
				li_template += "<li><div>" + data[i]["name"] + "</div></li>";
			}
			this.list.innerHTML = li_template;
		}
	},
	
	selectRow : function() {
		evt = document.createEvent("MouseEvents");
		evt.initMouseEvent("click", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		this.possibilities[this.currentRow].dispatchEvent(evt)
	},

	highlightRow : function(change) {
		
//		evt = document.createEvent("MouseEvents");
//		evt.initMouseEvent("mouseout", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
//		this.possibilities[this.currentRow].dispatchEvent(evt)

		this.currentRow -= change;
		if (this.currentRow >= 5) {
			this.currentRow -= 5;
		} else if (this.currentRow < 0) {
			this.currentRow += 5;
		}
//		evt = document.createEvent("MouseEvents");
//		evt.initMouseEvent("mouseover", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
//		this.possibilities[this.currentRow].dispatchEvent(evt)
	}

}
//debugger;
// searchBox에 focus evt가 발생시 searchBox객체 init
//searchBox.init();

//searching.addEventListener("keyup", searchBoxHandler);
//searching.addEventListener("click", toggleSearchBox);

//function searchBoxHandler(e) {
//	var keyID = e.keyCode;
//	// UP/DOWN Key
//	if (keyID == 38 || keyID == 40) {
//		highlightRow(39 - keyID);
//		return;
//	}
//	if (keyID == 13) {
//		selectRow();
//	}
//
//	searchSimilarKeyword(searching.value);
//}

function searchSimilarKeyword(searched) {
	var url = "/api/v1/search/most_similar_names?name=" + searched;
	Ajax.GET(url, drawBox);
}

//검색창이 클릭하면 추천 검색어 박스가 보였다 보이지 않았다 함
//function toggleSearchBox() {
//	var box = document.querySelector(".searched-box");
//	if(getComputedStyle(box, null).display == "none"){
//		box.style.display = "table";
//	} else {
//		box.style.display = "none";
//	}
//}

//var searched = document.querySelector(".searched-box tr");
//searched.addEventListener("mouseover", function(e) {
//	var target = e.target;
//	target.className = "mouseover";
//});
//searched.addEventListener("mouseout", function(e) {
//	var target = e.target;
//	target.className = "";
//});
//

//
//// 추천 검색어를 박스에 써 넣음
//function drawBox(data) {
//	var Data = JSON.parse(data)["data"];
//	var box = document.querySelector(".searched-box");
//	box.style.display = "table";
//	if(typeof Data != "undefined" && Data.length != 0) {
//		var tr_pos = document.querySelector(".searched-box").lastElementChild.lastElementChild;
//		var childCount = tr_pos.childElementCount;
//		if(childCount != 0){
//			for(var i = 0 ; i < childCount; i++){
//				tr_pos.removeChild(tr_pos.childNodes[0]);
//			}
//		}
//		
//		for(var i = 0 ; i < Data.length; i++){
//			tr_pos.insertAdjacentHTML('beforeend', '<td>'+Data[i]["name"]+'</td>');
//		}
//	}
//}
//
//// 클릭하면 검색어 입력
//document.querySelector(".searched-box").addEventListener('click', insetText, false);
//function insetText(e) {
//	if(e.target.tagName == "TD") {
//		searching.value = e.target.textContent
//		document.querySelector(".searched-box").style.display = "none";
//	}
//}
