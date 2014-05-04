function checkEmail(me) {	
	// 채워졌는지 확인
	if (!fieldIsFilled(me)) return false; 
	// 제대로 채워졌는지 확인
	if (!emailIsProper(me)) return false;

	return true;
}

function existInServer (me) {
	var url = "api/v1/check/email/" + me.value;
	Ajax.GET(url, showEmailStatus);
}


function fieldIsFilled(me) {
	if (me.value != "") {
		unwarn(me, "required");
		return true;
	}
	warn(me, "required");
	return false;	
}

function emailIsProper(me) {
	if (/^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/.test(me.value)) {
		unwarn(me, "format");
		return true;
	}
	warn(me, "format");
	return false;
}

/*
 * response: OK or 공백
 * OK: 이메일 사용가능
 *   : 이메일 사용불가
 */
function showEmailStatus(response) {
	// response에 포함된 개행문자 제거
	response = response.substring(0, response.length - 1);
	var email = document.querySelector(".form-container > input[name=email]");
	
	if (response) {
		unwarn(email, "usable");
		Util.removeClass(email, "status-denied");
		Util.addClass(email, "status-approved");
		return true;
	}

	warn(email, "usable");
	Util.removeClass(email, "status-approved");
	Util.addClass(email, "status-denied");
	return false;
}

var warnings = {
	"email" : {
		"required" : "email을 입력해주세요",
		"format" : "email형식을 지켜주세요",
		"usable" : "이미 등록된 email입니다",
		"err" : 0
	}
}

function warn(field, warningType) {
	var type = field.type;
	var target = document.querySelector(".form-container .msg-" + type);
	console.log(target);
	console.log(warningType);
	
	var warning = warnings[type][warningType];
	console.log(warning);
	
	target.innerText = warning; 
}

function unwarn(field, warningType) {
	var type = field.type;
	var target = document.querySelector(".form-container .msg-" + type);
	console.log(target);
	
	target.innerText = "";
}

