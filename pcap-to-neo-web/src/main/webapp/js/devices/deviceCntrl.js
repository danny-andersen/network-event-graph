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

function DeviceDetailCtrl($scope, $routeParams,  DeviceModel, DeviceDetailById, WebSitesByIp, WebSitesByHostname,
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
	};

	$scope.getSessionsBySrcIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.fromTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from";
		$scope.detail.device.fromSessions = SessionsBySrcIp.query({
			'ipAddr': ipaddr
		});
	};

	$scope.getSessionsByDestIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.toTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "to";
		$scope.detail.device.toSessions = SessionsByDestIp.query({
			'ipAddr': ipaddr
		});
	};
	$scope.setSessions = function(tabId) {
		if (tabId === $scope.navTabs.allTab) {
			$scope.currentSessions = $scope.detail.device.allSessions;
		} else if (tabId === $scope.navTabs.fromTab) {
			$scope.currentSessions = $scope.detail.device.fromSessions;
		} else if (tabId === $scope.navTabs.toTab) {
			$scope.currentSessions = $scope.detail.device.toSessions;
		}
	};

}