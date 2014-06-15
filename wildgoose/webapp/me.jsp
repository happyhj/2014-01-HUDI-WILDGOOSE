<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card-media.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/me-article.css" />
<style>
.card {
	margin: 10px 0;
	width: 100%;
	box-sizing:border-box;
	-moz-box-sizing:border-box; /* Firefox */
} 
.card {
	transition: all 0.5s ease-in;
	-webkit-transition: all 0.5s ease-in 0;
}
.card.blur {
	opacity: 0.4;
}
</style>
<title>Wildgoose</title>

<div class="wrap">
	<header class="header">
		<%@ include file ="/header.jsp" %>
	</header>
	<link type="text/css" rel="stylesheet" href="/stylesheet/me.css" />
	<%-- session 존재시 --%>
	<c:if test="${ not empty sessionScope.userId }"></c:if>
	<div class="container">
		<div class="dashboard dashboard-left">
			<div class="dashboard-header">
				<h2>나의 기자</h2>
			</div>
			<ul>
				<c:forEach var="reporter" items="${ requestScope.data.favorites }">
				<li class="card card-reporter">
					<%@ include file = "/jsp_templates/reporterCard.jsp" %>
				</li>
				</c:forEach>
				<li class="card card-reporter card-last" style="visibility:hidden;">
				</li>
			</ul>
		</div>
		<div class="content-main">
		
		<div class="content-main-header">
			<h2>타임라인</h2>
		</div>
		<div class="timeline-result">
			<ul>
				<c:forEach var="article" items="${ requestScope.data.articles }">
				<li class="card">
					<%@ include file = "/jsp_templates/articleCard.jsp" %>
				</li>
				</c:forEach>
			</ul>
			<div class="article-more">
				<button class="article-button-ajax">더보기</button>
				<div class="article-state article-state-hidden">
					<span class="state-article-curNum hidden">${ requestScope.data.articles.size() }</span>
					<span class="state-article-totalNum hidden">${ requestScope.data.totalNum }</span>
				</div>
			</div>
		</div>
		</div>
		<div class="dashboard dashboard-right">
			<div class="dashboard-header">
				<h2>추천기자</h2>
			</div>
			<ul>
				<c:if test="${ not empty sessionScope.userId }">
					<c:forEach var="reporter" items="${ requestScope.data.recommands }" varStatus="status">
					<li class="card card-reporter">
						<%@ include file = "/jsp_templates/reporterCard.jsp" %>
					</li>
					</c:forEach>
				</c:if>
			</ul>
		</div>
	</div>
	<footer class="footer"></footer>
</div>

<c:choose>
	<c:when test="${ initParam.debuggerMode eq 'on' }">
		<script type="text/javascript" src="/CAGE/src/CAGE.util.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.event.emitter.js"></script>
		<script type="text/javascript" src="/CAGE/src/CAGE.ajax.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/user/WILDGOOSE.user.js"></script>

		<script type="text/javascript" src="/scripts/WILDGOOSE/ui/WILDGOOSE.ui.favorite.js"></script>
		<script>
