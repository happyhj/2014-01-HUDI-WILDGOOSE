function startRequest() {
	xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange = handleStateChange;
	xmlHttp.open("GET", "http://127.0.0.1:8080/api/v1/reporters/232/number_of_hook_keywords", true);
	xmlHttp.send(null);
}
function handleStateChange() {
	if(xmlHttp.readyState == 4) {
		if(xmlHttp.status == 200) {
			drawGraph(xmlHttp.responseText);
		}
	}
}
function drawGraph(jsonText) {
	var jsonResponse = JSON.parse(jsonText)

	var div = document.createElement('div')
	div.setAttribute('class', 'graph')

	var graph = document.createElementNS('http://www.w3.org/2000/svg', 'svg')
	graph.setAttribute('width', 400)
	graph.setAttribute('height', 400)
	graph.setAttribute('viewBox', "0 0 400 400")

	var group = document.createElementNS('http://www.w3.org/2000/svg', 'g')
	group.setAttribute('transform', 'translate(0, 400) scale(1, -1)')

	var datalist = jsonResponse['data']

	for (var i=0, x=0, width=80; i<datalist.length; i++, x+=width) {
		var data = datalist[i]
		var key = Object.keys(data)[0]

		var bar = document.createElementNS('http://www.w3.org/2000/svg', 'rect')
		bar.setAttribute('x', x)
		bar.setAttribute('y', 0)
		bar.setAttribute('width', width)
		bar.setAttribute('height', data[key] * 20)
		bar.setAttribute('fill', getRandomColor())

		var text = document.createElementNS('http://www.w3.org/2000/svg', 'text')
		text.setAttribute('x', x)
		text.setAttribute('y', 300)
		text.setAttribute('font-size', 40)
		text.setAttribute('fill', '#000000')
		text.setAttribute('transform', 'translate(0, 400) scale(1, -1)')
		
		var textNode = document.createTextNode(key);

   		text.appendChild(textNode)
		group.appendChild(bar)
		group.appendChild(text)
	}
	graph.appendChild(group)
	div.appendChild(graph)
	document.body.appendChild(div)
	
}
function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

startRequest()