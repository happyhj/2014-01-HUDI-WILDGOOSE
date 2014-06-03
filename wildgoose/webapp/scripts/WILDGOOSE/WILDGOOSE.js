(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.graph = WILDGOOSE.ui.graph || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;

	var Graph = {
		brokenLine: function(rawD) {
			var realData = JSON.parse(rawD);
			var sampleData = realData.data.numberOfArticles;

			var svgContainer = d3.select("#brokenline-graph > .graph").append("svg")
			.style("width", "100%").style("height", 300).attr("id", "brokenLineGraph")
			.attr("viewBox", "0 0 520 360");

			var backgroundColor = svgContainer.append("rect").attr("width", "100%").attr("height", "100%").attr("fill", "#26A88E");


			// NumberIndex
			var numberIndexYPosition = [
			{"y_pos": 285, "num": "0"}, {"y_pos": 235, "num": "1"}, 
			{"y_pos": 185, "num": "2"}, {"y_pos": 135, "num": "3"}, {"y_pos": 85, "num": "4"}];
			var numberIndexes = svgContainer.selectAll("text").data(numberIndexYPosition).enter().append("text");
			numberIndexes.attr("x", 7).attr("font-size","18").attr("fill", "white").attr("y", function (d) {return d.y_pos;}).text(function(d) {return d.num;});

			// guidanceLine
			guidanceLinePosition = [ "M 45 85 H520", "M 45 135 H520", "M 45 185 H520", "M 45 235 H520", "M 45 285 H520" ];

			var guidLines = svgContainer.selectAll("path").data(guidanceLinePosition).enter().append("path")

			guidLines.attr("d", function (d) {return d;}).attr("stroke", "#7D7D7D")
				.attr("stroke-width", 1).style("stroke-dasharray", "5,8")
				.attr("stroke-opacity", 0.6).attr("fill","none");

			// variable values
			matching = [ {		//number : Y position
				0 : 285,
				1 : 235,
				2 : 185,
				3 : 135,
				4 : 85
			} ];

			graphPositionX = [ 50, 120, 190, 260, 330, 400, 470 ];

			var keys = new Array();
			var now = new Date();
			now.setDate(now.getDate() - 7);
			for(var i = 0; i<7; i++){
				now.setDate(now.getDate() + 1);
				var month = now.getMonth() + 1;
				if (month < 10) { month = '0' + month; }
				keys.push(month + "/" + now.getDate());
			}

			var getValue = function(date) {
				var result = sampleData[date];
				if (result == null) {
					return 0;
				}
				return result;
			}
			// graph
			var graphData = [ {
				"x" : graphPositionX[0],
				"y" : matching[0][getValue(keys[0])]
			}, {
				"x" : graphPositionX[1],
				"y" : matching[0][getValue(keys[1])]
			}, {
				"x" : graphPositionX[2],
				"y" : matching[0][getValue(keys[2])]
			}, {
				"x" : graphPositionX[3],
				"y" : matching[0][getValue(keys[3])]
			}, {
				"x" : graphPositionX[4],
				"y" : matching[0][getValue(keys[4])]
			}, {
				"x" : graphPositionX[5],
				"y" : matching[0][getValue(keys[5])]
			}, {
				"x" : graphPositionX[6],
				"y" : matching[0][getValue(keys[6])]
			} ]



			var lineFunction = d3.svg.line().x(function(d) {return d.x;})
					.y(function(d) {return d.y;}).interpolate("linear");

			var graph = svgContainer.append("path").attr("stroke", "white")
				.attr("stroke-width", 2).attr("fill", "none")
				.attr("stroke-linecap","round").attr("d", lineFunction(graphData));

			var text = svgContainer.append("text").attr("x", 370).attr("y", 40)
			.attr("font-size", "20").attr("fill", "white").text("기사송고추이");

			for(var i in keys) { 
				keys[i] = keys[i].replace("-", "/"); 
			}

			// date label
			for(labelIndex = 0; labelIndex < keys.length; labelIndex++){
				svgContainer.append("text").attr("font-size", "18").attr("fill", "white").attr("y", 315)
				.attr("x", 20 + (70 * labelIndex)).text(keys[labelIndex]);
			}
		},

		radar: function(data) {

			//var stat_data = JSON.parse(data)["data"];
			var stat_data = {
				"통솔" : Math.random() * 10,
				"매력" : Math.random() * 10,
				"지력" : Math.random() * 10,
				"정치" : Math.random() * 10,
				"무력" : Math.random() * 10
			};
			var object = {
				"container-width" : 400,
				"container-height" : 400,
				"circle-scale" : 0.75,
				"color" : "red"
			};

			var svgContainer = d3.select("#radar-graph > .graph").append("svg").style(
					"width", "100%").style("height", "300px").attr("id", "radarGraph")
					.attr("viewBox", "0 0 400 400");


			var backgroundColor = svgContainer.append("rect").attr("width", "100%")
					.attr("height", "100%").attr("fill", "#FFF");


			var radius = object["container-width"] / 2 * object["circle-scale"];
			for (var i = 0; i < 10; i++) {
				var circle = svgContainer.append("ellipse").attr("cx",
						object["container-width"] / 2).attr("cy",
						object["container-height"] / 2).attr("rx",
						radius * (1 - 0.1 * i)).attr("ry", radius * (1 - 0.1 * i))
						.attr("stroke", "#AAA").attr("stroke-width", 0.5).attr("fill",
								"white").attr("fill-opacity", 0);
			}

			// (200,200) 을 중심으로 화전시켜야 함
			function rotation(point, degree) {
				var x = point.x * Math.cos(degree) - point.y * Math.sin(degree);
				var y = point.x * Math.sin(degree) + point.y * Math.cos(degree);
				return {
					"x" : x,
					"y" : y
				};
			}

			var num_of_stats = Object.keys(stat_data).length;
			var i = 0;
			var lineData = [];
			for ( var stat in stat_data) {
				var point = {
					x : 0,
					y : -1 * stat_data[stat] / 10 * radius
				};
				point = rotation(point, 2 * Math.PI / num_of_stats * i);
				point.x += object["container-width"] / 2;
				point.y += object["container-height"] / 2;
				lineData.push(point);
				i++;
			}
			lineData.push(lineData[0]);

			var lineFunction = d3.svg.line().x(function(d) {
				return d.x;
			}).y(function(d) {
				return d.y;
			}).interpolate("linear");

			var vertexData = [];
			for (var i = 0; i < num_of_stats; i++) {
				var point = {
					x : 0,
					y : -1 * radius
				};
				point = rotation(point, 2 * Math.PI / num_of_stats * i);
				var point_label = {
					x : 0,
					y : -1 * (radius + 25)
				};
				point_label = rotation(point_label, 2 * Math.PI / num_of_stats * i);
				point.x += object["container-width"] / 2;
				point.y += object["container-height"] / 2;
				point.title = Object.keys(stat_data)[i];
				point.value = stat_data[point.title];
				point.x_t = point_label.x + object["container-width"] / 2;
				point.y_t = point_label.y + object["container-height"] / 2;

				vertexData.push(point);
			}
			for (var j = 0; j < vertexData.length; j++) {
				var lineGraph = svgContainer.append("path").attr("d",
						lineFunction([ vertexData[j], {
							x : object["container-width"] / 2,
							y : object["container-width"] / 2
						} ])).attr("stroke", "#AAA").attr("stroke-width", .5).attr(
						"fill", "yellow").attr("fill-opacity", .6);
			}

			var lineGraph = svgContainer.append("path").attr("d",
					lineFunction(lineData)).attr("stroke", "#222").attr("stroke-width",
					.5).attr("stroke-opacity", .1).attr("fill", object.color).attr(
					"fill-opacity", .4);



			//Add the SVG Text Element to the svgContainer
			var text = svgContainer.selectAll("text").data(vertexData).enter().append(
					"text");
			//Add SVG Text Element Attributes
			var textLabels = text.attr("x", function(d) {
				return d.x_t - 16;
			}).attr("y", function(d) {
				if (d.title === "통솔")
					return d.y_t + 1;
				return d.y_t - 4;
			}).text(function(d) {
				return d.title;
			}).attr("font-family", "sans-serif").attr("font-size", "18px").attr("fill",
					"#AAA").append('svg:tspan').attr('x', function(d) {
				return d.x_t - 16;
			}).attr('dy', 20).attr("font-size", "23px").attr("fill", "#444").text(
					function(d) {
						return parseInt(d.value * 10) / 10;
					});

		},

		donut: function(rawD) {
			console.log(rawD);
			var realData = JSON.parse(rawD);
			var data = realData.data.numberOfArticles;

			console.log(data);

			var w = 300 //width
			var h = 300 //height
			var r = 100 //radius
			var pie_scale = 1.2 // scale pie chart (default 1.2)
			var color = d3.scale.category20c(); //builtin range of colors

			var vis = d3.select("#donut-graph > .graph").append("svg") //create the SVG element inside the <body>

			.data([ data ]) //associate our data with the document
			.attr("viewBox", "0 0 300 300")
			.attr("style", "width: 100%; height: 300px;")
			.append("svg:g")
			.attr("transform", "translate(" + w/2 + "," + h/2 + ")") //move the center of the pie chart from 0, 0 to radius, radius

			var arc = d3.svg.arc() //this will create <path> elements for us using arc data
			.outerRadius(r*pie_scale);

			var pie = d3.layout.pie() //this will create arc data for us given a list of values
			.value(function(d) {
				return d.value;
			}); //we must tell it out to access the value of each element in our data array

			var arcs = vis.selectAll("g.slice") //this selects all <g> elements with class slice (there aren't any yet)
			.data(pie) //associate the generated pie data (an array of arcs, each having startAngle, endAngle and value properties) 
			.enter() //this will create <g> elements for every "extra" data element that should be associated with a selection. The result is creating a <g> for every object in the data array
			.append("svg:g") //create a group to hold each slice (we will have a <path> and a <text> element associated with each slice)
			.attr("class", "slice"); //allow us to style things in the slices (like text)

			arcs.append("svg:path").attr("fill", function(d, i) {
				return color(i);
			}) //set the color for each slice to be chosen from the color function defined above
			.attr("d", arc); //this creates the actual SVG path using the associated data (pie) with the arc drawing function

			arcs.append("svg:text") //add a label to each slice
			.attr("transform", function(d) { //set the label's origin to the center of the arc
				//we have to make sure to set these before calling arc.centroid
				d.innerRadius = 0;
				d.outerRadius = r;
				return "translate(" + arc.centroid(d) + ")"; //this gives us a pair of coordinates like [50, 50]
			}).attr("text-anchor", "middle") //center the text on it's origin
			.text(function(d, i) {
				return data[i].label;
			}).attr("font-family", "sans-serif")
		    .attr("font-size", "18px")
		    .attr("fill", "#FFF")  ; //get the label from our original data array
		},

		init: function() {	
			var reporterId = window.location.pathname.split("/")[2];
			var graphInfo = {
				donut:{
					url: "/api/v1/reporters/:reporterId/statistics?data=number_of_articles&by=section",
					callback: this.donut
				},
				brokenLine:{
					url: "/api/v1/reporters/:reporterId/statistics?data=number_of_articles&by=day",
					callback: this.brokenLine
				},
				radar:{
				 	url: "/api/v1/reporters/:reporterId/statistics?data=stat_points",
				 	callback: this.radar
				}
			};

			for (var graphName in graphInfo) {
				var graph = graphInfo[graphName];
				var url = graph['url'].replace(":reporterId", reporterId);
				Ajax.GET({"url":url, "callback":graph.callback.bind(this)});
			}
		}
	}

	// 공개 메서드 노출
	WILDGOOSE.ui.graph = Graph;
})();(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.etc = WILDGOOSE.etc || {};	
	
	var Etc = {			
		getUserId: function() {
			this.userId = document.getElementById("userId").innerText;
			return this.userId;
		},
		isUserLogined: function() {
			if (this.getUserId() == "") {
				return false;
			}
			return true;
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.etc = Etc;
})();(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.ui.validation = WILDGOOSE.ui.validation || {};

	var Ajax = CAGE.ajax
	var Dom = CAGE.util.dom;
	
	var validation_logics = {
		email : {
			sequence : [ "required", "format", "usable" ],
			required : [ /.+/, "email을 입력해주세요" ],
			format : [ /^[\w\.-_\+]+@[\w-]+(\.\w{2,4})+$/, "email형식을 지켜주세요" ],
			usable : [ function(inputEl, callback) {
				existInServer(inputEl, callback);
			}, "이미 등록된 email입니다" ]
		},
		password : {
			sequence : [ "required", "letter", "size", "ampleNumber", "ampleLetter" ],
			required : [ /.+/, "비밀번호를 입력해주세요" ],
			letter : [
					/[a-zA-Z0-9\`\~\!\@\#\$\%\^\&|*\(\)\-\_\=\=\+\\\|\,\.\<\>\/\?\[\]\{\}\;\:\'\"]/,
					"숫자, 영문자 대소문자, 특수문자만 사용해주세요" ],
			size : [ /^.{8,15}$/, "8~15자 사이로 입력해주세요" ],
			ampleNumber : [ /(.*\d{1}.*){4,}/, "숫자는 4자리 이상 포함되어야 합니다" ],
			ampleLetter : [ /(.*\D{1}.*){4,}/, "문자는 4자리 이상 포함되어야 합니다" ]
		},
		confirm : {
			sequence : [ "required", "equal" ],
			required : [ /.+/, "다시 입력해주세요" ],
			equal : [ function(inputEl, callback) {
				ckeckEquality(inputEl, callback);
			}, "다시 확인해주세요" ]
		}		
	};	

	function ckeckEquality(inputEl, callback) {
		var parent = inputEl.parentNode;
		var password = document.querySelector("." + parent.className
				+ " input[name=password]");
		
		callback(inputEl.value == password.value);
	}

	function existInServer(inputEl, callback) {
		var url = "api/v1/accounts/?email=" + inputEl.value;
		Ajax.GET({"url":url, "callback":function(response) {
			console.log(response);
			var validity = (JSON.parse(response).status===200)?true:false;
			var isAjax = true;
			callback(validity, isAjax);
		}});
		Dom.addClass(inputEl, "isProgressing");
	}
	
	
	function validCheck(inputEl) {
		var fieldName = inputEl.name;
		var fieldValue = inputEl.value;
		var checking_sequence = validation_logics[fieldName]["sequence"];

		for ( var i = 0; i<checking_sequence.length; ++i) {
			var cur_sequence = checking_sequence[i];
			console.log(cur_sequence);
			var checking_logic = validation_logics[fieldName][cur_sequence];
			var alert_message = checking_logic[1];
			
			if (checking_logic[0] instanceof RegExp) {
				console.log("RegExp");
				console.log(checking_logic[0].test(fieldValue));
				if (!checking_logic[0].test(fieldValue)) {
					warn(inputEl, alert_message);
					invalidStyle(inputEl);
					return false;
				}
			} else if (checking_logic[0] instanceof Function) {
				console.log("Function");
				var valid_state = true; 
				checking_logic[0](inputEl, function(validity, isAjax) {
					if (isAjax) {
						Dom.removeClass(inputEl, "isProgressing");
					}
					if (!validity) {
						warn(inputEl, alert_message);
						invalidStyle(inputEl);
						valid_state = false;
						return false;
					}
				});
				if (!valid_state) {
					return false;
				}
			}
		}
		unwarn(inputEl);
		validStyle(inputEl);
		return true;
	}
	
	
	/*
	 * 상태에 따른 변경될 style을 모음 
	 */
	function validStyle(inputEl) {
		Dom.removeClass(inputEl, "status-denied");
		Dom.removeClass(inputEl, "isInvalid");
		Dom.addClass(inputEl, "status-approved");
		Dom.addClass(inputEl, "isValid");
	}
	
	function invalidStyle(inputEl) {
		Dom.removeClass(inputEl, "status-approved");
		Dom.removeClass(inputEl, "isValid");
		Dom.addClass(inputEl, "status-denied");
		Dom.addClass(inputEl, "isInvalid");
	}

	/*
	 * 사용에게 메시지를 전달하기 위한 함수 
	 */
	function warn(inputEl, warningMsg) {
		var name = inputEl.name;
		var target = document.querySelector(".form-container .msg-" + name);

		target.innerText = warningMsg;
	}

	function unwarn(inputEl) {
		var name = inputEl.name;
		var target = document.querySelector(".form-container .msg-" + name);

		target.innerText = "";
	}
	
	WILDGOOSE.ui.validation = {
		validCheck: validCheck
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.account = WILDGOOSE.account || {};

	/*
	 * validation action
	 */
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
	var Validation = WILDGOOSE.ui.validation;
	
	function addValidationEvent() {
		var formContainer = document.querySelector(".form-container");
		for (var i = formContainer.length - 1; i >= 0; --i) {
			var input = formContainer[i];
			if (input.type == "email" || input.type == "password") {
				// blur event
	//			var dataCheck = input.getAttribute("data-check");
		//		if(dataCheck == "true") {
					input.addEventListener("blur", checkSignUpFrom, false);
			//	}
			}
		}
	};
	
	function checkSignUpFrom(e) {
		var inputEl = e.target;
		if (Validation.validCheck(inputEl)) {
			console.log("validation ok");
		} else {
			console.log("validation no");
		}
		
		// 각 input의 className을 확인하여 sumbit 버튼 활성화
		checkFormStatus(inputEl.parentNode);
	};
	
	
	/*
	 * form에 입력된 내용이 valid한지를 확인하여 회원가입 버튼 활성화 / 비활성화
	*/
	function checkFormStatus(form) {
		var btn = form.length-1;
		var flag = true;
		for (var i=btn-1; i>=0; --i) {
			if (!Dom.hasClass(form[i], "status-approved")) {
				flag = false;
				break;
			}
		}
		Dom[flag?"removeClass":"addClass"](form[btn], "hidden");
		
	};
	
	/*
	 * 모두 작성된 정보를 Ajax POST로 서버에 전달
	 */
	function signUpAccount(popup) {
		var url = "/api/v1/accounts/";
		var form = document.querySelector(".form-container");
		
		var email = escape(form[0].value)
		var password = escape(form[1].value);
		var payload = "email=" + email + "&password=" + SHA256(password);
		Ajax.POST({"url":url, "callback":function(response) {
			var form = document.querySelector(".form-container");
			Dom.removeClass(form, "isProgressing");
			if (JSON.parse(response).status == 200) {
				popup.afterclose.add(function() {location.reload();});
				popup.close();
			}
		}, "data":payload});
	//	domUtil.addClass(form, "isProgressing");
	
	};
	
	/*
	 * signUpAccount 실행 후
	 * 서버에서 전달된 결과값 확인
	 */
	function showSignUpResult(response) {
		
		if (response == "success") {
			// close modal. and update login panel
			WILDGOOSE.ui.modal.closeModal(function(){
				updateTopbar(true);
			});
		}
	};
	
	
	function loginAccount(popup) {
		var email = document.querySelector(".form-container input[name=email]").value;
		var password = document.querySelector(".form-container input[name=password]").value;
		var hashedPassword = SHA256(password);	
		var randomNumber = document.querySelector(".form-container input[name=randomNumber]").value;
		var finalPassword = SHA256(hashedPassword+randomNumber);
		var url = "/api/v1/session/";
		var payload = "email="+email+"&password="+finalPassword;
		Ajax.POST({"url": url, "callback":function(response) {
			var form = document.querySelector(".form-container");
			Dom.removeClass(form, "isProgressing");
//			console.log(response);
//			console.log(JSON.parse(response).status);
			if (JSON.parse(response).status == 200) {
				popup.afterclose.add(function() {location.reload();});
				popup.close();
			}
		}, "data":payload});
	};
	
	function withdrawAccount(){
		var user_email = document.getElementById("userId").innerText;
		Ajax.DELETE({
			"url":'/api/v1/accounts?email=' + user_email,
			"callback":function() {location.href="/";}
		});
	}
	
	WILDGOOSE.account = {
		loginAccount: loginAccount,
		signUpAccount: signUpAccount,
		showSignUpResult: showSignUpResult,
		checkFormStatus: checkFormStatus,
		checkSignUpFrom: checkSignUpFrom,
		addValidationEvent: addValidationEvent,
		withdrawAccount: withdrawAccount
	};
	
	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));
(function(window) {
	'use strict';
	var document = window.document;
	var console = window.console;
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.header = WILDGOOSE.header || {};

	// 의존성 선언 
	var Ajax = CAGE.ajax;
	var TemplateUtil = CAGE.util.template;
	var Dom = CAGE.util.dom;
	var Popup = CAGE.ui.popup;
	var Account = WILDGOOSE.account;
	
	function init(){
		var joinBtn = document.querySelector("#join");
		
		var joinPopup = new Popup.ajaxPopup({
			element: joinBtn,
			templateUrl: "/api/v1/templates/account.html",
			templateLoader: function(AjaxResponse) {
				return JSON.parse(AjaxResponse).data.template;
			}
		});
		
		joinPopup.afteropen.add(function() {
			Account.addValidationEvent();
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", Account.signUpAccount.bind(this, joinPopup), false);
		});
		
		var loginBtn = document.querySelector("#login");
		
		var loginPopup = new Popup.ajaxPopup({
			element: loginBtn,
			templateUrl: "/api/v1/templates/login.html",
			templateLoader: function(AjaxResponse) {
				var templateStr = JSON.parse(AjaxResponse).data.template;
				var randNum = JSON.parse(AjaxResponse).data.rand;
				var compiler = TemplateUtil.getCompiler(templateStr);
				return compiler({
					"randNum": randNum
				}, templateStr);		
			}
		});
		
		loginPopup.afteropen.add(function() {
			var btn = arguments[0].querySelector("#create");
			btn.addEventListener("click", Account.loginAccount.bind(this, loginPopup), false);
		});
		
		var leaveBtn = document.querySelector("#leave");
		leaveBtn.addEventListener("click", function(){
			console.log("탈퇴시킴. 확인창 뜨는건 다음 스텝에서");
			Account.withdrawAccount();
		}, false);
		
		
		var leaveBtn = document.querySelector("#leave");
		leaveBtn.addEventListener("click", function(){
			console.log("탈퇴시킴. 확인창 뜨는건 다음 스텝에서");
			Account.withdrawAccount();
		}, false);
		
		
		var logoutBtn = document.querySelector(".header-btn#logout");
		logoutBtn.addEventListener("click", function() {
			Ajax.DELETE({
				"url":'/api/v1/session',
				"callback":function() {location.href="/";}
			});
		}, false);
		
		var timelineBtn = document.querySelector(".header-btn#timeline");
		timelineBtn.addEventListener("click", function() {
			var Etc = WILDGOOSE.etc;
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/timeline".replace("?user_id?", userId);;
		}, false);
		
		var favoriteBtn = document.querySelector(".header-btn#favorite");
		favoriteBtn.addEventListener("click", function() {
			var Etc = WILDGOOSE.etc;
			var userId = Etc.getUserId();
			location.href = "/users/?user_id?/favorites".replace("?user_id?", userId);
		}, false);
		
		function updateTopbar(isLogined) {
			var joinBtn = document.querySelector(".header-btn#join");
			var loginBtn = document.querySelector(".header-btn#login");
			var logoutBtn = document.querySelector(".header-btn#logout");
			var timelineBtn = document.querySelector(".header-btn#timeline");
			var favoriteBtn = document.querySelector(".header-btn#favorite");
			console.log(logoutBtn);
			if (isLogined == true) {
				joinBtn.className = "header-btn hidden";
				loginBtn.className = "header-btn hidden";
				logoutBtn.className = "header-btn";
				timelineBtn.className = "header-btn";
				favoriteBtn.className = "header-btn";
			} else {
				joinBtn.className = "header-btn";
				loginBtn.className = "header-btn";
				logoutBtn.className = "header-btn hidden";
				timelineBtn.className = "header-btn hidden";
				favoriteBtn.className = "header-btn hidden";
			}
		}
	}

	WILDGOOSE.header = {
		init: init
	}

	// 글로벌 객체에 모듈을 프로퍼티로 등록한다.
	if (typeof module !== 'undefined' && module.exports) {
		module.exports = WILDGOOSE;
		// browser export
	} else {
		window.WILDGOOSE = WILDGOOSE;
	}    	

}(this));(function() {
	'use strict';
	var document = window.document;
	var console = window.console;

	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.ui = WILDGOOSE.ui || {};
	WILDGOOSE.favorite = WILDGOOSE.favorite || {};
	
	// 의존성 선언
	var Ajax = CAGE.ajax;
	var Dom = CAGE.util.dom;
	var Etc = WILDGOOSE.etc;

	var Favorite = {
		favoriteList : [],

		attatchEventToFavBtn : function(curNum, reqNum) {
			var reporterCards = document.querySelectorAll(".card");
			if (reporterCards.length != 0) {
				(curNum == undefined) ? curNum = reporterCards.length : true;
				(reqNum == undefined) ? reqNum = reporterCards.length : true;
				for (var i = curNum - reqNum ; i < curNum; i++) {
					var card = reporterCards[i];
					if (card == undefined) {
						continue;
					}
					var star = card.querySelector(".star");
					star.addEventListener("click", this.toggleFav, false);
					star.addEventListener("click", function(e) {
						Dom.addClass(e.target, "pumping");
						setTimeout(function() {
							Dom.removeClass(e.target, "pumping");
						}, 300)
					}, false);
				}				
			}
		},

		toggleFav : function(e) {
			var target = e.target;
			var card = target.parentElement.parentElement.parentElement;
			var reporterId = card.firstElementChild.dataset.reporter_id;
			var userId = Etc.getUserId();
			var url = "/api/v1/users/" + userId + "/favorites/?reporter_id="
					+ reporterId;


			if (Dom.hasClass(target, "on")) {
				Ajax.DELETE({
					"url" : url,
					"callback" : function(data) {
						var data = JSON.parse(data);
						if (data.status == 200) {
							Dom.removeClass(target, "on");
							Dom.addClass(target, "off");
							Dom.addClass(card, "blur");
						} else {
							// react fail
						}
					}
				});
			} else {
				Ajax.POST({
					"url" : url,
					"callback" : function(data) {
						var data = JSON.parse(data);
						if (data.status == 200) {
							Dom.addClass(target, "on");
							Dom.removeClass(target, "off");
							Dom.removeClass(card, "blur");

						} else {
							// react fail
						}
					}
				});
			}
		},

		updateFavs : function(curNum, reqNum) {
			var reporterCards = document.querySelectorAll(".card-section-identity");
			if (reporterCards.length != 0) {
				(curNum == undefined) ? curNum = reporterCards.length : true;
				(reqNum == undefined) ? reqNum = reporterCards.length : true;
				for (var i = curNum - reqNum ; i < curNum; i++) {
					var card = reporterCards[i];
					if (card == undefined) {
						continue;
					}
					var reporterId = card.dataset.reporter_id;
					if (this.favoriteList.indexOf(parseInt(reporterId)) >= 0) {
						card.querySelector(".star").className = "star on";
					}
				}				
			}
		},
		
		init: function() {
			// 초기화
			if (Etc.isUserLogined()) {
				// userID 확인
				var userId = Etc.getUserId();
				
				// 모든 별에 eventlistener 붙이기
				this.attatchEventToFavBtn();
				
				// user의 Favorite 목록 획득
				var url = "/api/v1/users/" + userId + "/favorites/";
				Ajax.GET({
					"url" : url,
					"callback" : function(jsonStr) {
						var result = JSON.parse(jsonStr);
						var reporterCards = result["data"]["reporterCards"]
						for (var i=0; i<reporterCards.length; i++) {
							var card = reporterCards[i];
							Favorite.favoriteList.push(card["id"]);
						}
						// 불러온 목록 내부에 존재하는 favorite 업데이트
						// 인자가 없으면 모두!
						this.updateFavs();
					}.bind(this)
				});
			}
		}
	};
	WILDGOOSE.ui.favorite = Favorite;

})();(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.search = WILDGOOSE.search || {};
	WILDGOOSE.search.auto_complement = WILDGOOSE.search.auto_complement || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	
	var AutoComplement = {
		init : function(args) {
			this.row = {
				requestCount : args.requestNum,
				currentCount : 0
			};
			
			// ms
			this.interval = 100;

			this.is = {
				searching : false,
				hovering : false,
				pressedEnter : false,
				listing : false,
				highlighting : false
			};	
//			this.box = document.querySelector(args.searchBox);
//			this.list = document.querySelector(args.container);
			this.box = args.searchBox;
			this.list = args.container;
			
			this.cache = {
				searchedQuery : this.box.value,
				row : null,
				callbackRef : {
					notify : this.notify.bind(this),
					expired : this.expired.bind(this),
					listHandler : this.listHandler.bind(this),
					drawList : this.drawList.bind(this),
					setHovering : this.setHovering.bind(this),
					observeState : this.observeState.bind(this)
				}
			};
			this.box.addEventListener("focus", this.cache.callbackRef.notify);
			this.box.addEventListener("blur", this.cache.callbackRef.expired);
			
			this.box.focus();
		},

		expired : function(evt) {
			if (this.is.searching != false && this.is.hovering == false) {
				// list가 지워지지 않은 경우 제거
				if (this.is.listing == true) {
					this.removeList();
				}
				// value값 탐지 해제
				clearInterval(this.is.searching);
				this.is.searching = false;
			}
		},

		notify : function(evt) {
			if (this.is.searching == false) {
				this.is.searching = setInterval(this.cache.callbackRef.observeState, this.interval);
			}
		},
		
		observeState : function() {
			var curQuery = this.box.value;
			// curQuery 값이 변했을 경우만 처리
			if (curQuery != this.cache.searchedQuery) {
				console.log("detection: " + curQuery);
				if (curQuery == "") {
					// list 종료 로직.
					this.removeList();
				}
				else {
					this.search(curQuery);
				}
			}
		},
		
		search : function(searched) {
			this.cache.searchedQuery = searched;
			var url = "/api/v1/search?autocomplete=true&q=" + searched + "&how_many=" + this.row.requestCount;
			Ajax.GET({"url":url, "callback":this.cache.callbackRef.drawList});
		},
		
		drawList : function(response) {
			this.is.pressedEnter = false;
			var data = JSON.parse(response).data.reporters;
			console.log(data)
			if (data === undefined || data.length == 0) {
				return;
			}
			
			// cache.row 초기화
			this.cache.row = null;
			// 전달받은 row개수를 기록
			this.row.currentCount = data.length;
			var li_template = "";
			for (var i = 0; i < this.row.currentCount; i++) {
				li_template += "<li><div>" + data[i]["name"] + "</div></li>";
			}
			this.list.innerHTML = li_template;
			this.addList();
		},
		
		removeList : function() {
			this.is.listing = false;
			this.row.currentCount = 0;
			this.cache.row = null;
			this.list.style.display = "none";
			this.removeMouseRader(this.list);
			this.box.removeEventListener("keydown", this.cache.callbackRef.listHandler, false);
			this.list.removeEventListener("click", this.cache.callbackRef.listHandler, false);
			this.list.removeEventListener("mousemove", this.cache.callbackRef.listHandler, false);
			
			this.is.hovering = false;
		},
		addList : function() {
			this.is.listing = true;
			this.list.style.display = "inline-block";
			this.addMouseRader(this.list);
			this.box.addEventListener("keydown", this.cache.callbackRef.listHandler, false);
			this.list.addEventListener("click", this.cache.callbackRef.listHandler, false);
			this.list.addEventListener("mousemove", this.cache.callbackRef.listHandler, false);
		},
		addMouseRader : function(el) {
			el.addEventListener("mouseover", this.cache.callbackRef.setHovering, false);
			el.addEventListener("mouseout", this.cache.callbackRef.setHovering, false);
		},
		removeMouseRader : function(el) {
			el.removeEventListener("mouseover", this.cache.callbackRef.setHovering, false);
			el.removeEventListener("mouseout", this.cache.callbackRef.setHovering, false);
		},
		setHovering : function(evt) {
			if (evt.type == "mouseover") {
				this.is.hovering = true;
			}
			else if (evt.type == "mouseout") {
				this.is.hovering = false;
			}
		},
		listHandler : function(evt) {
			var targetEl = evt.target;
			// keydown event
			if (evt.type == "keydown" && targetEl == this.box) {
				console.log("keydown");
				var keyID = evt.keyCode;
				// up, down key
				if (keyID == 38 || keyID == 40) {
					// 위쪽화살표를 움직였을 때 포인터가 왼쪽으로 가는걸 방지하기 위해 기본 이벤트 해제
					evt.preventDefault();
					this.highlightRow(39 - keyID);
				}
				// enter key
				else if (keyID == 13) {
					// 엔터를 눌렀을 때 기본 이벤트가 실행되는걸 방지하기 위해 해제
					evt.preventDefault();
					this.selectEl(this.list.children[this.cache.row]);
				}
			}
			
			// click event
			else if (evt.type == "click" && targetEl.parentNode.parentNode == this.list && this.is.hovering == true) {
				this.is.hovering = false;
				this.selectEl(evt.target);
			}
			
			// mousemove event
			else if (evt.type == "mousemove") {
				if (this.is.highlighting == true) {
					this.highlightOut(this.cache.row);
				}
			}
		},
		
		selectEl : function(el) {
			var text = null;
			// 선택된 el이 없는 경우 검색창에 입력된 query가 됨.
			if (el === undefined) { text = this.box.value; }
			else { text = el.innerText; }
			
			text = this.removeNewline(text);
			this.box.value = text;
			
			// ajax통신이 일어나지 못하도록 캐싱된 마지막 검색 query를 바꾼다.
			this.cache.searchedQuery = text;
			this.removeList();
			this.box.form.submit();
		},
		
		removeNewline : function(text) {
			var index = text.lastIndexOf("\n");
			if (index == -1) return text;
			return text.substring(0, index);
		},
		
		highlightRow : function(change) {
			// 처음 입력시
			if (this.cache.row == null) {
				this.cache.row = 0;
				change = 0;
			}
			
			var cacheRow = this.cache.row;
			var currentRow = cacheRow - change;
			
			// ajax에서 응답받은 현재 row의 수와 비교함.
			if (currentRow >= this.row.currentCount) { currentRow = 0; }
			else if (currentRow < 0) { currentRow = this.row.currentCount-1; }

			this.highlightOut(cacheRow);
			this.highlightIn(currentRow);
			this.cache.row = currentRow;
		},

		highlightIn : function(rowNum) {
			this.is.highlighting = true;
			this.list.children[rowNum].className = "highlight";
		},
		highlightOut : function(rowNum) {
			if (rowNum !== null) {
				this.is.highlighting = false;
				this.list.children[rowNum].className = "";
			}
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.search.auto_complement = AutoComplement;
})();(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.search = WILDGOOSE.search || {};
	WILDGOOSE.search.more = WILDGOOSE.search.more || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Template = CAGE.util.template;
	var Fav = WILDGOOSE.ui.favorite;
	
	var More = {
		init: function(args) {
//			this.searchMoreBtn = document.querySelector(args.button);
//			this.searchResult = document.querySelector(args.container);
//			this.requestNum = args.requestNum;
//			this.template = Template.get({"url":args.templateUrl});
			this.searchMoreBtn = args.button;
			this.searchResult = args.container;
			this.requestNum = args.requestNum;
			this.template = args.template;
			
			
			// 더보기 버튼 클릭이벤트 설정
			if (this.searchMoreBtn != null) {
				this.searchMoreBtn.addEventListener("click", this._more.bind(this), false);
				this._selectStatusOfSearchMoreBtn();
			}
		},
		_more: function(evt) {
			// click evt
			var searchQuery = document.querySelector(".search-more .state-search-query").innerText;
			var curNum = document.querySelector(".search-more .state-search-curNum").innerText;
			// search
			var url = "/api/v1/search?q=" + searchQuery + "&start_item=" + curNum + "&how_many=" + this.requestNum;
			Ajax.GET({"url":url, "callback":this._responseHandler.bind(this)});
		},
		
		_responseHandler: function(rawD) {
			var userId = null;
			var reporters = JSON.parse(rawD)["data"]["reporters"];
			var isLogined = ((userId = this._getUserId()) != null)? true : false;
			
			// response data가 존재할 경우만 실행
			if (reporters.length != 0) {	
				var cards = this._makeReporterCards(isLogined, reporters);
				this._attachRecievedData(cards);
				var metaData = this._updateMetaData(cards.length);
				this._selectStatusOfSearchMoreBtn(metaData.curNum);
				Fav.updateFavs(metaData.curNum, this.requestNum);
				Fav.attatchEventToFavBtn(metaData.curNum, this.requestNum);
			}
		},
		
		// 현재 card의 개수를 업데이트
		_updateMetaData: function(updatedNum) {
			var curNumDiv = document.querySelector(".search-more .state-search-curNum");
			var curNum = parseInt(curNumDiv.innerText) + updatedNum;
			curNumDiv.innerText = curNum;
			
			return {"curNum": curNum};
		},
		
		// 더보기 버튼을 보여줄지 말지를 결정
		_selectStatusOfSearchMoreBtn: function(curNum) {
			var searchMore = document.querySelector(".search-more");
			var totalNumDiv = document.querySelector(".search-more .state-search-totalNum");
			if (totalNumDiv === null) {
				searchMore.setAttribute("style", "display: none;");
				return;
			}
			
			var totalNum = parseInt(totalNumDiv.innerText);
			if (totalNum <= curNum) {
				searchMore.setAttribute("style", "display: none;");
			}
		},
		
		// card template에 데이터를 담은 template array를 반환
		_makeReporterCards: function(isLogined, reporters) {
			var templateCompiler = Template.getCompiler();
			var className = "card card-reporter";
			var reporterNum = reporters.length;
			var cards = [];
		
			// card template을 cards array에 담음
			for (var i=0; i<reporterNum; i++) {
				var cardData = reporters[i];
				
				/*
				 * 로그인 상태에 따른 별을 보여주는 로직 추가 필요
				 * 
				if logined
				favorited reporter card's star must be turn on
				and stars have their own event-handler
				else
				stars have to be invisible
				 */
				if(isLogined){
//					var star = card.querySelector(".star");
//					console.log(star);
//					Util.removeClass(star, "invisible");
//					star.addEventListener("click", Fav.toggleFav, false);
				}
				var newLi = '<li class="' + className + '">' + templateCompiler(cardData, this.template) + '</li>';
				cards.push(newLi);
			}
			
			return cards;
		},
		
		_getUserId: function() {
			var userId = document.getElementById("userId");
			if(userId != null){
				userId = document.getElementById("userId").getAttribute('email');
			}
			return userId;
		},
		
		_attachRecievedData: function(cards) {
			this.searchResult.innerHTML += cards.join("");
		}	
	};
	
	// 공개 메서드 노출
	WILDGOOSE.search.more = More;
})();
(function() {	
	// 자주 사용하는 글로벌 객체 레퍼런스 확보
	var document = window.document;
	var console = window.console;

	// 사용할 네임 스페이스 확보	
	var WILDGOOSE = window.WILDGOOSE || {};
	WILDGOOSE.search = WILDGOOSE.search || {};

	// 의존성 주입
	var Ajax = CAGE.ajax;
	var Template = CAGE.util.template;
	var More = WILDGOOSE.search.more;
	var AutoComplement = WILDGOOSE.search.auto_complement;
	
	var Search = {
		init: function(args) {
			// search
			var search = args.search;
			if (search !== undefined) {
				this.form = {
					box: document.querySelector(search.box),
					container: document.querySelector(search.container)
				}
				
				this.search = {
					submit: document.querySelector(search.submit),
					requestNum: search.requestNum,
					templateURL: search.templateURL,
					template: Template.get({"url":search.templateURL})
				}
			}
			
			// autocompletion
			var autocompletion = args.autocompletion;
			if (autocompletion !== undefined) {
				this.auto = {
					list: document.querySelector(args.autocompletion.list),
					requestNum: autocompletion.requestNum
				}
				AutoComplement.init({searchBox: this.form.box, container: this.auto.list, requestNum: this.auto.requestNum});
			}
			
			// more
			var more = args.more;
			if (more !== undefined) {
				this.more = {
					button: document.querySelector(more.button)
				}
				More.init({button: this.more.button, container: this.form.container, template: this.search.template, requestNum: this.search.requestNum});
			}
		}
	};
	
	// 공개 메서드 노출
	WILDGOOSE.search = {
		init: Search.init
	}
})();
