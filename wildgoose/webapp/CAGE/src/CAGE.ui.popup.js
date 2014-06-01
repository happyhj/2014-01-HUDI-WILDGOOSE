(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var CAGE = window.CAGE || {};
	
	CAGE.ui = CAGE.ui || {};
	CAGE.ui.popup = CAGE.ui.popup || {};


	var Dom = CAGE.util.dom;
	var Ajax = CAGE.ajax;
    
	function ajaxPopup(config){
		var el = config.element;
		
		el.addEventListener("click",function(event){
			event.preventDefault();
			event.stopPropagation();
			var originalTarget;
			if(event.toElement) {
				originalTarget = event.toElement;
			} else if(event.originalTarget){
				originalTarget = event.originalTarget;
			}
			var sourceUrl = originalTarget.getAttribute("href");
			console.log(sourceUrl);

			Ajax.GET({
				url: sourceUrl,
				callback: ajaxCallback.bind(this)
			});
			
			function ajaxCallback(response) {

				var popupBg = document.createElement("div");
				Dom.addClass(popupBg, "popup-bg");
				Dom.addClass(popupBg, "popup-animation");
	
				var popupWrap = document.createElement("div");
				Dom.addClass(popupWrap, "popup-wrap");
				Dom.addClass(popupWrap, "popup-animation");
	
				
				var popupContainer = document.createElement("div");
				Dom.addClass(popupContainer, "popup-container");
				var popupContent = document.createElement("div");
				Dom.addClass(popupContent, "popup-content");
				
				var templateStr;
				if(config.templateLoader != undefined) {
					templateStr = config.templateLoader(response);
				} else {
					templateStr = response
				}
				popupContent.innerHTML = templateStr;	
				popupContainer.appendChild(popupContent);
				
				if(config.transitionEffect != undefined) {
					Dom.addClass(popupContent, config.transitionEffect + "-animation-dialog");
				}

				popupWrap.appendChild(popupContainer);
				
				// 오픈 비포 콜백 실행
				if(config.callbacks != undefined && config.callbacks.beforeopen != undefined) {
					config.callbacks.beforeopen();
				}				
				
				document.body.insertAdjacentHTML("afterbegin", popupWrap.outerHTML);			
				document.body.insertAdjacentHTML("afterbegin", popupBg.outerHTML);

				//data-role : close 인 엘리먼트에 팝업 닫기 리스너 연결해 줌 
				var closeBtn = document.querySelector(".popup-wrap button[data-role='close']");
				if(closeBtn)closeBtn.addEventListener("click", closePopup, false);				
					
				// 크롬에서 오작동하여 비동기코드로 뺌 ㅠㅠ	
	//			setTimeout(function(){
					//var utility = Util;	
					var popupWrapAnimation= document.querySelector(".popup-wrap.popup-animation");
/* 					Util.addClass(popupWrapAnimation, "popup-ready"); */
					var popupBgAnimation = document.querySelector(".popup-bg.popup-animation");
					
/* 					setTimeout(function(){ */

					popupWrapAnimation.offsetHeight;
/* 					popupWrapAnimation.style.opacity = 1; */
					popupBgAnimation.offsetHeight;
/* 					popupBgAnimation.style.transform="translateY(0px)"; */
					
					Dom.addClass(popupWrapAnimation, "popup-ready");
					Dom.addClass(popupBgAnimation, "popup-ready");
/* 					}, 100); */
					
					
					
					

					var afteropenCallbackRef = function(){
						// 오픈 엔드 콜백 실행
						if(config.callbacks != undefined && config.callbacks.afteropen != undefined) {
							var btn = document.querySelector(".form-container .button[type='button']")
							config.callbacks.afteropen(btn);
						}	
						popupWrapAnimation.removeEventListener("webkitTransitionEnd", afteropenCallbackRef, false);    
						
						popupBg = document.querySelector(".popup-bg");			
						popupWrap = document.querySelector(".popup-wrap");
						popupContainer = document.querySelector(".popup-container");
						popupContent = document.querySelector(".popup-content");
						
						
						var escClose = function(e) {
						  if (e.keyCode == 27) { 
							  closePopup();
							  document.removeEventListener("keyup", escClose, false);				
						  }
						};
						
						document.addEventListener("keyup", escClose, false);				
						
						popupContainer.addEventListener("click",function(event){
							if(event.target === popupContainer || event.target === popupContent) {				
								// 클로즈 비포 콜백 실행
								if(config.callbacks != undefined && config.callbacks.beforeclose != undefined) {
									config.callbacks.beforeclose();
								}	



/* 								popupBg.offsetHeight; */
							//	popupBg.style.opacity = null;

					
								// 역 애니메이션 걸기

								Dom.removeClass(popupBg, "popup-ready");
								Dom.removeClass(popupWrap, "popup-ready");		
								var aftercloseCallbackRef = function(){
									// 오픈 엔드 콜백 실행
									if(config.callbacks != undefined && config.callbacks.afterclose != undefined) {
										config.callbacks.afterclose();
									}	
						        };
							        				        
						        popupBg.addEventListener("transitionend", function(){
									aftercloseCallbackRef();
		
									var popupBg = document.querySelector(".popup-bg");			
									var popupWrap = document.querySelector(".popup-wrap");
							        if(popupWrap== null)
							        	console.log(popupWrap);
							        popupBg?document.body.removeChild(popupBg):undefined;
							        popupWrap?document.body.removeChild(popupWrap):undefined;	
						
						        }, false);
		
						        //transitionend webkitTransitionEnd
							}
						}, false);							
						
						
						
			        };
			        popupWrapAnimation.addEventListener("transitionend", afteropenCallbackRef, false);					
//				},100);





					
		
			}	
			
		},false);
		
		function openPopup() {
			el.click();			
		}
		function closePopup() {
			var popupContainer = document.querySelector(".popup-container");
			popupContainer.click();
		}
		
		return {
			element: el,
			open: openPopup,
			close: closePopup
		};
	}
	
	CAGE.ui.popup = {
		ajaxPopup: ajaxPopup
	};

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));