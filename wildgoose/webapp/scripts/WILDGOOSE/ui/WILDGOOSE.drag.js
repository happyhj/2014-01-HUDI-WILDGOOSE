/**
 * 외부에서는 WILDGOOSE.drag.exe(args)로 드래스 함수에 접근할 수 있다.
 * 전달해야 하는 인자 args는 dictionary로 인자는 3개이다. 
 * 
 * 1. target: 드래그 되어야 하는 DOM들을 담고있는 DOM 		ex.ul
 * 2. tagName: 드래가 되는 DOM의 태그이름 				ex."LI"(태그이름은 모두 대문자임을 주의)
 * 3. movedClassName: 드래그 중인 DOM의 클래스명. 드래그 중의 CSS를 위한 것. 	ex. 드래그 중인 DOM은 투명도를 조절 함  
 * 
 * 외부에서는 WILDGOOSE.drag.localSet(args)으로 localSet함수에 접근할 수 있다.
 * 
 * 
 * 
 * 이 함수는 드래그 '될' 노드들의 유일한 값(id또는 특정 어트리뷰트의 값 등)을 세팅하는 함수로 로컬스토리지에 저장하는 함수를 반환한다.
 *  
 * 
 * */

(function(window){

'use strict';
var document = window.document;
var console = window.console;

var WILDGOOSE = window.WILDGOOSE || {};
WILDGOOSE.drag = WILDGOOSE.drag || {};

var User = WILDGOOSE.user;
	
var values = {sourceEle : null, destEle : null};
var forLocal = {};
//nameSpace
drag = {};
drag.localStore = {
	localSet: function(args){
		forLocal.attribute = args.attribute;
		forLocal.emptyNode = args.emptyNode;
		values.StorageName = User.getId() + "_" +args.localStorageName;
	},
	_localSave: function(){
		var testString ="";
		var child = values.target.children;
		for(var i=0; i < child.length-forLocal.emptyNode; i++){ // 빈 노드의 갯수만큼 뺌
			var ident = "child["+i+"]." + forLocal.attribute;
			var result;
			result = eval(ident);
			testString = testString+result+" ";
		}
		localStorage.setItem(values.StorageName, testString);
	},	
	_myAuthorOrder: function(){
		if (values.target !== null) {
			var child = values.target.children;
			if(localStorage.getItem(values.StorageName) == undefined) return;
			var numLi = localStorage.getItem(values.StorageName).split(" ");
			numLi.pop(); // 빈 값("") 때문에 -1을 해 줌
			
			for(var j=0; j<numLi.length; j++){ 
				for(var i=0; i<child.length-forLocal.emptyNode; i++){ // 빈 노드의 갯수만큼 뺌
					if(child[i].firstElementChild != null){
						var ident = "child["+i+"]." + forLocal.attribute;
						var result;
						result = eval(ident);
						if(result==numLi[j]){
							values.target.appendChild(child[i]);
						}
					}
				}
			}
			
			var lastCard = document.querySelector('.card-last');
			values.target.appendChild(lastCard);
		}
	}
}

drag.dragfunc = {
		_dragStart: function(e){
			var tar = e.target;
			tar.classList.add(values.movedClassName);
			
			e.dataTransfer.effectAllowed = 'move';
			if(tar.tagName == values.tagName){
				values.sourceEle = tar;
			}
		},
		_dragOver: function(e){
			if(e.preventDefault){
				e.preventDefault();
			}
			e.dataTransfer.dropEffect = 'move'; //cursor 모양
			
			return false;
		},
		_drop: function(e){
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
		},
		_dragEnd: function(e){
			e.preventDefault();
			var tar = e.target;
			if(tar.className.indexOf(values.movedClassName) != -1){
				tar.classList.remove(values.movedClassName);
			}
			
			if(values.destEle != null &&values.sourceEle != null){
				values.destEle.insertAdjacentElement('afterend',values.sourceEle);
			}
			
			this._localSave();
		}
}

// this & binding
drag.action = {
	_addEvent: function(target){
		if (target !== null) {
			var children = target.children;
			[].forEach.call(children, function(child){
				child.draggable = "true";
				child.addEventListener('dragstart', function(e){ drag.dragfunc._dragStart(e);}, false);
				child.addEventListener('dragover', function(e){ drag.dragfunc._dragOver(e);}, false);
				child.addEventListener('drop', function(e){ drag.dragfunc._drop(e);}, false);
				child.addEventListener('dragend', function(e){ drag.dragfunc._dragEnd.call(this, e);}.bind(drag.localStore), false);
			});
		}
	},
	execute: function(args){
		values.target = args.body;
		values.tagName = args.tagName;
		values.movedClassName = args.movedClassName;
		
		drag.action._addEvent(args.body);
	}
}

//DOMContentLoaded를 사용하여 성능개선. 버벅거리지 않고 DOM위치 수정이 바로 적용됨
document.addEventListener("DOMContentLoaded", function(e){
	drag.localStore._myAuthorOrder();
})

//hoisting
var drag;

		WILDGOOSE.drag = {
			localSet : drag.localStore.localSet,
			exe : drag.action.execute
		};

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}	   	

}(this));