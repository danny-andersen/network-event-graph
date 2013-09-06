/*jslint white:true */

function DeviceCtrl($scope, DeviceByIpAddr) {
	$scope.ipaddr = "192.168.*";
	$scope.getDevices = function() {
		if ($scope.devices !== undefined) {
			console.log("No of devices:" + $scope.devices.length);
		}
		$scope.devices = DeviceByIpAddr.query({
			'ipAddr': $scope.ipaddr
		});
	};
}

function DeviceDetailCtrl($scope, $routeParams, DeviceDetail, WebSitesByIp, WebSitesByHostname,
	SessionsByIp, SessionsBySrcIp, SessionsByDestIp) {
	// $scope.device = JSON.parse($routeParams.device);
	$scope.device = DeviceDetail.get({
		'deviceId': $routeParams.deviceId
	});
	$scope.tabs = [{
		active: true,
		url: ""
	}, {
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
		$scope.tabs[0].active = true;
		$scope.webAddress = ipaddr;
		$scope.websites = WebSitesByIp.query({
			'ipAddr': ipaddr
		});
	};

	$scope.getWebSitesVisitedByHost = function() {
		$scope.tabs[0].active = true;
		$scope.webAddress = $scope.device.hostName;
		$scope.websites = WebSitesByHostname.query({
			'hostName': $scope.device.hostName
		});
	};

	$scope.getSessionsByIp = function(ipaddr) {
		$scope.tabs[1].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from/to";
		$scope.sessions = SessionsByIp.query({
			'ipAddr': ipaddr
		});
	};

	$scope.getSessionsBySrcIp = function(ipaddr) {
		$scope.tabs[2].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from";
		$scope.sessions = SessionsBySrcIp.query({
			'ipAddr': ipaddr
		});
	};

	$scope.getSessionsByDestIp = function(ipaddr) {
		$scope.tabs[3].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "to";
		$scope.sessions = SessionsByDestIp.query({
			'ipAddr': ipaddr
		});
	};
}