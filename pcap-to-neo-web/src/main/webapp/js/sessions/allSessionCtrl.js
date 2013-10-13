/*jslint white:true */

function AllSessionCtrl($scope, $window, SessionsByIp, DeviceModel, DeviceByIpAddr, LocalDevices, RemoteDevices) {
	var nodes = {};
	var edges = {};
	$scope.data = {};
	$scope.data.devices = DeviceModel.getDevices();
	$scope.deviceCnt = 0;
	$scope.deviceLoading = false;
	$scope.graphLoading = false;
	var colours = {
		from: '#0f0',
		to: '#f00',
		both: '#ff0',
		device: '#22f'
	};

	$scope.timeType = -1;
	$scope.showTimePicker = false;
	$scope.fromDate = 0;
	$scope.fromTime = "00:00";
	// $scope.timepicker = {
	// 	"time": "00:00"
	// };
	var d = new Date();
	d.setHours(0);
	d.setMinutes(0);
	$scope.toDate = d;
	$scope.toTime = "00:00";
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
	$scope.setTimePeriod = function() {
		var d, t;
		switch ($scope.timeType) {
			case 0:
				$scope.showTimePicker = true;
				d = new Date();
				d.setHours(0);
				d.setMinutes(0);
				$scope.fromDate = d;
				break;
			case -1:
				$scope.fromDate = 0;
				$scope.showTimePicker = false;
				break;
			default:
				$scope.showTimePicker = true;
				t = new Date().getTime();
				t -= $scope.timeType * 24 * 3600 * 1000;
				d = new Date(t);
				d.setHours(0);
				d.setMinutes(0);
				$scope.fromDate = d;
				break;
		}
	};

	var setDevices = function(devices) {
		DeviceModel.devices = devices;
		$scope.deviceCnt = devices.length;
		$scope.deviceLoading = false;
	};
	$scope.getDevices = function() {
		$scope.deviceLoading = true;
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
		if (!$scope.graphLoading) {
			$scope.sigInst.stopForceAtlas2();
		}
	};
	$scope.startLayout = function() {
		if (!$scope.graphLoading) {
			$scope.sigInst.startForceAtlas2();
		}
	};
	showKey(colours);
	$scope.plotGraph = function() {
		var i, j, ipAddr, devices = [],
			sessions = [],
			ipaddrs = [];
		$scope.graphLoading = true;
		$scope.data = {};
		$scope.data.devices = DeviceModel.getDevices();
		devices = $scope.data.devices;
		initGraph($scope, colours);
		//For each device, plot the ipaddr node for it (so that it has the right colour)
		for (i = 0; i < devices.length; i++) {
			ipaddrs = devices[i].ipaddr;
			for (j = 0; j < ipaddrs.length; j++) {
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

		var params = {
			'ipAddr': ipAddr
		};
		if ($scope.timeType !== -1) {
			params.start = ($scope.fromDate.getTime() / 1000).toFixed(0);
			params.end = ($scope.toDate.getTime() / 1000).toFixed(0);
		}
		//For each device, retrieve sessions and then plot them
		for (i = 0; i < devices.length; i++) {
			ipaddrs = devices[i].ipaddr;
			for (j = 0; j < ipaddrs.length; j++) {
				ipAddr = ipaddrs[j].ipAddr;
				params.ipAddr = ipAddr;
				sessions = SessionsByIp.query(params, function(sessions) {
					plotSessions($scope, nodes, edges, $window, ipAddr, sessions, colours);
				});
			}
		}
		$scope.graphLoading = false;
		$scope.sigInst.startForceAtlas2();
	};
}