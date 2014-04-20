function createRequest() {
	try {
		request = new XMLHttpRequest();
	} catch (tryMS) {
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (otherMS) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (failed) {
				request = null;
			}
		}
	}
	return request;
}

// ajax
function requestData(url, func) {
	
	var request = createRequest();
	if (request == null) {
		console.log("Unable to create request");
		return;
	}
	request.open("GET", url, true);
	request.onreadystatechange = function (e) {
		responseData(e, func);
	};
	request.send(null);
}

function responseData(e, func) {
	if (request.readyState == 4) {
		if (request.status == 200) {
			func(request.responseText);
		}
	}
}


//util




