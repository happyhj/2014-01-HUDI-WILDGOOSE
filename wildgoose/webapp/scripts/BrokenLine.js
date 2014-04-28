// basic setting
//window.addEventListener("load", init2, false);
//
//function init2() {
//	var condition = "day";
//	var url = "/api/v1/reporters/1/number_of_articles?by="+condition;
//	requestData(url, BrokenLine);
//
//}

function BrokenLine(data) {
	
	var svgContainer = d3.select("#brokenline-graph > .graph").append("svg:svg")
	.attr("width", 500).attr("height", 320)
	.attr("id", "brokenLineGraph");
	
	var backgroundColor = svgContainer.append("rect").attr("width", "100%").attr("height", "100%").attr("fill", "#26A88E");
	

	// NumberIndex
	var numberIndexYPosition = [
	{"y_pos": 285, "num": "0"}, {"y_pos": 235, "num": "1"}, 
	{"y_pos": 185, "num": "2"}, {"y_pos": 135, "num": "3"}, {"y_pos": 85, "num": "4"}];
	var numberIndexes = svgContainer.selectAll("text").data(numberIndexYPosition).enter().append("text");
	numberIndexes.attr("x", 7).attr("font-size","18").attr("fill", "white").attr("y", function (d) {return d.y_pos;}).text(function(d) {return d.num;});

	// guidanceLine
	guidanceLinePosition = [ "M 45 85 H580", "M 45 135 H580", "M 45 185 H580", "M 45 235 H580", "M 45 285 H580" ];
	
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
	
	sampleData = JSON.parse(data)["data"];
	
	var keys = new Array();
	for(i = 0; i<sampleData.length; i++){
		keys.push(Object.keys(sampleData[i])[0]);
	}
	
	// date label
	for(labelIndex = 0; labelIndex < keys.length; labelIndex++){
		svgContainer.append("text").attr("font-size", "18").attr("fill", "white").attr("y", 315)
		.attr("x", 30 + (70 * labelIndex)).text(keys[labelIndex]);
	}

	// graph
	var graphData = [ {
		"x" : graphPositionX[0],
		"y" : matching[0][sampleData[0][keys[0]]]
	}, {
		"x" : graphPositionX[1],
		"y" : matching[0][sampleData[1][keys[1]]]
	}, {
		"x" : graphPositionX[2],
		"y" : matching[0][sampleData[2][keys[2]]]
	}, {
		"x" : graphPositionX[3],
		"y" : matching[0][sampleData[3][keys[3]]]
	}, {
		"x" : graphPositionX[4],
		"y" : matching[0][sampleData[4][keys[4]]]
	}, {
		"x" : graphPositionX[5],
		"y" : matching[0][sampleData[5][keys[5]]]
	}, {
		"x" : graphPositionX[6],
		"y" : matching[0][sampleData[6][keys[6]]]
	} ]
	
	

	var lineFunction = d3.svg.line().x(function(d) {return d.x;})
			.y(function(d) {return d.y;}).interpolate("linear");

	var graph = svgContainer.append("path").attr("stroke", "white")
		.attr("stroke-width", 2).attr("fill", "none")
		.attr("stroke-linecap","round").attr("d", lineFunction(graphData));

	var text = svgContainer.append("text").attr("x", 370).attr("y", 40)
	.attr("font-size", "20").attr("fill", "white").text("기사송고추이");
}
