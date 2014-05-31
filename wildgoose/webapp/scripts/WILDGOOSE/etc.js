function getUserId() {
	var userId = document.getElementById("userId").innerText;
	return userId;
}

function isUserLogined() {
	if (getUserId() == "") {
		return false;
	}
	return true;
}