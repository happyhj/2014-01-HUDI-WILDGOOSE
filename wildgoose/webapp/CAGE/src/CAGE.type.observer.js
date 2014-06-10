(function(window) {
	'use strict';
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var CAGE = window.CAGE || {};
	CAGE.type = CAGE.type || {};
	CAGE.type.observer = CAGE.type.observer || {};
	
	
	var Dom = CAGE.util.dom;

	function Observer(args) {
		this.interval = 100;
		this.status = null;
		this.observerEl = null;
		this.targetElObj = null; 
		this.usable = false;
		this.infoObj = {};
		
		this._observer(args);
	};
	
	Observer.prototype = {
		constructor: "Observer",
		_observer: function(args) {
			if (args !== undefined) {
				if (args.interval !== undefined) {
					this.interval = args.interval;
				}
				this.observerEl = args.observerEl;
				this.targetElObj = args.targetElObj;
				this.usable = (this.observerEl === null || this.targetElObj === null)? false: true;
			}
		},
		activate: function() {
			if (this.usable === true && this.status === null) {
				this.status = setInterval(this._handler.bind(this), this.interval);
			}
		},
		deactivate: function() {
			clearInterval(this.status);
			this.status = null;
		},
		_handler: function() {
			this._trigger(this._observe());
		},
		_trigger: function(flag) {
			var observeEvt = new CustomEvent("observe");
			this.observerEl.dispatchEvent(observeEvt);
		},
		_observe: function() {
			// interface
			return true;
		}
	};


	
	// 공개 메서드 노출
	CAGE.type.observer = Observer;
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = CAGE;
		// browser export
	} else {
		window.CAGE = CAGE;
	}    	

}(this));
