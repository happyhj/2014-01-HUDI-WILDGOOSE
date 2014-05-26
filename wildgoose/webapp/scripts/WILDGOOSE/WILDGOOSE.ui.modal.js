(function() {
	'use strict';
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.modal = WILDGOOSE.ui.modal || {};
	
	function Modal(url) {
		// private properties
		var a = 0;
		
		
		// public properties
		this.url = url;
		this.submitValue = "submit";
		this.cancelValue = "cancel";
		
		this.onLoad = function() {
			alert(1);
		}
		this.onSubmit = function() {
			alert(2);
		}
		this.onCancel = function() {
			alert(3);
		}
	}
	
	// public methods
	Modal.prototype = {
		
		openModal : function() {
			var mask = this._createMask();
			var modal = this._createModalWindow();
			var modalFooter = this._createModalFooter();
			mask.addEventListener("click", this.removeModal, false);
			mask.appendChild(modal);
			
			WILDGOOSE.ajax.GET({"url":this.url, "callback":(function(htmlDoc) {
				modal.innerHTML = htmlDoc;
				modal.appendChild(modalFooter);
				this.onLoad();
				
			}).bind(this)});
			
			document.body.appendChild(mask);
		},
		closeModal : function() {
			
		}, 
		// private methods
		_createMask : function() {
			var mask = document.createElement("div");
			mask.id = "mask";
			mask.setAttribute("class", "mask mask-on");
			return mask;
		},
		_createModalWindow : function() {
			var modal = document.createElement("div");
			modal.setAttribute("class", "modal");
			return modal;
		},
		_createModalFooter : function() {
			var footer = document.createElement("div");
			var submitBtn = document.createElement("input");
			var cancelBtn = document.createElement("input");
			footer.setAttribute("class", "modal-footer");
			submitBtn.setAttribute("type", "button");
			cancelBtn.setAttribute("type", "button");
			submitBtn.setAttribute("id", "submit");
			cancelBtn.setAttribute("id", "cancel");
			console.log(this) // OMG Window
			submitBtn.addEventListener("click", this.onSubmit, false);
			cancelBtn.addEventListener("click", this.onCancel, false);
			footer.appendChild(submitBtn);
			footer.appendChild(cancelBtn);
			return footer;
		}
	}
	
	// 공개 메서드 노출
	WILDGOOSE.ui.modal = Modal;
    	
})();
