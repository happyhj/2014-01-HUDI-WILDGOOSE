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
		Ajax.GET(url, this.putHtmlInModal);
		return modal;
	},
	"putHtmlInModal" : function(json) {
		var modal = document.querySelector(".modal");
		var reaction = JSON.parse(json);
		var htmlDoc = reaction["html"]
		modal.innerHTML = htmlDoc;
	},
	"removeModal" : function(event) {
		if (event.target == this) {
			var mask = event.target;
			mask.parentNode.removeChild(mask);
		}
	},
	"openModalWindow" : function(url) {
		var mask = this.createMask();
		var modal = this.createModalWindow(url);
		mask.addEventListener("click", this.removeModal, false);
		mask.appendChild(modal);
		document.body.appendChild(mask);
	}
}