/**
 * using D3.js
 */
window.addEventListener("load", initGraph, false);


function initGraph() {
	var reporterId = window.location.pathname.split("/")[2];
	var graphInfo = {
		"donut":{
			"url": "/api/v1/reporters/:reporterId/number_of_articles?by=section",
			"function": donutGraph
		},
		"brokenLine":{
			"url": "/api/v1/reporters/:reporterId/number_of_articles?by=day",
			"function": brokenLineGraph
		},
		 "bar":{
		 	"url": "/api/v1/reporters/:reporterId/number_of_hook_keywords",
		 	"function": barGraph
		 },
		 "radar":{
			 	"url": "/api/v1/reporters/:reporterId/stat_points",
			 	"function": radarGraph
		 }
	};

	for (var graphName in graphInfo) {
		var graph = graphInfo[graphName];
		var url = graph['url'].replace(":reporterId", reporterId);
		Ajax.GET(url, graph['function']);
	}
}







