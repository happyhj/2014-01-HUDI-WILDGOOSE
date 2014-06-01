
var Popup = CAGE.ui.popup;
var Template = CAGE.util.template;
var Util = CAGE.util.dom;

var joinBtn = document.querySelector("#join");

var joinPopup = new Popup.ajaxPopup({
	element: joinBtn,
	templateUrl: "/api/v1/templates/account.html",
	templateLoader: function(AjaxResponse) {
		return JSON.parse(AjaxResponse).data.template;
	}
});

joinPopup.afteropen.add(function() {
	addValidationEvent();
	var btn = arguments[0].querySelector("#create");
	btn.addEventListener("click", signUpAccout, false);
});

var loginBtn = document.querySelector("#login");

var loginPopup = new Popup.ajaxPopup({
	element: loginBtn,
	templateUrl: "/api/v1/templates/login.html",
	templateLoader: function(AjaxResponse) {
		var templateStr = JSON.parse(AjaxResponse).data.template;
		var randNum = JSON.parse(AjaxResponse).message;
		var compiler = Template.getCompiler(templateStr);
		return compiler({
			"randNum": randNum
		}, templateStr);		
	}
});

loginPopup.afteropen.add(function() {
	var btn = arguments[0].querySelector("#create");
	btn.addEventListener("click", loginAccount, false);
});
loginPopup.afterclose.add(function() {
	location.reload();
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