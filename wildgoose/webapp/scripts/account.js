function doAction() {
	var mask = createMask();
	var modal = createModal();
	mask.addEventListener("click", removeModal, false);
	mask.appendChild(modal);
	document.body.appendChild(mask);
}
function createMask() {
	var mask = document.createElement("div");
	mask.id = "mask";
	mask.setAttribute("class", "mask mask-on");
	return mask;
}
function createModal() {
	var modal = document.createElement("div");
	modal.setAttribute("class", "modal");
	return modal;
}
function removeModal(event) {
	if (event.target == this) {
		var mask = event.target;
		mask.parentNode.removeChild(mask);
	}
}

var accountBtn = document.querySelector(".account");
accountBtn.addEventListener("click", doAction, false);