// 덮어쓰기
(function() {
	'use strict';
	var document = window.document;
	var console = window.console;

	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.favorite = {};
	
	// 의존성 선언
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
	var User = WILDGOOSE.user;
	
	function Star(element) {
		if (element == null) {
			console.error("No element found");
			return;
		}
		this.star = element;
		this.reporterId = this.getReporterId();
		this.show();
		this.attatchEvent();
	}
	
	Star.prototype = {
		constructor: Star,
		show: function() {
			Dom.removeClass(this.star, "invisible");
		},
		getReporterId: function(){
			var reporterId = this.star.parentElement.parentElement.dataset["reporter_id"];
			return parseInt(reporterId);
		},
		toggleStar: function(onoff) {
			var star = this.star;
			var card = star.parentElement.parentElement.parentElement;
			var leftList = document.querySelector(".dashboard-left");
			var rightList = document.querySelector(".dashboard-right");
			if (onoff == true) {
				Dom.addClass(star, "on");
				Dom.removeClass(star, "off");
				Dom.removeClass(card, "blur");
				if (Dom.isDescendant(rightList, card)) {
					console.log("right to left");
					
					var container = document.querySelector(".container");
					var containerPos = container.getBoundingClientRect();
					var curPos = card.getBoundingClientRect();
					card.style.position = "absolute";
					card.style.width = curPos.width + "px";
					card.style.height = curPos.height + "px";
					card.style.top = curPos.top - containerPos.top + "px";
					card.style.left = curPos.left - containerPos.left + "px";
					card.style.zIndex = 1;
					
					var lastCard = document.querySelector(".card-last");
					var lastCardPos = lastCard.getBoundingClientRect();
					var margin = 10;
					card.style.top = lastCardPos.top - containerPos.top - margin + "px";
					card.style.left = lastCardPos.left - containerPos.left + "px";
					
					setTimeout(function() {
						document.querySelector(".dashboard-left > ul").insertBefore(card.parentNode.removeChild(card), lastCard);
						card.style.position = "";
						card.style.width = "";
						card.style.height = "";
						card.style.top = "";
						card.style.left = "";
						card.style.zIndex = "";
					}, 500);
				}
			} else if (onoff == false) {
				Dom.removeClass(star, "on");
				Dom.addClass(star, "off");
				if (Dom.isDescendant(leftList, card)) {
					Dom.addClass(card, "blur");
				}
			}
		},

		clickStar : function(e) {
			var star = e.target;
			var card = star.parentElement.parentElement.parentElement;
			var reporterId = card.firstElementChild.dataset.reporter_id;
			var url = "/api/v1/users/" + Favorite.userId + "/favorites/?reporter_id="
					+ reporterId;

			if (Dom.hasClass(star, "on")) {
				Ajax.DELETE({
					"url" : url,
					"success" : function(responseObj) {
						this.toggleStar(false);
					}.bind(this),
					"failure" : function(responseObj) {
						console.log("Failure!");
					},
					"error" : function(responseObj) {
						console.log("Error!");
					}
				});
			} else {
				Ajax.POST({
					"url" : url,
					"success" : function(responseObj) {
						this.toggleStar(true);
					}.bind(this),
					"failure" : function(responseObj) {
						console.log("Failure!");
					},
					"error" : function(responseObj) {
						console.log("Error!");
					}
				});
			}
		},
		attatchEvent: function() {
			this.star.addEventListener("click", this.clickStar.bind(this), false);
			this.star.addEventListener("click", function(e) {
				Dom.addClass(e.target, "pumping");
				setTimeout(function() {
					Dom.removeClass(e.target, "pumping");
				}, 300)
			}, false);
		},
		updateStar: function() {
			var url = "/api/v1/users/:userId/favorites/:reporterId";
			url.replace(":userId", this.userId);
			url.replace(":reporterId", this.reporterId)
			Ajax.GET({
				"url" : url,
				"callback" : function(jsonStr) {
					var result = JSON.parse(jsonStr);
					if (result.data.bool == true) {
						this.toggleStar();
					}
				}.bind(this)
			});
		}

	}
	
	var Favorite = {
		starList: [],
		userFavorites: [],
		
		init: function() {
			var userId = User.getId();
			if (userId == "" || userId == undefined) {
				console.error("Not logined");
				return;
			}
			this.userId = userId;
			
			var favStars = document.querySelectorAll(".star");
			for (var i = 0; i < favStars.length; i++) {
				var favStar = favStars[i];
				var star = new Star(favStar);
				this.starList.push(star);
			}
			this.getStarListFromServer();
		},

		addCards: function(conatiner) {
			var userId = User.getId();
			if (userId == "" || userId == undefined) {
				return;
			}
			var stars = conatiner.querySelectorAll(".star");
			Array.prototype.forEach.call(stars, function(value){
				var star = new Star(value);
				if (this.userFavorites.indexOf(star.reporterId) >= 0) {
					star.toggleStar(true);
				}
				
			}.bind(this));
		},
		updateStars: function(stars) {
			for (var i = 0; i < stars.length; i++) {
				var star = stars[i];
				if (this.userFavorites.indexOf(star.reporterId) >= 0) {
					star.toggleStar(true);
				}
			}
		},
		getStarListFromServer: function() {
			var url = "/api/v1/users/:userId/favorites/";
			url = url.replace(":userId", this.userId);
			Ajax.GET({
				"url" : url,
				"callback" : function(jsonStr) {
					var result = JSON.parse(jsonStr);
					var reporterCards = result["data"]["reporterCards"];
					for (var i=0; i<reporterCards.length; i++) {
						var card = reporterCards[i];
						Favorite.userFavorites.push(card["id"]);
					}
					this.updateStars(this.starList);
				}.bind(this)
			});
		}
	};
	
	WILDGOOSE.ui.favorite = Favorite;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}	

})();

</script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/ui/WILDGOOSE.drag.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/more/WILDGOOSE.more.super_type.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/more/WILDGOOSE.more.article.js"></script>
		
		<!-- <script type="text/javascript" src="/scripts/APP/APP.page.favorite.js"></script> -->
		<!-- ????????? -->
		<script type="text/javascript" src="/scripts/APP/APP.page.me.js"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript" src="/CAGE/src/CAGE.min.js"></script>
		<script type="text/javascript" src="/scripts/WILDGOOSE/WILDGOOSE.min.js"></script>
		<script type="text/javascript" src="/scripts/APP/APP.min.js"></script>
	</c:otherwise>
</c:choose>


<script>
window.addEventListener("load", function(evt){
	APP.page.me.init();
}, false);
</script>