(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.search = WILDGOOSE.search || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Template = CAGE.util.template;
	var More = WILDGOOSE.search.more;
	var AutoComplement = WILDGOOSE.search.auto_complement;
	var Submit = WILDGOOSE.search.submit;
	
	var Search = {
		init: function(args) {
			// search
			var search = args.search;
			if (search !== undefined) {
				this.form = {
					box: document.querySelector(search.box),
					container: document.querySelector(search.container)
				};
				
				this.search = {
					submit: document.querySelector(search.submit),
					requestNum: search.requestNum,
					templateURL: search.templateURL,
					template: Template.get({"url":search.templateURL})
				};
				
				// initialize submit button
				var submitArgs = {
					box: this.form.box,
					submit: this.search.submit
				};
				Submit.init(submitArgs);
				
			}
			
			// initialize auto completion list
			var autocompletion = args.autocompletion;
			if (autocompletion !== undefined) {
				this.list = {
					element: document.querySelector(args.autocompletion.list),
					requestNum: autocompletion.requestNum,
					interval: 100
				};
				AutoComplement.init({searchBox: this.form.box, list: this.list});
			}
			
			// initialize more button
			var more = args.more;
			if (more !== undefined) {
				this.more = {
					button: document.querySelector(more.button)
				}
				More.init({button: this.more.button, container: this.form.container, template: this.search.template, requestNum: this.search.requestNum});
			}
			
			// box focus status
			this.form.box.focus();
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.search = {
		init: Search.init
	}
})();
