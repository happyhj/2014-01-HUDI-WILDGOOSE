
/*
 * validation action
 */

var validCheck = WILDGOOSE.ui.validation.validCheck;

	
function addValidationEvent() {
	var formContainer = document.querySelector(".modal .form-container");
	
	for (var i = formContainer.length - 1; i >= 0; --i) {
		var input = formContainer[i];
		if (input.type == "email" || input.type == "password") {
			// blur event
			input.addEventListener("blur", checkSignUpFrom, false);
		}
	}
}

function checkSignUpFrom(e) {
	var inputEl = e.target;
	
	// validCheck는 전역으로 선언
	if (validCheck(inputEl)) {
		console.log("validation ok");
	} else {
		console.log("validation no");
	}
	
	// 각 input의 className을 확인하여 sumbit 버튼 활성화
	checkFormStatus(inputEl.parentNode);
}


/*
 * form에 입력된 내용이 valid한지를 확인하여 회원가입 버튼 활성화 / 비활성화
 */
function checkFormStatus(form) {
	var btn = form.length-1;
	var flag = true;
	for (var i=btn-1; i>=0; --i) {
		if (!Util.hasClass(form[i], "status-approved")) {
			flag = false;
			break;
		}
	}

	if (flag)
		Util.removeClass(form[btn], "hidden");
	else
		Util.addClass(form[btn], "hidden");
	
	console.log(flag + " " + form[btn].className);
}

/*
 * 모두 작성된 정보를 Ajax POST로 서버에 전달
 */
function signUpAccout() {
	
	var url = "/api/v1/accounts/new";
	var form = document.querySelector(".form-container");
	
	var email = escape(form[0].value)
	var password = escape(form[1].value);
	var payload = "email=" + email + "&password=" + SHA256(password);
	console.log(payload)
	Ajax.POST(url, showSignUpResult, payload);
//	Util.addClass(form, "isProgressing");

}

/*
 * signUpAccount 실행 후
 * 서버에서 전달된 결과값 확인
 */
function showSignUpResult(response) {
	console.log("response: " + response);
	
	var form = document.querySelector(".form-container");
	Util.removeClass(form, "isProgressing");
	
	var responseDiv = document.createElement("div");
	responseDiv.innerHTML = response;
	
	form.appendChild(responseDiv);
	
}
