/*jslint white:true */

function DeviceCtrl($scope, DeviceModel, DeviceByIpAddr, LocalDevices, RemoteDevices) {
	$scope.data = {};
	$scope.data.devices = DeviceModel.getDevices();
	var setDevices = function(devices) {
			DeviceModel.devices = devices;
		};
	$scope.getDevices = function() {
		$scope.data.devices = DeviceByIpAddr.query({
			'ipAddr': $scope.ipaddr
		}, setDevices);
	};
	$scope.getLocalDevices = function() {
		$scope.data.devices = LocalDevices.query({}, setDevices);
	};
	$scope.getRemoteDevices = function() {
		$scope.data.devices = RemoteDevices.query({}, setDevices);
	};
	$scope.addDevice = function(deviceId) {
		$scope.menuItems.push({"href" : "#/device/"+deviceId, "name": "Device(id="+deviceId+")" });
	};
}

function showSessionGraph($scope) {
	var coreIpAddr = $scope.ipAddr;
	var sessions = $scope.currentSessions;
	var nodes = $scope.nodes;
	//Session: numSessions srcIpAddr srcHostName destIpAddr destHostName
    var element = document.getElementById($scope.graph);
//     var element = $('#sessionGraph');
	var i, sigInst = sigma.init(element);
	sigInst.emptyGraph();
	sigInst.refresh();
	sigInst.drawingProperties({
		defaultLabelColour: '#fff'
	});

// var i, sigInst = sigma.init($('#sessionGraph'));
	// sigInst.addNode('hello', {
 //      label: 'Hello',
 //      x: -0.51,
 //      y: -0.51,
 //      color: '#f00',
 //      size: 5,
 //    }).addNode('world', {
 //      label: 'World!',
 //      x: 0.99,
 //      y: 0.99,
 //      color: '#0f0',
 //      size: 5,
 //    }).addEdge('hello_world', 'hello','world');
	if (nodes === undefined) {
	    nodes = {};
	    $scope.nodes = nodes;
	}
	if (nodes[coreIpAddr] === undefined) {
		sigInst.addNode(coreIpAddr, {
			label: coreIpAddr,
		    x: 0.5,
		    y: 0.5,
		    color: '#00f',
		    size: 5
		});
		nodes[coreIpAddr] = true;
	}
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
 		} else if (maxCnt < cnt) {
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

function DeviceDetailCtrl($scope, $routeParams, $timeout, DeviceModel, DeviceDetailById, WebSitesByIp, WebSitesByHostname,
	SessionsByIp, SessionsBySrcIp, SessionsByDestIp) {
	$scope.detail = {};
	$scope.detail.id = $routeParams.deviceId;
	$scope.detail.device = DeviceModel.getDeviceDetail($scope.detail.id);
	if ($scope.detail.device === undefined) {
		$scope.detail.device = DeviceDetailById.get({
			'deviceId': $scope.detail.id
		}, function(device) {
			DeviceModel.setDeviceDetail($scope.detail.id, device);
		});
	}
    $('#fromTimepicker').timepicker({
    	showMeridian: false
    });
    $('#toTimepicker').timepicker({
    	showMeridian: false
    });

	//Date control
  var d = new Date();
  d.setHours(0);
  d.setMinutes(0);
  $scope.fromDate = d;
  d = new Date();
  d.setHours(0);
  d.setMinutes(0);
  $scope.toDate = d;
  $scope.fromOpened = false;
  $scope.toOpened = false;

  $scope.dateOptions = {
    'year-format': "'yy'",
    'starting-day': 1
  };

  $scope.fromOpen = function() {
    $timeout(function() {
      $scope.fromOpened = true;
    });
  };

  $scope.toOpen = function() {
    $timeout(function() {
      $scope.toOpened = true;
    });
  };

    //Tabs
	$scope.navTabs = {};
	$scope.navTabs.protoTab = 0;
	$scope.navTabs.webTab = 1;
	$scope.navTabs.allTab = 2;
	$scope.navTabs.fromTab = 3;
	$scope.navTabs.toTab = 4;
	$scope.navTabs.tabs = [{
		active: true,
		url: ""
	},	{
		active: false,
		url: ""
	}, 	{
		active: false,
		url: "partials/sessionTable.html"
	}, {
		active: false,
		url: "partials/sessionTable.html"
	}, {
		active: false,
		url: "partials/sessionTable.html"
	}];

	$scope.showSessionSrc = function(session) {
		return session.srcHostname === null ? session.srcIpAddr : session.srcHostName;
	};

	$scope.showSessionDest = function(session) {
		return session.destHostname === null ? session.destIpAddr : session.destHostName;
	};

	$scope.getWebSitesVisitedByIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.webTab].active = true;
		$scope.webAddress = ipaddr;
		$scope.detail.device.websites = [];
		$scope.detail.device.websites = WebSitesByIp.query({
			'ipAddr': ipaddr
		});
	};

	$scope.getWebSitesVisitedByHost = function() {
		$scope.navTabs.tabs[$scope.navTabs.webTab].active = true;
		$scope.webAddress = $scope.detail.device.hostName;
		$scope.detail.device.websites = [];
		$scope.detail.device.websites = WebSitesByHostname.query({
			'hostName': $scope.detail.device.hostName
		});
	};

	$scope.getSessionsByIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.allTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from/to";
		$scope.detail.device.allSessions = SessionsByIp.query({
			'ipAddr': ipaddr
		});
		return $scope.detail.device.allSessions;
	};

	$scope.getSessionsBySrcIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.fromTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from";
		$scope.detail.device.fromSessions = SessionsBySrcIp.query({
			'ipAddr': ipaddr
		});
		return $scope.detail.device.fromSessions;
	};

	$scope.getSessionsByDestIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.toTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "to";
		$scope.detail.device.toSessions = SessionsByDestIp.query({
			'ipAddr': ipaddr
		});
		return $scope.detail.device.toSessions;
	};

	$scope.setSessions = function(tabId) {
		if (tabId === $scope.navTabs.webTab) {
			$scope.detail.device.websites = [];
			if ($scope.ipAddr === undefined) {
				$scope.webAddress = $scope.detail.device.hostName;
				$scope.detail.device.websites = WebSitesByHostname.query({
					'hostName': $scope.detail.device.hostName
				});
			} else {
				$scope.webAddress = $scope.ipAddr;
				$scope.detail.device.websites = WebSitesByIp.query({
					'ipAddr': $scope.ipAddr
				});
			}
		} else if (tabId === $scope.navTabs.allTab) {
			if ($scope.detail.device.allSessions !== undefined && $scope.detail.device.allSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.allSessions;
			} else {
				$scope.currentSessions = $scope.getSessionsByIp($scope.ipAddr);
			}
			$scope.graph = "allSessionGraph";
			$scope.nodes = $scope.detail.device.allNodes;
		} else if (tabId === $scope.navTabs.fromTab) {
			if ($scope.detail.device.fromSessions !== undefined && $scope.detail.device.fromSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.fromSessions;
			} else {
				$scope.currentSessions = $scope.getSessionsBySrcIp($scope.ipAddr);
			}
			$scope.graph = "fromSessionGraph";
			$scope.nodes = $scope.detail.device.fromNodes;
		} else if (tabId === $scope.navTabs.toTab) {
			if ($scope.detail.device.toSessions !== undefined && $scope.detail.device.toSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.toSessions;
			} else {
				$scope.currentSessions = $scope.getSessionsByDestIp($scope.ipAddr);
			}
			$scope.graph = "toSessionGraph";
			$scope.nodes = $scope.detail.device.toNodes;
		}
	};

	//Graphing
	$scope.graphTable="table";
	$scope.tableOnly = true;
	$scope.showGraphTable = function(graphTable) {
		// console.log("radio: " + graphTable);
		switch(graphTable) {
			case 'graph':
				$scope.graphOnly=true;
				$scope.tableOnly=false;
				showSessionGraph($scope);
				break;
			case 'both':
				$scope.graphOnly=false;
				$scope.tableOnly=false;
				showSessionGraph($scope);
				break;
			case 'table':
				$scope.graphOnly=false;
				$scope.tableOnly=true;
				break;
			default:
		}
		switch($scope.graph) {
			case 'allSessionGraph':
				$scope.detail.device.allNodes = $scope.nodes;
				break;
			case 'fromSessionGraph':
				$scope.detail.device.fromNodes = $scope.nodes;
				break;
			case 'toSessionGraph':
				$scope.detail.device.toNodes = $scope.nodes;
				break;
			default:
		}

	};
}

