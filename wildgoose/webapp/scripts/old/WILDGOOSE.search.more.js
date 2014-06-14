(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.search = WILDGOOSE.search || {};
	WILDGOOSE.search.more = WILDGOOSE.search.more || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Template = CAGE.util.template;
	var Fav = WILDGOOSE.ui.favorite;
	var User = WILDGOOSE.user;
	
	var More = {
		init: function(args) {
			this.searchMoreBtn = args.more.button;
			this.searchResult = args.container;
			this.requestNum = args.requestNum;
			this.template = args.template;
			this.isLogined = User.isLogined();
			this.tempDiv = document.createElement("div");
			this.metaData = {
				curNum : args.more.curNum,
				totalNum: args.more.totalNum,
				updatedNum: 0,
				searchQuery: args.searchQuery
			};
			
			
			this.cache = {
				exec: this.exec.bind(this),
				scrollingHandler: this._scrollingHandler.bind(this),
				responseHandler: this._responseHandler.bind(this)
			}
			
			// 더보기 버튼 클릭이벤트 설정
			if (this.searchMoreBtn != null) {
				this.searchMoreBtn.addEventListener("click", this.cache.exec, false);
				this.searchMoreBtn.addEventListener("approachPageEnd", this.cache.exec, false);
				this._updateUI();
			}
		},
		
		exec: function(evt) {
			// search
			var url = "/api/v1/search?q=" + this.metaData.searchQuery + "&start_item=" + this.metaData.curNum + "&how_many=" + this.requestNum;
			Ajax.GET({
				"url": url,
				"callback": this.cache.responseHandler
			});
		},
		
		_addPageEndEvent: function() {
			window.addEventListener("scroll", this.cache.scrollingHandler, false);
		},
		_removePageEndEvent: function() {
			window.removeEventListener("scroll", this.cache.scrollingHandler, false);
		},
		_scrollingHandler: function(evt) {
			// 뷰포트의 크기
			var footer = document.querySelector(".footer");
			var viewportHeight = window.innerHeight;
			var footerHeight = parseInt(window.getComputedStyle(footer, null).height);
			var footerTopPos = footer.getBoundingClientRect().bottom - footerHeight;
			
			if (viewportHeight - 15 > footerTopPos) {
				var approachPageEndEvt = new CustomEvent("approachPageEnd");
				this.searchMoreBtn.dispatchEvent(approachPageEndEvt);
			}
		},
		_responseHandler: function(rawD) {
			var reporters = JSON.parse(rawD)["data"]["reporters"];
			
			// response data가 존재할 경우만 실행
			if (reporters.length != 0) {	
				var cards = this._makeReporterCards(this.isLogined, reporters);
				for (var i=0; i<cards.length; i++) {
					this.searchResult.appendChild(cards[i]);
				}

				this._updateMetaData(cards.length);
				this._updateUI();
				
				Fav.addCards(cards);
			}
		},
		
		// 현재 card의 개수를 업데이트
		_updateMetaData: function(updatedNum) {
			this.metaData.updatedNum = updatedNum;
			this.metaData.curNum += updatedNum;
		},
		
		// 더보기 버튼을 보여줄지 말지를 결정
		_updateUI: function() {
			var totalNum = this.metaData.totalNum;
			var curNum = this.metaData.curNum;
			this._addPageEndEvent();
			
			if (totalNum == 0 || totalNum <= curNum) {
				this._removePageEndEvent();
				this.searchMoreBtn.setAttribute("style", "display: none;");
			}
		},
		
		// card template에 데이터를 담은 template array를 반환
		_makeReporterCards: function(isLogined, reporters) {
			var templateCompiler = Template.getCompiler();
			var tempDiv = this.tempDiv;
			var className = "card card-reporter";
			var reporterNum = reporters.length;
			var cards = [];
		
			// card template을 cards array에 담음
			for (var i=0; i<reporterNum; i++) {
				var cardData = reporters[i];
				var newLiStr = '<li class="' + className + '">' + templateCompiler(cardData, this.template) + '</li>';
				tempDiv.innerHTML = newLiStr;
				cards.push(tempDiv.firstElementChild);
			}
			
			return cards;
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.search.more = More;
})();
