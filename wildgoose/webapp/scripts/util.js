var Ajax = (function(){
	return {
		createRequest : function () {
			try {
				var request = new XMLHttpRequest();
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
		},
		responseData : function(e, request, func) {
			if (request.readyState == 4) {
				if (request.status == 200) {
					func(request.responseText);
				}
			}
		},
		requestData : function (url, func) {
			var request = this.createRequest();
			if (request == null) {
				console.log("Unable to create request");
				return;
			}
			request.open("GET", url, true);
			request.addEventListener("readystatechange", function (e) {
				Ajax.responseData(e, request, func);
			}, false); 
			request.send(null);
		}
	}
}());
