var autocomplete = {
	init : function(searchBox, selector) {
		this.row = {
			max : 5,
			count : 5
		};
		this.isSearching = false;
		this.isHovering = false;
		this.hasMouseRader = false;
		
		this.box = searchBox;
		this.list = document.querySelector(selector);
		console.log(this.box.form);
//		document.addEventListener("keypress", function (evt) { 
//			var targetEl = evt.target;
//			if ((evt.keyCode == 13) && targetEl.type == "search") {
//				return false;
//			}
//		}, false);
		
		this.cache = {
			searchedQuery : "",
			row : null,
			callbackRef : {
				notify : this.notify.bind(this),
				expired : this.expired.bind(this),
				boxHandler : this.boxHandler.bind(this),
				keyboardHandler : this.keyboardHandler.bind(this),
				drawList : this.drawList.bind(this),
				setHovering : this.setHovering.bind(this),
				selectRow : this.selectRow.bind(this),
				pressEnter : this.pressEnter.bind(this)
			}
		};
		
		this.box.addEventListener("focus", this.cache.callbackRef.notify);
		this.box.addEventListener("blur", this.cache.callbackRef.expired);
		
		this.box.focus();
	},

	expired : function(evt) {
		if (this.isSearching != false && this.isHovering == false) {
			console.log("searchBox expired");
			this.off();
			clearInterval(this.isSearching);
			this.box.removeEventListener("keydown", this.cache.callbackRef.keyboardHandler);
			
			this.isSearching = false;
		}
	},

	notify : function(evt) {
		if (this.isSearching == false) {
			console.log("searchBox standBy");
			this.on();
//			this.isSearching = 1;
			this.isSearching = setInterval(this.cache.callbackRef.boxHandler, 100);
			this.box.addEventListener("keydown", this.cache.callbackRef.keyboardHandler);
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
		if (keyID == 38 || keyID == 40) {
			this.highlightRow(39 - keyID);
		}
//		else if (keyID == 13) {
//			
//			
//			console.log("select row");
//			this.selectRow(evt);
//		}
	},
//	addMouseEvents : function() {
//		this.list.addEventListener("mousemove", this.cache.callbackRef.mouseHandler, false);
//		this.list.addEventListener("click", this.cache.callbackRef.mouseHandler);
//		
//	},
//	removeMouseEvents : function() {
//		this.list.removeEventListener("mousemove", this.cache.callbackRef.mouseHandler, false);
//		this.list.removeEventListener("click", this.cache.callbackRef.mouseHandler);
//	},
	
//	mouseHandler : function(evt) {
//		if (evt.type == "mousemove") {
//			this.highlightOut(this.cache.row);
//			console.log("moving");
//		}
//		else if (evt.type == "click") {
//			console.log("click");
//		}
//	},
	

	// list
	on : function() {
		this.list.style.display = "inline-block";
		if (this.hasMouseRader == false) {
			this.addMouseRader();
			this.list.addEventListener("click", this.cache.callbackRef.selectRow);
		}
		this.box.form.addEventListener("keypress", this.cache.callbackRef.pressEnter, false);
		
	},
	off : function() {
		this.cache.row = null;
		this.list.style.display = "none";
		if (this.hasMouseRader == true) {
			this.removeMouseRader();
			this.list.removeEventListener("click", this.cache.callbackRef.selectRow);
		}
		this.box.form.removeEventListener("keypress", this.cache.callbackRef.pressEnter, false);
	},
	highlightIn : function(rowNum) {
		this.list.children[rowNum].className = "highlight";
	},
	highlightOut : function(rowNum) {
		if (rowNum !== null) {
			this.list.children[rowNum].className = "";
		}
	},
	pressEnter : function (evt) {
		// 기존 enter입력을 막기위해서.
		evt.preventDefault();
		console.log("press enter");
	},

	addMouseRader : function() {
		this.hasMouseRader = true;
		this.list.addEventListener("mouseover", this.cache.callbackRef.setHovering, false);
		this.list.addEventListener("mouseout", this.cache.callbackRef.setHovering, false);
	},
	removeMouseRader : function() {
		this.hasMouseRader = false;
		this.list.removeEventListener("mouseover", this.cache.callbackRef.setHovering, false);
		this.list.removeEventListener("mouseout", this.cache.callbackRef.setHovering, false);
	},
	setHovering : function(evt) {
		this.highlightOut(this.cache.row);
		
		if (evt.type == "mouseover") {
			this.isHovering = true;
			console.log("hovering");
		}
		else if (evt.type == "mouseout") {
			this.isHovering = false;
			console.log("not hovering");
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
	
	selectRow : function(evt) {
		var targetEl = evt.target;
		this.box.value = targetEl.innerText;
		this.isHovering = false;
		this.expired();
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
