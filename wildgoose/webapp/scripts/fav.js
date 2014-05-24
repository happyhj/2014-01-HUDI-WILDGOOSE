attatchEventToFavBtn();
function attatchEventToFavBtn() {
	var stars = document.querySelectorAll(".favorite");
	for (var i = 0; i < stars.length; i++) {
		var star = stars[i];
		star.addEventListener("click", toggleFav, false);
	}
}

function toggleFav(e) {
	var target = e.target;
	
	var card = target.parentNode.parentNode;
	var press = target.nextElementSibling;
	var email = target.previousElementSibling;
	var name = card.children[0].firstElementChild.firstElementChild;
	console.log(email);
	console.log(name);
	var headline = card.children[1].firstElementChild;
	
	var pressClasses = press.className.split(" ");
	var pressName = (pressClasses[0] != "press-tag")?pressClasses[0]:pressClasses[1];
	var pressNameTemp = pressName.split("-blur");
	pressName = pressNameTemp[0];
	
	var nephew = target.parentElement.firstElementChild.firstElementChild
	var reporterId = nephew.getAttribute("href").split("reporters/")[1];
	var url = "/api/v1/users/reporters/id/"+reporterId;
	//var payload = "reporter_id="+reporterId;
	if (Util.hasClass(target, "on")) {
		//url = url + "?" + payload;
		Ajax.DELETE(url, function(data) {
			//console.log(data)
			if (data == "success") {
//				debugger;
				Util.removeClass(target, "on");
				Util.removeClass(press, pressName);
				Util.addClass(press, pressName + "-blur");
				Util.addClass(card, "favorite-card-blur");
				Util.addClass(target, "off");
				Util.addClass(name, "blur");
				Util.addClass(email, "blur");
				Util.addClass(headline, "blur");
			} else {
				// react fail
			}
		});
	} else {
		Ajax.POST(url, function(data) {
			//console.log(data)
			if (data == "success") {
//				debugger;
				Util.removeClass(name, "blur");
				Util.removeClass(email, "blur");
				Util.removeClass(headline, "blur");
				Util.removeClass(press, pressName + "-blur");
				Util.removeClass(card, "favorite-card-blur");
				Util.removeClass(target, "off");
				Util.addClass(target, "on");
				Util.addClass(press, pressName);
				
			} else {
				// react fail
			}
		});
	}
}

function getFavs() {
	var url = "api/v1/users/reporters/id";
	Ajax.GET(url, function(jsonStr) {
		var result = JSON.parse(jsonStr);
		favs = result["data"];
		var reporterCards = document.querySelectorAll(".card-reporter");
		updateFavs(reporterCards);
	});
}

function updateFavs(reporterCards) {
	if (favs == undefined || favs == "잘못된 접근입니다") {
		return;
	}
	for (var i = 0; i < reporterCards.length; i++) {
		var card = reporterCards[i];
		var reporterName = card.querySelector(".name a");
		var reporterId = reporterName.getAttribute("href").split("/")[2];
		if (favs.indexOf(parseInt(reporterId)) >= 0) {
			card.querySelector(".favorite").className = "favorite on";
		}
	}
}