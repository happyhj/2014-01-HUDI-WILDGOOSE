
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
	//var validCheck = WILDGOOSE.ui.validation.validCheck;
	var result = validCheck(inputEl);
	console.log(result);
	if (result) {
		console.log("ok");
	} else {
		console.log("no");
	}
//	var valid = false;
//	console.log("1123")
//	// email
//	// e.valid가 undefined라는 의미는 사용자 정의 이벤트가 아니라는 의미임.
//	if (me.name == "email" && e.valid === undefined) {
//		// email이 유효할 경우 서버에 존재하는지 확인
//		if (validCheck(me)) {
//			existInServer(me);
//		}
//	}
//	// passward
//	if (me.name == "password") {
//		if (validCheck(me)) {
//			valid = true;
//		}
//	}
//	// confirm
//	if (me.name == "confirm") {
//		if (validCheck(me)) {
//			valid = true;
//		}
//	}
	
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
//	if (e.valid !== undefined) {
//		valid = e.valid;
//	}
//	
//	if (valid) {
//		Util.removeClass(me, "status-denied");
////		Util.removeClass(me, "isInvalid");
//		Util.addClass(me, "status-approved");
////		Util.addClass(me, "isValid");
//	}
//	else {
//		Util.removeClass(me, "status-approved");
////		Util.removeClass(me, "isValid");
//		Util.addClass(me, "status-denied");
////		Util.addClass(me, "isInvalid");
//	}
	
	// 각 input의 className을 확인하여 sumbit 버튼 활성화
//	checkFormStatus(inputEl.parentNode);
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
	
	var url = "/api/v1/sign/up";
	var form = document.querySelector(".form-container");
	
	var payload =
		"email=" + escape(form[0].value) +
		"&password=" + escape(form[1].value);
	
	Ajax.POST(url, showSignUpResult, payload);
	Util.addClass(form, "isProgressing");

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
