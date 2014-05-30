(function() {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.favorite = WILDGOOSE.ui.favorite || {};
	
	var Ajax = CAGE.ajax;
	if(document.getElementById('userId') != null) {
		attatchEventToFavBtn();
	}
	
	function attatchEventToFavBtn() {
		var stars = document.querySelectorAll(".star");
		for (var i = 0; i < stars.length; i++) {
			var star = stars[i];
			Util.removeClass(star, "invisible");
			star.addEventListener("click", toggleFav, false);
			document.querySelector(".star").addEventListener("click", function(e) {
				Util.addClass(e.target, "pumping");
				setTimeout(function() {
					Util.removeClass(e.target, "pumping");
				}, 300)
			}, false)
		}
	}

	function toggleFav(e) {
		var target = e.target;
		var card = target.parentElement.parentElement;
		var anchor = card.querySelector('a');
		var reporterId = anchor.getAttribute("href").split("reporters/")[1];
		var userId = document.getElementById("userId").getAttribute('email');
		var url = "api/v1/users/"+userId+"/favorites/?reporter_id=" + reporterId;
		
		var card = target.parentNode.parentNode;
		
		if (Util.hasClass(target, "on")) {
			Ajax.DELETE({"url":url, "callback":function(data) {
				var data = JSON.parse(data);
				if (data.status == 200) {
					Util.removeClass(target, "on");
					Util.addClass(target, "off");
					Util.addClass(card, "blur");
				} else {
					// react fail
				}
			}});
		} else {
			Ajax.POST({"url":url, "callback":function(data) {
				var data = JSON.parse(data);
				if (data.status == 200) {
					Util.addClass(target, "on");
					Util.removeClass(target, "off");
					Util.removeClass(card, "blur");
					
				} else {
					// react fail
				}
			}});
		}
	}

	function getFavs() {
	   function getCookie(cName) {
	          cName = cName + '=';
	          var cookieData = document.cookie;
	          var start = cookieData.indexOf(cName);
	          var cValue = '';
	          if(start != -1){
	               start += cName.length;
	               var end = cookieData.indexOf(';', start);
	               if(end == -1)end = cookieData.length;
	               cValue = cookieData.substring(start, end);
	          }
	          return unescape(cValue);
	     }
	   
		var url = "api/v1/users/"+getCookie("userId")+"/favorites/";
		Ajax.GET({"url":url, "callback":function(jsonStr) {
			var result = JSON.parse(jsonStr);
			favs = result["data"];
			var reporterCards = document.querySelectorAll(".card-reporter");
			updateFavs(reporterCards);
		}});
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
				card.querySelector(".star").className = "star on";
			}
		}
	}
	
	WILDGOOSE.ui.fovorite = {
			attatchEventToFavBtn: attatchEventToFavBtn,
			toggleFav : toggleFav
	}
//	WILDGOOSE.ui.fovorite.attatchEventToFavBtn();
})();