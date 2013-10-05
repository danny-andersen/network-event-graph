/*jslint white:true */

function showGraph(coreIpAddr, sessions) {
	var nodes = {};
    var element = document.getElementById('sessionGraph');
//     var element = $('#sessionGraph');
	var i, sigInst = sigma.init(element);
	sigInst.emptyGraph();
	sigInst.refresh();
	sigInst.drawingProperties({
		defaultLabelColour: '#fff'
	});

	sigInst.addNode(coreIpAddr, {
			label: coreIpAddr,
		    x: 0.5,
		    y: 0.5,
		    color: '#00f',
		    size: 5
	});
	nodes[coreIpAddr] = true;
	//Find max and min session size
	var maxCnt = -1;
	var minCnt = 9999999;
 	var nodeColour = {};
 	var session;
 	for (i=0; i<sessions.length; i++) {
 		session = sessions[i];
 		var cnt = session.numSessions;
 		if (cnt < minCnt) {
 			minCnt = cnt;
 		}
 		if (maxCnt < cnt) {
 			maxCnt = cnt;
 		}
 		nodeColour[session.srcIpAddr] = nodeColour[session.srcIpAddr] === undefined ? '#f00' : '#ff0';
 		nodeColour[session.destIpAddr] = nodeColour[session.destIpAddr] === undefined ? '#0f0' : '#ff0';
 	}
 	var sizeScale = 9 / (maxCnt - minCnt);
 	var minSize = 1;
 	for (i=0; i<sessions.length; i++) {
		session = sessions[i];
		//Check node exists
		var srcIp = session.srcIpAddr;
		var size = session.numSessions * sizeScale;
		var newNode = false;
		if (nodes[srcIp] === undefined) {
	 		sigInst.addNode(srcIp, {
				label: session.srcHostname === null ? session.srcIpAddr : session.srcHostName,
			    x: Math.random(),
			    y: Math.random(),
			    color: nodeColour[srcIp],
			    size: size + minSize
	 		});
	 		nodes[srcIp] = true;
	 		newNode = true;
	 	}
		var destIp = session.destIpAddr;
		if (nodes[destIp] === undefined) {
	 		sigInst.addNode(destIp,	{
				label: session.destHostname === null ? destIp : session.destHostName,
			    x: Math.random(),
			    y: Math.random(),
			    color: nodeColour[destIp],
			    size: size + minSize
			});
	 		nodes[destIp] = true;
	 		newNode = true;
	 	}
	 	if (newNode) {
	 		sigInst.addEdge(srcIp + '-' + destIp, srcIp, destIp);
	 	}
 	}
    sigInst.draw();
}

function SessionGraphCtrl($scope, $routeParams, SessionsByIp, SessionsBySrcIp, SessionsByDestIp) {
	var ipAddr = $routeParams.ipAddr;
	var direction = $routeParams.direction;
	if (direction === undefined || direction === "" || direction === 'both') {
		direction = 'from/to';
	}
	$scope.direction = direction;
	$scope.ipaddr = ipAddr;
	var startTime = $routeParams.start;
	var endTime = $routeParams.end;
	$scope.hasDate = false;
	if (startTime !== undefined && endTime !== undefined) {
		var d = new Date();
		d.setTime(startTime);
		$scope.startDate = d.toUTCString();
		d.setTime(endTime);
		$scope.endDate = d.toUTCString();
		$scope.hasDate = true;
	}
    var canvas = document.getElementById("keyCanvas");
    var context = canvas.getContext("2d");
    context.beginPath();
    context.arc(5, 5, 5, 0, Math.PI * 2, false);
    context.fillStyle ='#f00';
    context.fill();
    context.beginPath();
    context.arc(15, 5, 5, 0, Math.PI * 2, false);
    context.fillStyle ='#0f0';
    context.fill();
	var sessions = [];
	switch (direction) {
		case 'from':
			sessions = SessionsBySrcIp.query({
				'ipAddr': ipAddr
			}, function() {
				showGraph(ipAddr, sessions);
			});
			break;
		case 'to':
			sessions = SessionsByDestIp.query({
				'ipAddr': ipAddr
			}, function() {
				showGraph(ipAddr, sessions);
			});
			break;
		default:
			sessions = SessionsByIp.query({
				'ipAddr': ipAddr
			}, function() {
				showGraph(ipAddr, sessions);
			});
			break;
	}
}

