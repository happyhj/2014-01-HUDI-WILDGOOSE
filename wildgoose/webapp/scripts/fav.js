(function() {
	var stars = document.querySelectorAll(".favorite");
	for (var i = 0; i < stars.length; i++) {
		var star = stars[i];
		star.addEventListener("click", toggleFav, false);
	}
})()

function toggleFav(e) {
	var target = e.target;
	var nephew = target.parentElement.firstElementChild.firstElementChild
	var reporterId = nephew.getAttribute("href").split("reporters/")[1];
	var url = "/api/v1/users/reporters/"+reporterId;
	//var payload = "reporter_id="+reporterId;
	if (Util.hasClass(target, "on")) {
		//url = url + "?" + payload;
		Ajax.DELETE(url, function(data) {
			//console.log(data)
			if (data == "success") {
				Util.removeClass(target, "on");
				Util.addClass(target, "off");
			} else {
				// react fail
			}
		});
	} else {
		Ajax.POST(url, function(data) {
			//console.log(data)
			if (data == "success") {
				Util.removeClass(target, "off");
				Util.addClass(target, "on");
			} else {
				// react fail
			}
		});
	}
}

function getFavs() {
	var url = "api/v1/users/reporters";
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