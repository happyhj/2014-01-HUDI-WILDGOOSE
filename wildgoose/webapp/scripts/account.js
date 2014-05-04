function createAccount() {
	var accountBtn = document.querySelector(".account");
	var url = "/api/v1/subhtml/create_account";
	accountBtn.addEventListener("click", function() {modal.openModalWindow(url)}, false);
}
createAccount();