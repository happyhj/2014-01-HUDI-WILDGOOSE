
var Popup = CAGE.ui.popup;
var Template = CAGE.util.template;

var joinBtn = document.querySelector("#join");

var joinPopup = Popup.ajaxPopup({
	element: joinBtn,
	transitionEffect: "turn", // turn
	callbacks: {
		afteropen: function() {
			addValidationEvent();
			var btn = arguments[0];
			btn.addEventListener("click", signUpAccout, false);
		},
		afterclose: function() {
			//alert("afterclose");	
		}
	},
	templateLoader: function(AjaxResponse) {
		return JSON.parse(AjaxResponse).data.template;
	}
});
var loginBtn = document.querySelector("#login");

var loginPopup = Popup.ajaxPopup({
	element: loginBtn,
	transitionEffect: "turn", // turn
	callbacks: {
		afteropen: function() {
			var btn = arguments[0];
			btn.addEventListener("click", loginAccount, false);
		},
		afterclose: function() {
			location.reload();
		}
	},
	templateLoader: function(AjaxResponse) {
		var templateStr = JSON.parse(AjaxResponse).data.template;
		var randNum = JSON.parse(AjaxResponse).message;
		var compiler = Template.getCompiler(templateStr);
		return compiler({
			"randNum": randNum
		}, templateStr);		
	}
});

var logoutBtn = document.querySelector(".header-btn#logout");
logoutBtn.addEventListener("click", function() {
	Ajax.DELETE({"url":'/api/v1/session'});
	updateTopbar(false);
}, false);

var timelineBtn = document.querySelector(".header-btn#timeline");
timelineBtn.addEventListener("click", function() {
	var userId = getUserId();
	location.href = "/users/?user_id?/timeline".replace("?user_id?", userId);;
}, false);

var favoriteBtn = document.querySelector(".header-btn#favorite");
favoriteBtn.addEventListener("click", function() {
	var userId = getUserId();
	location.href = "/users/?user_id?/favorites".replace("?user_id?", userId);
}, false);

function updateTopbar(isLogined) {
	var joinBtn = document.querySelector(".header-btn#join");
	var loginBtn = document.querySelector(".header-btn#login");
	var logoutBtn = document.querySelector(".header-btn#logout");
	var timelineBtn = document.querySelector(".header-btn#timeline");
	var favoriteBtn = document.querySelector(".header-btn#favorite");
	console.log(logoutBtn);
	if (isLogined == true) {
		joinBtn.className = "header-btn hidden";
		loginBtn.className = "header-btn hidden";
		logoutBtn.className = "header-btn";
		timelineBtn.className = "header-btn";
		favoriteBtn.className = "header-btn";
	} else {
		joinBtn.className = "header-btn";
		loginBtn.className = "header-btn";
		logoutBtn.className = "header-btn hidden";
		timelineBtn.className = "header-btn hidden";
		favoriteBtn.className = "header-btn hidden";
	}
}