var autocomplete = {
	init : function(searchBox, selector) {
		this.row = {
			max : 5,
			count : 5
		};
		this.isSearching = null;
		
		this.box = searchBox 
		this.list = document.querySelector(selector);
		this.submit = document.querySelector("." + this.box.form.className + " input[type=submit]");
		
		
		this.cache = {
			searchedQuery : "",
			row : null,
			callbackRef : {
				notify : this.notify.bind(this),
				expired : this.expired.bind(this),
				boxHandler : this.boxHandler.bind(this),
				keyboardHandler : this.keyboardHandler.bind(this),
				drawList : this.drawList.bind(this),
				mouseHandler : this.mouseHandler.bind(this)
			}
		};
		
		this.box.addEventListener("focus", this.cache.callbackRef.notify);
		this.box.addEventListener("blur", this.cache.callbackRef.expired);
		
		this.box.focus();
	},

	expired : function(evt) {
		console.log("searchBox expired");
		if (this.isSearching != null) {
			this.off();
			clearInterval(this.isSearching);
			this.box.removeEventListener("keydown", this.cache.callbackRef.keyboardHandler);
//			this.list.removeEventListener("mousemove", this.cache.callbackRef.mouseHandler, false);
			this.list.removeEventListener("click", this.cache.callbackRef.mouseHandler);
			
			this.isSearching = null;
			currentQuery = "";
		}
	},

	notify : function(evt) {
		console.log("searchBox standBy");
		if (this.isSearching == null) {
			this.isSearching = 1;
//			this.isSearching = setInterval(this.cache.callbackRef.boxHandler, 100);
			this.box.addEventListener("keydown", this.cache.callbackRef.keyboardHandler);
//			this.list.addEventListener("mousemove", this.cache.callbackRef.mouseHandler, false);
			this.list.addEventListener("click", this.cache.callbackRef.mouseHandler);
		}
	},
	on : function() {
		this.list.style.display = "inline-block";
	},
	off : function() {
		this.cache.row = null;
		this.list.style.display = "none";
	},
	highlightIn : function(rowNum) {
		this.list.children[rowNum].className = "highlight";
	},
	highlightOut : function(rowNum) {
		if (rowNum !== null) {
			this.list.children[rowNum].className = "";
		}
	},

	boxHandler : function(evt) {
		var currentQuery = this.box.value;
		if (currentQuery != "" && currentQuery != this.cache.searchedQuery) {
			this.find(currentQuery);
			this.cache.searchedQuery = currentQuery;
		}
	},

	keyboardHandler : function(evt) {
		var keyID = evt.keyCode;
		// UP/DOWN Key
		console.log(keyID);
		if (keyID == 38 || keyID == 40) {
			console.log("press up down key");
			this.highlightRow(39 - keyID);
			return;
		}
		
		if (keyID == 13) {
			evt.preventDefault();
			console.log("select row");
//			this.selectRow();
		}
	},
	mouseHandler : function(evt) {
		if (evt.type == "mousemove") {
			this.highlightOut(this.cache.row);
			console.log("moving");
		}
		else if (evt.type == "click") {
			console.log("click");
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
		console.log("change: " + change);
		
		// 처음 입력시
		if (this.cache.row == null) {
			this.cache.row = 0;
			change = 0;
		}
		
		var cacheRow = this.cache.row;
		var currentRow = cacheRow - change;
		if (currentRow >= 5) {
			currentRow = 0;
		}
		else if (currentRow < 0) {
			currentRow = 4;
		}
		console.log(cacheRow);
		console.log(currentRow);
		
		this.highlightOut(cacheRow);
		this.highlightIn(currentRow);
		this.cache.row = currentRow;
	}

}

// searchBox에 focus evt가 발생시 searchBox객체 init
var searchBox = document.querySelector('#query-entry');
autocomplete.init(searchBox, ".auto-completion-list");
