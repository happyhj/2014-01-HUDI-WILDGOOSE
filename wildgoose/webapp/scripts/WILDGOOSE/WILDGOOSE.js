(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};

	/*
	 * validation action
	 */
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
	var Validation = WILDGOOSE.ui.validation;
	
	function addValidationEvent() {
		var formContainer = document.querySelector(".form-container");
		for (var i = formContainer.length - 1; i >= 0; --i) {
			var input = formContainer[i];
			if (input.type == "email" || input.type == "password") {
				// blur event
	//			var dataCheck = input.getAttribute("data-check");
		//		if(dataCheck == "true") {
					input.addEventListener("blur", checkSignUpFrom, false);
			//	}
			}
		}
	};
	
	function checkSignUpFrom(e) {
		var inputEl = e.target;
		if (Validation.validCheck(inputEl)) {
			console.log("validation ok");
		} else {
			console.log("validation no");
		}
		
		// 각 input의 className을 확인하여 sumbit 버튼 활성화
		checkFormStatus(inputEl.parentNode);
	};
	
	
	/*
	 * form에 입력된 내용이 valid한지를 확인하여 회원가입 버튼 활성화 / 비활성화
	*/
	function checkFormStatus(form) {
		var btn = form.length-1;
		var flag = true;
		for (var i=btn-1; i>=0; --i) {
			if (!Dom.hasClass(form[i], "status-approved")) {
				flag = false;
				break;
			}
		}
	
		(flag) ? Dom.removeClass(form[btn], "hidden") : Dom.addClass(form[btn], "hidden");
		
	};
	
	/*
	 * 모두 작성된 정보를 Ajax POST로 서버에 전달
	 */
	function signUpAccount() {
		var url = "/api/v1/accounts/";
		var form = document.querySelector(".form-container");
		
		var email = escape(form[0].value)
		var password = escape(form[1].value);
		var payload = "email=" + email + "&password=" + SHA256(password);
		Ajax.POST({"url":url, "callback":showSignUpResult, "data":payload});
	//	domUtil.addClass(form, "isProgressing");
	
	};
	
	/*
	 * signUpAccount 실행 후
	 * 서버에서 전달된 결과값 확인
	 */
	function showSignUpResult(response) {
		var form = document.querySelector(".form-container");
		Dom.removeClass(form, "isProgressing");
		
		if (response == "success") {
			// close modal. and update login panel
			WILDGOOSE.ui.modal.closeModal(function(){
				updateTopbar(true);
			});
		}
	};
	
	
	function loginAccount() {
			
		var email = document.querySelector(".form-container input[name=email]").value;
		var password = document.querySelector(".form-container input[name=password]").value;
		
		var hashedPassword = SHA256(password);	
		var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
		
		var finalPassword = SHA256(hashedPassword+randomNumber);
		
		var url = "/api/v1/session/";
		var payload = "email="+email+"&password="+finalPassword;
		Ajax.POST({"url": url, "callback":function(response) {
			loginHandler(response);
		}, "data":payload});
	};
	
	function loginHandler(response){
		
		var form = document.querySelector(".form-container");
		Dom.removeClass(form, "isProgressing");
		
		if (JSON.parse(response).status == 200) {
			// close modal. and update login panel
			loginPopup.close();
			updateTopbar(true);
		}
	};
	
	
	WILDGOOSE.account = {
		loginHandler: loginHandler,
		loginAccount: showSignUpResult,
		showSignUpResult: showSignUpResult,
		signUpAccount: signUpAccount,
		checkFormStatus: checkFormStatus,
		checkSignUpFrom: checkSignUpFrom,
		addValidationEvent: addValidationEvent
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};

	// 의존성 선언 
	var Popup = CAGE.ui.popup;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
	var Account = WILDGOOSE.account;
	
	function init(){
		var joinBtn = document.querySelector("#join");
		
		var joinPopup = new Popup.ajaxPopup({
			element: joinBtn,
			templateUrl: "/api/v1/templates/account.html",
			templateLoader: function(AjaxResponse) {
				return JSON.parse(AjaxResponse).data.template;
			}
		});
		
		joinPopup.afteropen.add(function() {
			Account.addValidationEvent();
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", Account.signUpAccount, false);
		});
		
		var loginBtn = document.querySelector("#login");
		
		var loginPopup = new Popup.ajaxPopup({
			element: loginBtn,
			templateUrl: "/api/v1/templates/login.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				var randNum = JSON.parse(AjaxResponse).message;
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum
				}, templateStr);		
			}
		});
		
		loginPopup.afteropen.add(function() {
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", Account.loginAccount, false);
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
	}

	WILDGOOSE.header = {
		init: init
	}

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.auto_complement = WILDGOOSE.ui.auto_complement || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	
	var AutoComplement = {
		init : function(args) {
			this.row = {
				requestCount : args.requestNum,
				currentCount : 0
			};
			
			// ms
			this.interval = 100;

			this.is = {
				searching : false,
				hovering : false,
				pressedEnter : false,
				listing : false,
				highlighting : false
			};
					
			this.box = document.querySelector(args.searchBox);
			this.list = document.querySelector(args.container);
			this.cache = {
				searchedQuery : this.box.value,
				row : null,
				callbackRef : {
					notify : this.notify.bind(this),
					expired : this.expired.bind(this),
					listHandler : this.listHandler.bind(this),
					drawList : this.drawList.bind(this),
					setHovering : this.setHovering.bind(this),
					observeState : this.observeState.bind(this)
				}
			};
			this.box.addEventListener("focus", this.cache.callbackRef.notify);
			this.box.addEventListener("blur", this.cache.callbackRef.expired);
			
			this.box.focus();
		},

		expired : function(evt) {
			if (this.is.searching != false && this.is.hovering == false) {
				// list가 지워지지 않은 경우 제거
				if (this.is.listing == true) {
					this.removeList();
				}
				// value값 탐지 해제
				clearInterval(this.is.searching);
				this.is.searching = false;
			}
		},

		notify : function(evt) {
			if (this.is.searching == false) {
				this.is.searching = setInterval(this.cache.callbackRef.observeState, this.interval);
			}
		},
		
		observeState : function() {
			var curQuery = this.box.value;
			// curQuery 값이 변했을 경우만 처리
			if (curQuery != this.cache.searchedQuery) {
				console.log("detection: " + curQuery);
				if (curQuery == "") {
					// list 종료 로직.
					this.removeList();
				}
				else {
					this.search(curQuery);
				}
			}
		},
		
		search : function(searched) {
			this.cache.searchedQuery = searched;
			var url = "/api/v1/search?autocomplete=true&q=" + searched + "&how_many=" + this.row.requestCount;
			Ajax.GET({"url":url, "callback":this.cache.callbackRef.drawList});
		},
		
		drawList : function(response) {
			this.is.pressedEnter = false;
			var data = JSON.parse(response).data.reporters;
			console.log(data)
			if (data === undefined || data.length == 0) {
				return;
			}
			
			// cache.row 초기화
			this.cache.row = null;
			// 전달받은 row개수를 기록
			this.row.currentCount = data.length;
			var li_template = "";
			for (var i = 0; i < this.row.currentCount; i++) {
				li_template += "<li><div>" + data[i]["name"] + "</div></li>";
			}
			this.list.innerHTML = li_template;
			this.addList();
		},
		
		removeList : function() {
			this.is.listing = false;
			this.row.currentCount = 0;
			this.cache.row = null;
			this.list.style.display = "none";
			this.removeMouseRader(this.list);
			this.box.removeEventListener("keydown", this.cache.callbackRef.listHandler, false);
			this.list.removeEventListener("click", this.cache.callbackRef.listHandler, false);
			this.list.removeEventListener("mousemove", this.cache.callbackRef.listHandler, false);
			
			this.is.hovering = false;
		},
		addList : function() {
			this.is.listing = true;
			this.list.style.display = "inline-block";
			this.addMouseRader(this.list);
			this.box.addEventListener("keydown", this.cache.callbackRef.listHandler, false);
			this.list.addEventListener("click", this.cache.callbackRef.listHandler, false);
			this.list.addEventListener("mousemove", this.cache.callbackRef.listHandler, false);
		},
		addMouseRader : function(el) {
			el.addEventListener("mouseover", this.cache.callbackRef.setHovering, false);
			el.addEventListener("mouseout", this.cache.callbackRef.setHovering, false);
		},
		removeMouseRader : function(el) {
			el.removeEventListener("mouseover", this.cache.callbackRef.setHovering, false);
			el.removeEventListener("mouseout", this.cache.callbackRef.setHovering, false);
		},
		setHovering : function(evt) {
			if (evt.type == "mouseover") {
				this.is.hovering = true;
			}
			else if (evt.type == "mouseout") {
				this.is.hovering = false;
			}
		},
		listHandler : function(evt) {
			var targetEl = evt.target;
			// keydown event
			if (evt.type == "keydown" && targetEl == this.box) {
				console.log("keydown");
				var keyID = evt.keyCode;
				// up, down key
				if (keyID == 38 || keyID == 40) {
					// 위쪽화살표를 움직였을 때 포인터가 왼쪽으로 가는걸 방지하기 위해 기본 이벤트 해제
					evt.preventDefault();
					this.highlightRow(39 - keyID);
				}
				// enter key
				else if (keyID == 13) {
					// 엔터를 눌렀을 때 기본 이벤트가 실행되는걸 방지하기 위해 해제
					evt.preventDefault();
					this.selectEl(this.list.children[this.cache.row]);
				}
			}
			
			// click event
			else if (evt.type == "click" && targetEl.parentNode.parentNode == this.list && this.is.hovering == true) {
				this.is.hovering = false;
				this.selectEl(evt.target);
			}
			
			// mousemove event
			else if (evt.type == "mousemove") {
				if (this.is.highlighting == true) {
					this.highlightOut(this.cache.row);
				}
			}
		},
		
		selectEl : function(el) {
			var text = null;
			// 선택된 el이 없는 경우 검색창에 입력된 query가 됨.
			if (el === undefined) { text = this.box.value; }
			else { text = el.innerText; }
			
			text = this.removeNewline(text);
			this.box.value = text;
			
			// ajax통신이 일어나지 못하도록 캐싱된 마지막 검색 query를 바꾼다.
			this.cache.searchedQuery = text;
			this.removeList();
			this.box.form.submit();
		},
		
		removeNewline : function(text) {
			var index = text.lastIndexOf("\n");
			if (index == -1) return text;
			return text.substring(0, index);
		},
		
		highlightRow : function(change) {
			// 처음 입력시
			if (this.cache.row == null) {
				this.cache.row = 0;
				change = 0;
			}
			
			var cacheRow = this.cache.row;
			var currentRow = cacheRow - change;
			
			// ajax에서 응답받은 현재 row의 수와 비교함.
			if (currentRow >= this.row.currentCount) { currentRow = 0; }
			else if (currentRow < 0) { currentRow = this.row.currentCount-1; }

			this.highlightOut(cacheRow);
			this.highlightIn(currentRow);
			this.cache.row = currentRow;
		},

		highlightIn : function(rowNum) {
			this.is.highlighting = true;
			this.list.children[rowNum].className = "highlight";
		},
		highlightOut : function(rowNum) {
			if (rowNum !== null) {
				this.is.highlighting = false;
				this.list.children[rowNum].className = "";
			}
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.ui.auto_complement = AutoComplement;
})();

window.addEventListener("load", function(evt){
	var AutoComplement = WILDGOOSE.ui.auto_complement;
	AutoComplement.init({searchBox: "#query-entry", container: ".search .auto-completion-list", requestNum: 7});
}, false);(function() {
	'use strict';
	var document = window.document;
	var console = window.console;

	// 의존성 선언
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
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
						Dom.addClass(e.target, "pumping");
						setTimeout(function() {
							Dom.removeClass(e.target, "pumping");
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


			if (Dom.hasClass(target, "on")) {
				Ajax.DELETE({
					"url" : url,
					"callback" : function(data) {
						var data = JSON.parse(data);
						if (data.status == 200) {
							Dom.removeClass(target, "on");
							Dom.addClass(target, "off");
							Dom.addClass(card, "blur");
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
							Dom.addClass(target, "on");
							Dom.removeClass(target, "off");
							Dom.removeClass(card, "blur");

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

})();(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.search_more = WILDGOOSE.ui.search_more || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Template = CAGE.util.template;
	var Fav = WILDGOOSE.ui.favorite;
	
	var SearchMore = {
		_more: function(evt) {
			// click evt
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			var curNum = document.querySelector(".search-more .state-search-curNum").innerText;
			// search
			var url = "/api/v1/search?q=" + searchQuery + "&start_item=" + curNum + "&how_many=" + this.requestNum;
			Ajax.GET({"url":url, "callback":this._responseHandler.bind(this)});
		},
		
		_responseHandler: function(rawD) {
			var userId = null;
			var reporters = JSON.parse(rawD)["data"]["reporters"];
			var isLogined = ((userId = this._getUserId()) != null)? true : false;
			
			// response data가 존재할 경우만 실행
			if (reporters.length != 0) {	
				var cards = this._makeReporterCards(isLogined, reporters);
				this._attachRecievedData(cards);
				var metaData = this._updateMetaData(cards.length);
				this._selectStatusOfSearchMoreBtn(metaData.curNum);
				debugger;
				Fav.updateFavs(metaData.curNum, this.requestNum);
				Fav.attatchEventToFavBtn(metaData.curNum, this.requestNum);
			}
		},
		
		// 현재 card의 개수를 업데이트
		_updateMetaData: function(updatedNum) {
			var curNumDiv = document.querySelector(".search-more .state-search-curNum");
			var curNum = parseInt(curNumDiv.innerText) + updatedNum;
			curNumDiv.innerText = curNum;
			
			return {"curNum": curNum};
		},
		
		// 더보기 버튼을 보여줄지 말지를 결정
		_selectStatusOfSearchMoreBtn: function(curNum) {
			var searchMore = document.querySelector(".search-more");
			var totalNumDiv = document.querySelector(".search-more .state-search-totalNum");
			if (totalNumDiv === null) {
				searchMore.setAttribute("style", "display: none;");
				return;
			}
			
			var totalNum = parseInt(totalNumDiv.innerText);
			if (totalNum <= curNum) {
				searchMore.setAttribute("style", "display: none;");
			}
		},
		
		// card template에 데이터를 담은 template array를 반환
		_makeReporterCards: function(isLogined, reporters) {
			var templateCompiler = Template.getCompiler();
			var className = "card card-reporter";
			var reporterNum = reporters.length;
			var cards = [];
		
			// card template을 cards array에 담음
			for (var i=0; i<reporterNum; i++) {
				var cardData = reporters[i];
				
				/*
				 * 로그인 상태에 따른 별을 보여주는 로직 추가 필요
				 * 
				if logined
				favorited reporter card's star must be turn on
				and stars have their own event-handler
				else
				stars have to be invisible
				 */
				if(isLogined){
//					var star = card.querySelector(".star");
//					console.log(star);
//					Util.removeClass(star, "invisible");
//					star.addEventListener("click", Fav.toggleFav, false);
				}
				var newLi = '<li class="' + className + '">' + templateCompiler(cardData, this.template) + '</li>';
				cards.push(newLi);
			}
			
			return cards;
		},
		
		_getUserId: function() {
			var userId = document.getElementById("userId");
			if(userId != null){
				userId = document.getElementById("userId").getAttribute('email');
			}
			return userId;
		},
		
		_attachRecievedData: function(cards) {
			this.searchResult.innerHTML += cards.join("");
		},
		
		init: function(args) {
			this.searchMoreBtn = document.querySelector(args.button);
			this.searchResult = document.querySelector(args.container);
			this.requestNum = args.requestNum;
			this.template = Template.get({"url":args.templateUrl});
			
			// 더보기 버튼 클릭이벤트 설정
			if (this.searchMoreBtn != null) {
				this.searchMoreBtn.addEventListener("click", this._more.bind(this), false);
				this._selectStatusOfSearchMoreBtn();
			}
		}	
	};
	
	// 공개 메서드 노출
	WILDGOOSE.ui.search_more = SearchMore;
})();

window.addEventListener("load", function(evt){
	var SearchMore = WILDGOOSE.ui.search_more;
	SearchMore.init({button: ".search-more", container: ".search-result > ul", templateUrl: "/api/v1/templates/reporterCard.html", requestNum: 24});
}, false);

(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.validation = WILDGOOSE.ui.validation || {};

	var Ajax = CAGE.ajax
	var DomUtil = CAGE.util.dom;
	
	var validation_logics = {
		email : {
			sequence : [ "required", "format", "usable" ],
			required : [ /.+/, "email을 입력해주세요" ],
			format : [ /^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/, "email형식을 지켜주세요" ],
			usable : [ function(inputEl, callback) {
				existInServer(inputEl, callback);
			}, "이미 등록된 email입니다" ]
		},
		password : {
			sequence : [ "required", "letter", "size", "ampleNumber", "ampleLetter" ],
			required : [ /.+/, "비밀번호를 입력해주세요" ],
			letter : [
					/[a-zA-Z0-9\`\~\!\@\#\$\%\^\&|*\(\)\-\_\=\=\+\\\|\,\.\<\>\/\?\[\]\{\}\;\:\'\"]/,
					"숫자, 영문자 대소문자, 특수문자만 사용해주세요" ],
			size : [ /^.{8,15}$/, "8~15자 사이로 입력해주세요" ],
			ampleNumber : [ /(.*\d{1}.*){4,}/, "숫자는 4자리 이상 포함되어야 합니다" ],
			ampleLetter : [ /(.*\D{1}.*){4,}/, "문자는 4자리 이상 포함되어야 합니다" ]
		},
		confirm : {
			sequence : [ "required", "equal" ],
			required : [ /.+/, "다시 입력해주세요" ],
			equal : [ function(inputEl, callback) {
				ckeckEquality(inputEl, callback);
			}, "다시 확인해주세요" ]
		}		
	};	

	function ckeckEquality(inputEl, callback) {
		var parent = inputEl.parentNode;
		var password = document.querySelector("." + parent.className
				+ " input[name=password]");
		
		callback(inputEl.value == password.value);
	}

	function existInServer(inputEl, callback) {
		var url = "api/v1/accounts/?email=" + inputEl.value;
		Ajax.GET({"url":url, "callback":function(response) {
			console.log(response);
			var validity = (JSON.parse(response).status===200)?true:false;
			var isAjax = true;
			callback(validity, isAjax);
		}});
		Util.addClass(inputEl, "isProgressing");
	}
	
	
	function validCheck(inputEl) {
		var fieldName = inputEl.name;
		var fieldValue = inputEl.value;
		var checking_sequence = validation_logics[fieldName]["sequence"];

		for ( var i = 0; i<checking_sequence.length; ++i) {
			var cur_sequence = checking_sequence[i];
			var checking_logic = validation_logics[fieldName][cur_sequence];
			var alert_message = checking_logic[1];
			
			if (checking_logic[0] instanceof RegExp) {
				console.log("RegExp");
				console.log(checking_logic[0].test(fieldValue));
				if (!checking_logic[0].test(fieldValue)) {
					warn(inputEl, alert_message);
					invalidStyle(inputEl);
					return false;
				}
			} else if (checking_logic[0] instanceof Function) {
				console.log("Function");
				var valid_state = true; 
				checking_logic[0](inputEl, function(validity, isAjax) {
					if (isAjax) {
						Util.removeClass(inputEl, "isProgressing");
					}
					if (!validity) {
						warn(inputEl, alert_message);
						invalidStyle(inputEl);
						valid_state = false;
						return false;
					}
				});
				if (!valid_state) {
					return false;
				}
			}
		}
		unwarn(inputEl);
		validStyle(inputEl);
		return true;
	}
	
	
	/*
	 * 상태에 따른 변경될 style을 모음 
	 */
	function validStyle(inputEl) {
		DomUtil.removeClass(inputEl, "status-denied");
		DomUtil.removeClass(inputEl, "isInvalid");
		DomUtil.addClass(inputEl, "status-approved");
		DomUtil.addClass(inputEl, "isValid");
	}
	
	function invalidStyle(inputEl) {
		DomUtil.removeClass(inputEl, "status-approved");
		DomUtil.removeClass(inputEl, "isValid");
		DomUtil.addClass(inputEl, "status-denied");
		DomUtil.addClass(inputEl, "isInvalid");
	}

	/*
	 * 사용에게 메시지를 전달하기 위한 함수 
	 */
	function warn(inputEl, warningMsg) {
		var name = inputEl.name;
		var target = document.querySelector(".form-container .msg-" + name);

		target.innerText = warningMsg;
	}

	function unwarn(inputEl) {
		var name = inputEl.name;
		var target = document.querySelector(".form-container .msg-" + name);

		target.innerText = "";
	}
	
	WILDGOOSE.ui.validation = {
		validCheck: validCheck
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
