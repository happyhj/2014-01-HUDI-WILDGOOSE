// basic setting
//window.addEventListener("load", init2, false);
//
//function init2() {
//	var condition = "day";
//	var url = "/api/v1/reporters/1/number_of_articles?by="+condition;
//	requestData(url, BrokenLine);
//
//}

function brokenLineGraph(rawD) {
	
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
}
