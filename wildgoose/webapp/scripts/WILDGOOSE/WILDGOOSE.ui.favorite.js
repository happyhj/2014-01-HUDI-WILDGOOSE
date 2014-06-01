(function() {
	'use strict';
	var document = window.document;
	var console = window.console;

	// 의존성 선언
	var Ajax = CAGE.ajax;
	var Util = CAGE.util.dom;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.favorite = WILDGOOSE.favorite || {};

	var Favorite = {
		favoriteList : [],

		attatchEventToFavBtn : function(curNum, reqNum) {
			var reporterCards = document.querySelectorAll(".card");
			if (reporterCards.length != 0) {
				(curNum == undefined) ? curNum = reporterCards.length : true;
				(reqNum == undefined) ? reqNum = reporterCards.length : true;
				for (var i = curNum - reqNum ; i < curNum; i++) {
					var card = reporterCards[i];
					if (card == undefined) {
						continue;
					}
					var star = card.querySelector(".star");
					star.addEventListener("click", this.toggleFav, false);
					star.addEventListener("click", function(e) {
						Util.addClass(e.target, "pumping");
						setTimeout(function() {
							Util.removeClass(e.target, "pumping");
						}, 300)
					}, false);
				}				
			}
		},

		toggleFav : function(e) {
			var target = e.target;
			var card = target.parentElement.parentElement.parentElement;
			var reporterId = card.firstElementChild.dataset.reporter_id;
			var userId = getUserId();
			var url = "/api/v1/users/" + userId + "/favorites/?reporter_id="
					+ reporterId;


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
			var reporterCards = document.querySelectorAll(".card-section-identity");
			if (reporterCards.length != 0) {
				(curNum == undefined) ? curNum = reporterCards.length : true;
				(reqNum == undefined) ? reqNum = reporterCards.length : true;
				for (var i = curNum - reqNum ; i < curNum; i++) {
					var card = reporterCards[i];
					if (card == undefined) {
						continue;
					}
					var reporterId = card.dataset.reporter_id;
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
		var url = "/api/v1/users/" + userId + "/favorites/";
		Ajax.GET({
			"url" : url,
			"callback" : function(jsonStr) {
				console.log(url);
				var result = JSON.parse(jsonStr);
				var reporterCards = result["data"]["reporterCards"]
				for (var i=0; i<reporterCards.length; i++) {
					var card = reporterCards[i];
					Favorite.favoriteList.push(card["id"]);
				}
				// 불러온 목록 내부에 존재하는 favorite 업데이트
				// 인자가 없으면 모두!
				Favorite.updateFavs();
			}
		});
	}

	WILDGOOSE.ui.favorite = Favorite;

})();