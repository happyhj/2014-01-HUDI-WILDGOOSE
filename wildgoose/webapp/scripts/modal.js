modal = {
	"createMask" : function() {
		var mask = document.createElement("div");
		mask.id = "mask";
		mask.setAttribute("class", "mask mask-on");
		return mask;
	},
	"createModalWindow" : function(url) {
		var modal = document.createElement("div");
		modal.setAttribute("class", "modal");
		return modal;
	},
	"putHtmlInModal" : function(htmlDoc) {
		var modal = document.querySelector(".modal");
		modal.innerHTML = htmlDoc;
		
		addValidationEvent();
		var btn = document.querySelector(".form-container input[type=button]");
		btn.addEventListener("click", signUpAccout, false);
	},
	"removeModal" : function(event) {
		if (event.target == this) {
			var mask = event.target;
			mask.parentNode.removeChild(mask);
		}
	},
	"openModalWindow" : function(url, onloadCallback) {
		var mask = this.createMask();
		var modal = this.createModalWindow(url);
		mask.addEventListener("click", this.removeModal, false);
		mask.appendChild(modal);
		document.body.appendChild(mask);
		
		Ajax.GET(url, this.putHtmlInModal)
		
	}
}