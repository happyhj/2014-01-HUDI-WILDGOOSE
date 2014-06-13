(function(window){

'use strict';
var document = window.document;
var console = window.console;

var WILDGOOSE = window.WILDGOOSE || {};
WILDGOOSE.drag = WILDGOOSE.drag || {};
	
var values = {sourceEle : null, destEle : null};

		function _dragStart(e){
			var tar = e.target;
			tar.classList.add(values.movedClassName);
			
			e.dataTransfer.effectAllowed = 'move';
			if(tar.tagName == values.tagName){
				values.sourceEle = tar;
			}
		}
		
		function _dragOver(e){
			if(e.preventDefault){
				e.preventDefault();
			}
			e.dataTransfer.dropEffect = 'move'; //cursor 모양
			
			return false;
		}
		
		function _drop(e){
			var tar = e.target;
			if(e.stopPropagation){
				e.stopPropagation(); //browser redirecting 방지
			}
			
			while(true){
				if(tar.tagName == values.tagName){
					values.destEle = tar;
					break;
				}
				tar = tar.parentNode;
			}
			
			return false;
		}
		
		function _dragEnd(e){
			e.preventDefault();
			var tar = e.target;
			if(tar.className.indexOf(values.movedClassName) != -1){
				tar.classList.remove(values.movedClassName);
			}
			
			if(values.destEle != null &&values.sourceEle != null){
				values.destEle.insertAdjacentElement('afterend',values.sourceEle);
			}
		}
		
		function _addEvent(target){
			var children = target.children;
			[].forEach.call(children, function(child){
				child.draggable = "true";
				child.addEventListener('dragstart', function(e){ _dragStart(e);}, false);
				child.addEventListener('dragover', function(e){ _dragOver(e);}, false);
				child.addEventListener('drop', function(e){ _drop(e);}, false);
				child.addEventListener('dragend', function(e){ _dragEnd(e);}, false);
			});
		}
		
		function execute(args){
			values.target = args.body;
			values.tagName = args.tagName;
			values.movedClassName = args.movedClassName;
			
			_addEvent(args.body);
		}

		
		WILDGOOSE.drag = {
			exe : execute
		};

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}	   	

}(this));