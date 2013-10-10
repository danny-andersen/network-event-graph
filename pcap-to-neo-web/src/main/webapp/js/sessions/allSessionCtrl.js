/*jslint white:true */

function AllSessionCtrl($scope, $window, SessionsByIp, DeviceModel, DeviceByIpAddr, LocalDevices, RemoteDevices) {
	var nodes = {};
	var edges = {};
	$scope.data = {};
	$scope.data.devices = DeviceModel.getDevices();
	$scope.deviceCnt = 0;
	var colours = {
		from: '#0f0',
		to: '#f00', 
		both: '#ff0',
		device: '#22f'
	};
	var setDevices = function(devices) {
			DeviceModel.devices = devices;
			$scope.deviceCnt = devices.length;
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
	$scope.stopLayout = function() {
		$scope.sigInst.stopForceAtlas2();
	};
	$scope.startLayout = function() {
		$scope.sigInst.startForceAtlas2();
	};
	showKey(colours);
	$scope.plotGraph = function() {
		var i, j, ipAddr, devices = [], sessions = [], ipaddrs = [];
		$scope.data = {};
		$scope.data.devices = DeviceModel.getDevices();
		devices = $scope.data.devices;
		initGraph($scope, colours);
		//For each device, plot the ipaddr node for it (so that it has the right colour)
		for (i=0; i<devices.length; i++) {
			ipaddrs = devices[i].ipaddr;
			for (j=0; j<ipaddrs.length; j++) {
				ipAddr = ipaddrs[j].ipAddr;
				if (nodes[ipAddr] === undefined) {
					$scope.sigInst.addNode(ipAddr, {
						label: ipAddr,
					    x: Math.random(),
					    y: Math.random(),
					    color: colours.device,
					    size: 5
					});
					nodes[ipAddr] = true;
				}
			}
		}

		//For each device, retrieve sessions and then plot them
		for (i=0; i<devices.length; i++) {
			ipaddrs = devices[i].ipaddr;
			for (j=0; j<ipaddrs.length; j++) {
				ipAddr = ipaddrs[j].ipAddr;
				sessions = SessionsByIp.query({
					'ipAddr': ipAddr
				}, function(sessions) {
					plotSessions($scope, nodes, edges, $window, ipAddr, sessions, colours);
				});
			}
		}
	    $scope.sigInst.startForceAtlas2();
	};
}