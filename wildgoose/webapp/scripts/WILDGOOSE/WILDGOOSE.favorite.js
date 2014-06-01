(function() {
	'use strict';
	var document = window.document;
	var console = window.console;

	// 의존성 선언
	var Ajax = CAGE.ajax;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.favorite = WILDGOOSE.favorite || {};

	var Favorite = {
		favoriteList : [],

		attatchEventToFavBtn : function() {
			var stars = document.querySelectorAll(".star");
			for (var i = 0; i < stars.length; i++) {
				var star = stars[i];
				star.addEventListener("click", this.toggleFav, false);
				star.addEventListener("click", function(e) {
					console.log(e.target);
					Util.addClass(e.target, "pumping");
					setTimeout(function() {
						Util.removeClass(e.target, "pumping");
					}, 300)
				}, false);
			}
		},

		toggleFav : function(e) {
			var target = e.target;
			var card = target.parentElement.parentElement;
			var anchor = card.querySelector('a');
			var reporterId = anchor.getAttribute("href").split("reporters/")[1];
			var userId = getUserId();
			var url = "api/v1/users/" + userId + "/favorites/?reporter_id="
					+ reporterId;

			var card = target.parentNode.parentNode;

			if (Util.hasClass(target, "on")) {
				Ajax.DELETE({
					"url" : url,
					"callback" : function(data) {
						var data = JSON.parse(data);
						if (data.status == 200) {
							Util.removeClass(target, "on");
							Util.addClass(target, "off");
							Util.addClass(card, "blur");
						} else {
							// react fail
						}
					}
				});
			} else {
				Ajax.POST({
					"url" : url,
					"callback" : function(data) {
						var data = JSON.parse(data);
						if (data.status == 200) {
							Util.addClass(target, "on");
							Util.removeClass(target, "off");
							Util.removeClass(card, "blur");

						} else {
							// react fail
						}
					}
				});
			}
		},

		updateFavs : function(curNum, reqNum) {
			var reporterCards = document.querySelectorAll(".card");
			if (reporterCards.length != 0) {
				for (var i = curNum; i < reqNum; i++) {
					var card = reporterCards[i];
					var reporterName = document.querySelector(".card .name a");
					var reporterId = reporterName.getAttribute("href").split("/")[2];
					if (this.favoriteList.indexOf(parseInt(reporterId)) >= 0) {
						card.querySelector(".star").className = "star on";
					}
				}				
			}
		}
	};
	
	// 초기화
	if (isUserLogined()) {
		// userID 확인
		var userId = getUserId();
		
		// 모든 별에 eventlistener 붙이기
		Favorite.attatchEventToFavBtn();
		
		// user의 Favorite 목록 획득
		var url = "api/v1/users/" + userId + "/favorites/";
		Ajax.GET({
			"url" : url,
			"callback" : function(jsonStr) {
				var result = JSON.parse(jsonStr);
				var reporterCards = result["data"]["reporterCards"]
				for (var i=0; i<reporterCards.length; i++) {
					var card = reporterCards[i];
					Favorite.favoriteList.push(card["id"]);
				}
				// 불러온 목록 내부에 존재하는 favorite 업데이트
				Favorite.updateFavs(0, 24);
			}
		});
	}

	WILDGOOSE.favorite = Favorite;

})();