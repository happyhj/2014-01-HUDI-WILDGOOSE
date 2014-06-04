(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.modal = WILDGOOSE.ui.modal || {};

	function createMask() {
		var mask = document.createElement("div");
		mask.id = "mask";
		mask.setAttribute("class", "mask mask-on");
		return mask;
	}
	function createModalWindow(url) {
		var modal = document.createElement("div");
		modal.setAttribute("class", "modal");
		return modal;
	}
	function putHtmlInModal(htmlDoc) {
		var modal = document.querySelector(".modal");
		modal.innerHTML = htmlDoc;
	}
	function removeModal(event, closeCallback) {
		if (event.target == this) {
			var mask = event.target;
			mask.parentNode.removeChild(mask);
		}
		closeCallback();
	}
	function closeModal(closeCallback) {
		var mask = document.querySelector(".mask")
		mask.parentNode.removeChild(mask);
		closeCallback();
	}
	function openModalWindow(url, onloadCallback) {
		var mask = createMask();
		var modal = createModalWindow(url);
		mask.addEventListener("click", this.removeModal, false);
		mask.appendChild(modal);
			
		Ajax.GET(url, (function(htmlDoc) {
			putHtmlInModal(htmlDoc);
			console.log(onloadCallback);
			var btn = document.querySelector(".form-container .button[type='button']")
			onloadCallback(btn);
		}).bind(this));
		document.body.appendChild(mask);
	}
	
	WILDGOOSE.ui.modal = {
		closeModal: closeModal,
		removeModal: removeModal,
		openModalWindow: openModalWindow
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));


