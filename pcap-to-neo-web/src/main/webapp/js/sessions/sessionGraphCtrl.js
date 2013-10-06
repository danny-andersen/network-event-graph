/*jslint white:true */

function showGraph($scope, $window, coreIpAddr, sessions, colours) {
	var nodes = {};
    var element = document.getElementById('sessionGraph');
//     var element = $('#sessionGraph');
	var i, sigInst = sigma.init(element);
	sigInst.emptyGraph();
	sigInst.refresh();
	sigInst.drawingProperties({
		defaultLabelColor: '#fff',
		edgeColor: 'default',
		defaultEdgeColor: '#aaa'
    	// defaultEdgeType: 'curve'
    });

	$scope.filterSessions = function() {
		var direction = $scope.filter;
		sigInst.iterNodes(function(node) {
			if (node.color === colours.device) {
				//leave this one
				node.hidden = false;
				return;
			}
			switch (direction) {
				case('from'):
					if (node.color === colours.from) {
						node.hidden = false;
					} else {
						node.hidden = true;
					}
					break;
				case('to'):
					if (node.color === colours.to) {
						node.hidden = false;
					} else {
						node.hidden = true;
					}
					break;
				case('both'):
					if (node.color === colours.both) {
						node.hidden = false;
					} else {
						node.hidden = true;
					}
					break;
				case('all'):
					node.hidden = false;
					break;
				}
		});
		sigInst.draw();
	};

	//Show device node centrally
	sigInst.addNode(coreIpAddr, {
			label: coreIpAddr,
		    x: 0.5,
		    y: 0.5,
		    color: colours.device,
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
 		nodeColour[session.srcIpAddr] = nodeColour[session.srcIpAddr] === undefined ? colours.to : colours.both;
 		nodeColour[session.destIpAddr] = nodeColour[session.destIpAddr] === undefined ? colours.from : colours.both;
 	}
 	var sizeScale = 9 / maxCnt;
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
	sigInst.bind('downnodes', function(event) {
		var ipAddr = event.content;
		var path = "#/ipaddr/"+ipAddr;
		$scope.menuItems.push({"href" : path, "name": "IpAddr("+ipAddr+")" });
		var currentPath = $window.location.pathname;
		var url = currentPath + path;
		$window.location = url;
	});
}

function showKey(colours) {
	var canvas = document.getElementById("keyCanvas");
    var ctx = canvas.getContext("2d");
	ctx.font="12px Georgia";
	var spacing = 65;
	var spotSize = 5;
	var offset = spotSize + 2;
	var x = 5;
    ctx.beginPath();
    ctx.fillStyle ='#222';
	ctx.fillText("Key:",x+offset,10);
    ctx.fill();
    ctx.beginPath();
    x += spacing;
    ctx.fillStyle = colours.to;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
	ctx.fillText("Incoming",x+offset,10);
    ctx.fill();
    ctx.beginPath();
    ctx.fillStyle = colours.from;
    x += spacing;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
	ctx.fillText("Outgoing",x+offset,10);
    ctx.fill();
    ctx.beginPath();
    x += spacing;
    ctx.fillStyle = colours.both;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
	ctx.fillText("In+Out",x+offset,10);
    ctx.fill();
    ctx.beginPath();
    x += spacing;
    ctx.fillStyle = colours.device;
    ctx.arc(x, 5, 5, 0, Math.PI * 2, false);
	ctx.fillText("Device",x+offset,10);
    ctx.fill();
}

function SessionGraphCtrl($scope, $window, $routeParams, SessionsByIp, SessionsBySrcIp, SessionsByDestIp) {
	var ipAddr = $routeParams.ipAddr;
	var direction = $routeParams.direction;
	$scope.filter='all';
	if (direction === undefined || direction === "" || direction === 'both') {
		direction = 'from/to';
	}
	var colours = {
		from: '#0f0',
		to: '#f00', 
		both: '#ff0',
		device: '#22f'
	};
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
	showKey(colours);
	var sessions = [];
	switch (direction) {
		case 'from':
			sessions = SessionsBySrcIp.query({
				'ipAddr': ipAddr
			}, function() {
				showGraph($scope, $window, ipAddr, sessions, colours);
			});
			break;
		case 'to':
			sessions = SessionsByDestIp.query({
				'ipAddr': ipAddr
			}, function() {
				showGraph($scope, $window, ipAddr, sessions, colours);
			});
			break;
		default:
			sessions = SessionsByIp.query({
				'ipAddr': ipAddr
			}, function() {
				showGraph($scope, $window, ipAddr, sessions, colours);
			});
			break;
	}
}

