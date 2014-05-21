var searchBox = {
		init : function() {
			this.listListener = false;
			this.callbackRefCache = {
					notify : this.notify.bind(this),
					expired : this.expired.bind(this),
					boxHandler : this.boxHandler.bind(this),
					listHandler : this.listHandler.bind(this),
					drawList : this.drawList.bind(this)
			};
			this.box = document.querySelector('#query-entry');
			
			// searched에 입력하고 있는 글자를 가져옴
			this.list = document.querySelector(".searched-box");
			this.currentRow = 0;
			
			this.box.addEventListener("focus", this.callbackRefCache.notify);
			this.box.addEventListener("blur", this.callbackRefCache.expired);
		},
		
		expired : function(evt) {
			console.log("searchBox expired");
			this.box.removeEventListener("keypress", this.callbackRefCache.boxHandler);
			if (this.listListener) {
				this.listListener = false;
				this.box.removeEventListener("keyup", this.callbackRefCache.listHandler);
			}
		},
		
		notify : function(evt) {
			console.log("searchBox standBy");
			this.box.addEventListener("keypress", this.callbackRefCache.boxHandler);
		},
				
		boxHandler : function(evt) {
			console.log(this.listListener);
			if (!this.listListener) {
				this.listListener = true;
				console.log(this.list);
				this.box.addEventListener("keyup", this.callbackRefCache.listHandler);
			}
		    this.find(this.box.value);
		},
		
		listHandler : function(evt) {
			var keyID = evt.keyCode;
			console.log(keyID);
			// UP/DOWN Key
		    if (keyID == 38 || keyID == 40) {
//		        this.highlightRow(39 - keyID);
		    	console.log("press up down key");
		        return;
		    }
		    if (keyID == 13) {
//		    	this.selectRow();
		    	console.log("select row");
		    }
		},

		on : function() {
			this.box.style.display = "table";
		},
		
		off : function() {
			this.box.style.display = "none";
		},
		
		
		find : function (searched) {
			var url = "/api/v1/search/most_similar_names?name=" + searched;
			Ajax.GET(url, this.callbackRefCache.drawList);
		},
		
		drawList : function(data) {
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
		},
		selectRow : function() {
//		    var searched = document.querySelector(".searched-box");
		    var possibilities = searched.querySelectorAll("td");
		    
//		    evt = document.createEvent("MouseEvents");
//		    evt.initMouseEvent("click", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
//		    possibilities[currentRow].dispatchEvent(evt)
		},
		
		highlightRow : function(change) {
		    var searched = document.querySelector(".searched-box");
		    var possibilities = searched.querySelectorAll("td");
		    
		    evt = document.createEvent("MouseEvents");
		    evt.initMouseEvent("mouseout", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		    possibilities[currentRow].dispatchEvent(evt)
		    
		    currentRow -= change;
		    if (currentRow >= 5) {
		        currentRow -= 5;
		    } else if (currentRow < 0) {
		        currentRow += 5;
		    }
		    evt = document.createEvent("MouseEvents");
		    evt.initMouseEvent("mouseover", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
		    possibilities[currentRow].dispatchEvent(evt)
		}
		//		toggle : function(evt) {
		////	debugger;
		//	if(getComputedStyle(searchBox.box, null).display == "none"){
		//		this.on();
		//	} else {
		//		this.off();
		//	}
		//},

}
//debugger;
// searchBox에 focus evt가 발생시 searchBox객체 init
searchBox.init();


//searching.addEventListener("keyup", searchBoxHandler);
//searching.addEventListener("click", toggleSearchBox);



function searchBoxHandler(e) {
	var keyID = e.keyCode; 
    // UP/DOWN Key
    if (keyID == 38 || keyID == 40) {
        highlightRow(39 - keyID);
        return;
    }
    if (keyID == 13) {
        selectRow();
    }
    
    searchSimilarKeyword(searching.value);
}

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
