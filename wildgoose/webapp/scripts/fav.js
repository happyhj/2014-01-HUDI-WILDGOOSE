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
	if (Util.hasClass(target, "favorite-on")) {
		//url = url + "?" + payload;
		Ajax.DELETE(url, function(data) {
			//console.log(data)
			if (data == "success") {
				Util.removeClass(target, "favorite-on");
			} else {
				// react fail
			}
		});
	} else {
		Ajax.POST(url, function(data) {
			//console.log(data)
			if (data == "success") {
				Util.addClass(target, "favorite-on");
			} else {
				// react fail
			}
		});
	}
	
}