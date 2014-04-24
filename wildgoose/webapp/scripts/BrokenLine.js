// basic setting
window.addEventListener("load", init2, false);

function init2() {
	var condition = "day";
	var url = "/api/v1/reporters/1/number_of_articles?by="+condition;
	requestData(url, BrokenLine);

}

function BrokenLine(data) {
	
	var svgContainer = d3.select("#svg-brokenLine").append("svg:svg").attr(
			"width", 500).attr("height", 320)
			.attr("id", "brokenLineGraph");
	var backgroundColor = svgContainer.append("rect").attr("width", "100%").attr("height", "100%").attr("fill", "#26A88E");
	var text = svgContainer.append("text").attr("x", 370).attr("y", 40).attr(
			"font-size", "20px").attr("fill", "white").text("기사송고추이");

	// NumberIndex
	var label0 = svgContainer.append("text").attr("x", 7).attr("font-size",
			"18px").attr("fill", "white").attr("y", 285).text("0");
	var label1 = svgContainer.append("text").attr("x", 7).attr("font-size",
			"18px").attr("fill", "white").attr("y", 235).text("1");
	var label2 = svgContainer.append("text").attr("x", 7).attr("font-size",
			"18px").attr("fill", "white").attr("y", 185).text("2");
	var label3 = svgContainer.append("text").attr("x", 7).attr("font-size",
			"18px").attr("fill", "white").attr("y", 135).text("3")
	var label4 = svgContainer.append("text").attr("x", 7).attr("font-size",
			"18px").attr("fill", "white").attr("y", 85).text("4");

	// guidanceLine
	var guide1 = svgContainer.append("path").attr("d", "M 45 85 H580").attr(
			"stroke", "#7D7D7D").attr("stroke-width", 1).style(
			"stroke-dasharray", "5,8").attr("stroke-opacity", 0.6).attr("fill",
			"none");
	var guide2 = svgContainer.append("path").attr("d", "M 45 135 H580").attr(
			"stroke", "#7D7D7D").attr("stroke-width", 1).style(
			"stroke-dasharray", "5,8").attr("stroke-opacity", 0.6).attr("fill",
			"none");
	var guide3 = svgContainer.append("path").attr("d", "M 45 185 H580").attr(
			"stroke", "#7D7D7D").attr("stroke-width", 1).style(
			"stroke-dasharray", "5,8").attr("stroke-opacity", 0.6).attr("fill",
			"none");
	var guide4 = svgContainer.append("path").attr("d", "M 45 235 H580").attr(
			"stroke", "#7D7D7D").attr("stroke-width", 1).style(
			"stroke-dasharray", "5,8").attr("stroke-opacity", 0.6).attr("fill",
			"none");
	var guide5 = svgContainer.append("path").attr("d", "M 45 285 H580").attr(
			"stroke", "#7D7D7D").attr("stroke-width", 1).style(
			"stroke-dasharray", "5,8").attr("stroke-opacity", 0.6).attr("fill",
			"none");

	// variable values
	matching = [ {
		0 : 285,
		1 : 235,
		2 : 185,
		3 : 135,
		4 : 85
	} ];
	graphPositionX = [ 30, 100, 170, 240, 310, 380, 450 ];
	
	sampleData = JSON.parse(data)["data"];
	
	var keys = new Array();
	for(i = 0; i<sampleData.length; i++){
		keys.push(Object.keys(sampleData[i])[0]);
	}
	
	// dateLabel
	var labelDate1 = svgContainer.append("text").attr("x", 30).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[0]);
	var labelDate2 = svgContainer.append("text").attr("x", 100).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[1]);
	var labelDate3 = svgContainer.append("text").attr("x", 170).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[2]);
	var labelDate4 = svgContainer.append("text").attr("x", 240).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[3]);
	var labelDate5 = svgContainer.append("text").attr("x", 310).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[4]);
	var labelDate6 = svgContainer.append("text").attr("x", 380).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[5]);
	var labelDate7 = svgContainer.append("text").attr("x", 450).attr(
			"font-size", "18px").attr("fill", "white").attr("y", 315).text(
			keys[6]);

	
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

	var lineFunction = d3.svg.line().x(function(d) {
		return d.x;
	}).y(function(d) {
		return d.y;
	}).interpolate("linear");

	var graph = svgContainer.append("path").attr("stroke", "white").attr(
			"stroke-width", 2).attr("fill", "none").attr("stroke-linecap",
			"round").attr("d", lineFunction(graphData));
}
