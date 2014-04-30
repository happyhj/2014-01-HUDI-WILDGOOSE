function radarGraph(data) {

	//var stat_data = JSON.parse(data)["data"];
	var stat_data = {
			"통솔": Math.random()*10,
			"매력": Math.random()*10,
			"지력": Math.random()*10,
			"정치": Math.random()*10,
			"무력": Math.random()*10
	};
	var object = {
		"container-width" : 400,
		"container-height" : 400,
		"circle-scale" : 0.75,
		"color" : "red"	
	};

	var svgContainer = d3.select("#radar-graph > .graph").append("svg")
	.style("width", "100%").style("height", "100%").attr("id", "radarGraph")
	.attr("viewBox", "0 0 400 400");

	var backgroundColor = svgContainer.append("rect").attr("width", "100%").attr("height", "100%").attr("fill", "#FFF");
var radius = object["container-width"]/2 * object["circle-scale"];
 for (var i=0;i<10;i++){
 var circle = svgContainer.append("ellipse")
                          .attr("cx", object["container-width"]/2)
                          .attr("cy", object["container-height"]/2)
                         .attr("rx", radius * (1 - 0.1*i))
                         .attr("ry", radius * (1 - 0.1*i))
                            .attr("stroke", "#AAA")
                            .attr("stroke-width", 0.5)
                            .attr("fill", "white")                                               
                            .attr("fill-opacity", 0);                                               
}


// (200,200) 을 중심으로 화전시켜야 함
function rotation (point, degree) {
	var x = point.x * Math.cos(degree) - point.y * Math.sin(degree);
	var y = point.x * Math.sin(degree) + point.y * Math.cos(degree);
	return {"x":x,"y":y};
}

var num_of_stats = Object.keys(stat_data).length;
var i=0;
 var lineData = [];
for(var stat in stat_data ) {
	var point = {
		x: 0,
		y: -1*stat_data[stat]/10 * radius 
	};
	point = rotation(point,2*Math.PI/num_of_stats*i);
	point.x += object["container-width"]/2;
	point.y += object["container-height"]/2;
	lineData.push(point);
	i++;
}
lineData.push(lineData[0]);

 var lineFunction = d3.svg.line()
                          .x(function(d) { return d.x; })
                          .y(function(d) { return d.y; })
                         .interpolate("linear");
                    
var vertexData = [];
for(var i =0;i<num_of_stats;i++ ) {
	var point = {
		x: 0,
		y: -1*radius 
	};
	point = rotation(point,2*Math.PI/num_of_stats*i);
	var point_label = {
		x: 0,
		y: -1*(radius + 25)
	};
	point_label = rotation(point_label,2*Math.PI/num_of_stats*i);
	point.x += object["container-width"]/2;
	point.y += object["container-height"]/2;
	point.title = Object.keys(stat_data)[i];
	point.value = stat_data[point.title];
	point.x_t = point_label.x + object["container-width"]/2;
	point.y_t = point_label.y + object["container-height"]/2;
	
	vertexData.push(point);
}
for(var j=0;j<vertexData.length;j++){
var lineGraph = svgContainer.append("path")
                            .attr("d", lineFunction([vertexData[j],{x:object["container-width"]/2,y:object["container-width"]/2}]))
                            .attr("stroke", "#AAA")
                            .attr("stroke-width", .5)
                            .attr("fill", "yellow")                     
                            .attr("fill-opacity", .6);  
} 

var lineGraph = svgContainer.append("path")
                            .attr("d", lineFunction(lineData))
                            .attr("stroke", "#222")
                            .attr("stroke-width", .5)
                            .attr("stroke-opacity", .1)
                            .attr("fill", object.color )                     
                            .attr("fill-opacity", .4);  

//Add the SVG Text Element to the svgContainer
var text = svgContainer.selectAll("text")
                        .data(vertexData)
                        .enter()
                        .append("text");
//Add SVG Text Element Attributes
var textLabels = text
                 .attr("x", function(d) {
                 return d.x_t-16; })
                  .attr("y", function(d) {  if(d.title==="통솔") return d.y_t+1; return d.y_t  -4; })
                 .text( function (d) { return d.title; })
                 .attr("font-family", "sans-serif")
                 .attr("font-size", "18px")
                 .attr("fill", "#AAA")  
                 .append('svg:tspan')
				 .attr('x', function(d) { return d.x_t-16; })
				 .attr('dy', 20)
                 .attr("font-size", "23px")
                 .attr("fill", "#444")  
				 .text(function(d) { return parseInt(d.value*10)/10; });

}