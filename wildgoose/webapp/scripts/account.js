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


function createAccount() {
	var accountBtn = document.querySelector(".account");
	var url = "/api/v1/subhtml/create_account";
	accountBtn.addEventListener("click", function() {modal.openModalWindow(url)}, false);
}
createAccount();


/*
 * validation action
 */
function addValidationEvent() {
	var formContainer = document.querySelector(".modal .form-container");
	
	for (var i=formContainer.length-1; i>=0; --i) {
		var input = formContainer[i];
		if (input.type == "email" || input.type == "password") {
			
			// blur event
			input.addEventListener("blur", checkSignUpFrom, false);
		}
	}
}

function checkSignUpFrom(e) {
	var me = e.target;
	var valid = false;
	
	// email
	// e.valid가 undefined라는 의미는 사용자 정의 이벤트가 아니라는 의미임.
	if (me.name == "email" && e.valid === undefined) {
		// email이 유효할 경우 서버에 존재하는지 확인
		if (validEmail(me)) {
			existInServer(me);
		}
	}
	// passward
	if (me.name == "password") {
		if (validPassword(me)) {
			valid = true;
		}
	}
	// confirm
	if (me.name == "confirm") {
		if (validConfirm(me)) {
			valid = true;
		}
	}
	
	/*
	 * 아이디의 사용가능여부를 확인하기 위해 ajax를 사용하여 서버에 검증하게 됨
	 * ajax의 callback함수가 실행된 후 결과값을 받아오기 위해서
	 * blur형식의 사용자 정의 이벤트를 발생만들고 valid라는 property속에 true/false라는 값을 포함하여
	 * form input[name=email]에 전달함.
	 * 
	 * valid = e.valid는
	 * 사용자 정의 이벤트일 경우에 
	 * valid변수에 사용자 정의 이벤트 속에 담긴 valid property를 옮기는 작업을 의미함.
	 */  
	if (e.valid !== undefined) {
		valid = e.valid;
	}
	
	if (valid) {
		Util.removeClass(me, "status-denied");
		Util.addClass(me, "status-approved");
	}
	else {
		Util.removeClass(me, "status-approved");
		Util.addClass(me, "status-denied");
	}
}
