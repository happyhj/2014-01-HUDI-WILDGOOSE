function barGraph(jsonString) {
	var data = JSON.parse(jsonString)['data'];
	
	var div = document.querySelector("#bar-graph > .graph");
	var graph = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
	graph.setAttribute('style', 'width:100%; height:300px;');
	graph.setAttribute('viewBox', "0 0 400 400");
	
	var group = document.createElementNS('http://www.w3.org/2000/svg', 'g');
	group.setAttribute('transform', 'translate(0, 400) scale(1, -1)');

	for (var i=0, x=0, width=80; i<data.length; i++, x+=width) {
		var bardata = data[i];
		var key = Object.keys(bardata)[0];

		var bar = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
		bar.setAttribute('x', x);
		bar.setAttribute('y', 0);
		bar.setAttribute('width', width);
		bar.setAttribute('height', bardata[key] * 20);
		bar.setAttribute('fill', getRandomColor());

		var text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
		text.setAttribute('x', x);
		text.setAttribute('y', 300);
		text.setAttribute('font-size', 40);
		text.setAttribute('fill', '#000000');
		text.setAttribute('transform', 'translate(0, 400) scale(1, -1)');
		
		var textNode = document.createTextNode(key);

   		text.appendChild(textNode);
		group.appendChild(bar);
		group.appendChild(text);
	}
	graph.appendChild(group);
	div.appendChild(graph);
	
}

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}