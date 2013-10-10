/*jslint white:true */

function DeviceDetailCtrl($scope, $routeParams, $window, $location, $timeout, DeviceModel, DeviceByIpAddr, DeviceDetailById, WebSitesByIp, WebSitesByHostname,
	SessionsByIp, SessionsBySrcIp, SessionsByDestIp) {
	$scope.detail = {};
	if ($routeParams.deviceId !== undefined) {
	 	$scope.detail.id = $routeParams.deviceId;
		$scope.detail.device = DeviceModel.getDeviceDetail($scope.detail.id);
		if ($scope.detail.device === undefined) {
			$scope.detail.device = DeviceDetailById.get({
				'deviceId': $scope.detail.id
			}, function(device) {
				DeviceModel.setDeviceDetail($scope.detail.id, device);
				$scope.ipAddr = device.ipaddr[0].ipAddr;
			});
		}
	} else if ($routeParams.ipaddr !== undefined) {
		$scope.ipAddr = $routeParams.ipaddr;
		$scope.detail.device = DeviceModel.getDeviceDetailByIpAddr($scope.ipAddr);
		if ($scope.detail.device === undefined) {
			var devs = [];
			devs = DeviceByIpAddr.query({
				'ipAddr': $scope.ipAddr
			}, function(devs) {
				$scope.detail.device = devs[0];
				DeviceModel.setDeviceDetail(devs[0].deviceId, devs[0]);
			});
		}
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
		$scope.loading=true;
		$scope.webAddress = ipaddr;
		$scope.detail.device.websites = [];
		$scope.detail.device.websites = WebSitesByIp.query({
			'ipAddr': ipaddr
		}, function() {
			$scope.loading = false;
		});
	};

	$scope.getWebSitesVisitedByHost = function() {
		$scope.navTabs.tabs[$scope.navTabs.webTab].active = true;
		$scope.loading = true;
		$scope.webAddress = $scope.detail.device.hostName;
		$scope.detail.device.websites = [];
		$scope.detail.device.websites = WebSitesByHostname.query({
			'hostName': $scope.detail.device.hostName
		}, function() {
			$scope.loading = false;
		});
	};

	$scope.getSessionsByIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.allTab].active = true;
		$scope.loading = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from/to";
		$scope.detail.device.allSessions = SessionsByIp.query({
			'ipAddr': ipaddr,
			'start' : ($scope.fromDate.getTime() / 1000).toFixed(0),
			'end'	: ($scope.toDate.getTime() / 1000).toFixed(0)
		}, function() {
			$scope.loading = false;
		});
		return $scope.detail.device.allSessions;
	};

	$scope.getSessionsBySrcIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.fromTab].active = true;
		$scope.loading = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from";
		$scope.detail.device.fromSessions = SessionsBySrcIp.query({
			'ipAddr': ipaddr,
			'start' : ($scope.fromDate.getTime() / 1000).toFixed(0),
			'end'	: ($scope.toDate.getTime() / 1000).toFixed(0)
		}, function() {
			$scope.loading = false;
		});
		return $scope.detail.device.fromSessions;
	};

	$scope.getSessionsByDestIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.toTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "to";
		$scope.loading = true;
		$scope.detail.device.toSessions = SessionsByDestIp.query({
			'ipAddr': ipaddr,
			'start' : ($scope.fromDate.getTime() / 1000).toFixed(0),
			'end'	: ($scope.toDate.getTime() / 1000).toFixed(0)
		}, function() {
			$scope.loading = false;
		});
		return $scope.detail.device.toSessions;
	};

	$scope.refresh = function() {
		$scope.setSessions($scope.activeTab, true);
	};

	$scope.setSessions = function(tabId, refresh) {
		refresh = refresh || false;
		$scope.activeTab = tabId;
		if (tabId === $scope.navTabs.webTab) {
			$scope.detail.device.websites = [];
			if ($scope.ipAddr === undefined) {
				$scope.getWebSitesVisitedByHost();
			} else {
				$scope.getWebSitesVisitedByIp($scope.ipAddr);
			}
		} else if (tabId === $scope.navTabs.allTab) {
			if (!refresh && ($scope.detail.device.allSessions !== undefined && $scope.detail.device.allSessions.length > 0)) {
				$scope.currentSessions = $scope.detail.device.allSessions;
			} else {
				$scope.currentSessions = $scope.getSessionsByIp($scope.ipAddr);
			}
		} else if (tabId === $scope.navTabs.fromTab) {
			if ($scope.detail.device.fromSessions !== undefined && $scope.detail.device.fromSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.fromSessions;
			} else {
				$scope.currentSessions = $scope.getSessionsBySrcIp($scope.ipAddr);
			}
		} else if (tabId === $scope.navTabs.toTab) {
			if ($scope.detail.device.toSessions !== undefined && $scope.detail.device.toSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.toSessions;
			} else {
				$scope.currentSessions = $scope.getSessionsByDestIp($scope.ipAddr);
			}
		}
	};

	//Graphing
	$scope.showSessionGraph = function() {
		var direction='both';
		switch ($scope.activeTab) {
			case $scope.navTabs.allTab:
				direction='both';
				break;
			case $scope.navTabs.fromTab:
				direction='from';
				break;
			case $scope.navTabs.toTab:
				direction='to';
				break;
		}
		var currentPath = $window.location.pathname;
		var url = currentPath + '#/device/graph/' + $scope.ipAddr + '/' + direction;
		url = url + '?start=' + $scope.fromDate.getTime() + '&end=' + $scope.toDate.getTime();
		$window.location = url;
		// $window.open(url);
	};
}

