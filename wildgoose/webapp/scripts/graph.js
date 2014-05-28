/**
 * using D3.js
 */
window.addEventListener("load", initGraph, false);

function initGraph() {
	var pieGraphParams = {
		type : "pie",
		selector : "#donut-graph > .graph"
	}
	var pieGraph = new Graph(pieGraphParams);
	pieGraph.init();
	
	
	var reporterId = window.location.pathname.split("/")[2];
	var graphInfo = {
		"donut":{
			"url": "/api/v1/reporters/:reporterId/statistics?data=number_of_articles&by=section",
			"function": pieGraph.render
		},
		"brokenLine":{
			"url": "/api/v1/reporters/:reporterId/statistics?data=number_of_articles&by=day",
			"function": brokenLineGraph
		},
		"radar":{
		 	"url": "/api/v1/reporters/:reporterId/statistics?data=stat_points",
		 	"function": radarGraph
		}
	};

	for (var graphName in graphInfo) {
		var graph = graphInfo[graphName];
		var url = graph['url'].replace(":reporterId", reporterId);
		Ajax.GET(url, graph['function']);
	}
}

function Graph(params) {
	this.type = params.type;
	this.vis = d3.select(params.selector).append("svg")  //create the SVG element inside the <body>
	
	this.scale = 1.2 // scale pie chart (default 1.2)
	this.color = d3.scale.category20c(); //builtin range of colors
	
	this.w = 300;
	this.h = 300;
	this.r = 100;
}

Graph.prototype = {
	constructor : Graph,
	init : function() {
		
		
		var arc = d3.svg.arc() //this will create <path> elements for us using arc data
		.outerRadius(this.r*this.pie_scale);
		
		this.arcs = this.vis.selectAll("g.slice") //this selects all <g> elements with class slice (there aren't any yet)
		.data(this.pie) //associate the generated pie data (an array of arcs, each having startAngle, endAngle and value properties) 
		.enter() //this will create <g> elements for every "extra" data element that should be associated with a selection. The result is creating a <g> for every object in the data array
		.append("svg:g") //create a group to hold each slice (we will have a <path> and a <text> element associated with each slice)
		.attr("class", "slice"); //allow us to style things in the slices (like text)
		
	},
	render : function(data) {
		this.vis.data([ data ]) //associate our data with the document
		.attr("viewBox", "0 0 300 300")
		.attr("style", "width: 100%; height: 300px;")
		.append("svg:g")
		.attr("transform", "translate(" + this.w/2 + "," + this.h/2 + ")") //move the center of the pie chart from 0, 0 to radius, radius
	},
	
	receiveData : function(rawD) {
		var realData = JSON.parse(rawD);
		var data = realData["data"];
		this.render(data);
		this.pie();
		this.fill();
		this.text();
	},
	
	pie : function() {
		this.pie = d3.layout.pie() //this will create arc data for us given a list of values
		.value(function(d) {
			return d.value;
		}); //we must tell it out to access the value of each element in our data array
	},
	fill : function() {
		this.arcs.append("svg:path")
		.attr("fill", function(d, i) {
			return color(i);
		}) //set the color for each slice to be chosen from the color function defined above
		.attr("d", arc); //this creates the actual SVG path using the associated data (pie) with the arc drawing function
	},
	text : function() {
		this.arcs.append("svg:text") //add a label to each slice
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
	}
	
}


function Graph(obj) {
	// data
	this.type = obj.type;
	this.selector = obj.selector
	
	// initialize
	this.init();
};

Graph.prototype = {
	constructor : Graph,
	init : function() {
		
	},
	render : function() {
		
	},
	receiveData : function() {
		
	},
	transform : function() {
		
	}
}


/*
 * { type : "pie|rader|brokenline|bar",
 *   select : "css-selector",
 *   size : {width, height, radius}
 * }
 */ 
var donutGraphParam = {type:"pie", selector:"#donut-graph > .graph", size:{width:300, height:300, radius:100}};
var donutGraph = new Graph(donutGraphParam);


function donutGraph (rawD) {
	var realData = JSON.parse(rawD);
	var data = realData["data"];
	
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
}



