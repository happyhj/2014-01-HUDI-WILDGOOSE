/*
 * modal action
 */
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
	var url = "/api/v1/subhtml/create_account";
	Ajax.GET(url, putHtmlInModal);
	return modal;
}

function putHtmlInModal(htmlDoc) {
	var modal = document.querySelector(".modal");
	console.log(htmlDoc);
	modal.innerHTML = htmlDoc;
	
	// add event for validation at input
	addValidationEvent();
}

function removeModal(event) {
	if (event.target == this) {
		var mask = event.target;
		mask.parentNode.removeChild(mask);
	}
}

var accountBtn = document.querySelector(".account");
accountBtn.addEventListener("click", doAction, false);

/*
 * validation action
 */
function addValidationEvent() {
	var formContainer = document.querySelector(".modal .form-container");
	
	for (var i=formContainer.length-1; i>=0; --i) {
		var input = formContainer[i];
		if (input.type == "email" || input.type == "password") {
			
			// blur event
			input.addEventListener("blur", function(e) {
					var me = e.target;
					
					// email
					if (me.type == "email") {
						if (!checkEmail(me)) {
							Util.removeClass(me, "status-approved");
							Util.addClass(me, "status-denied");
						}
						else {
							// 서버에 존재하는지 확인
							existInServer(me);
						}
					}
					
					// passward
					
					
					// confirm
					

			}, false);
		}
	}
}


