/*jslint white:true */
'use strict';

angular.module('networkEventGraphApp')
  .controller('DeviceCtrl', function ($scope, $routeParams, $window, $location, deviceModel,
	deviceByIpAddr, deviceDetailById, webSitesByIp, webSitesByHostname,
	sessionsByIp, sessionsBySrcIp, sessionsByDestIp) {
	$scope.detail = {};
	$scope.sessionParams = {};
	if ($routeParams.deviceId !== undefined) {
	 	$scope.detail.id = $routeParams.deviceId;
		$scope.detail.device = deviceModel.getDeviceDetail($scope.detail.id);
		if ($scope.detail.device === undefined) {
			$scope.detail.device = deviceDetailById.get({
				'deviceId': $scope.detail.id
			}, function(device) {
				deviceModel.setDeviceDetail($scope.detail.id, device);
				$scope.sessionParams.ipAddr = device.ipaddr[0].ipAddr;
                if (device.description !== undefined || device.description !== '') {
                    device.hasDescription = true;
                } else {
                    device.hasDescription = false;
                }
			});
		}
	} else if ($routeParams.ipaddr !== undefined) {
		$scope.sessionParams.ipAddr = $routeParams.ipaddr;
		$scope.detail.device = deviceModel.getDeviceDetailByIpAddr($scope.sessionParams.ipAddr);
		if ($scope.detail.device === undefined) {
			var devs = [];
			devs = deviceByIpAddr.query({
				'ipAddr': $scope.sessionParams.ipAddr
			}, function(devs) {
				$scope.detail.device = devs[0];
				deviceModel.setDeviceDetail(devs[0].deviceId, devs[0]);
			});
		}
	}

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
		url: "views/sessionTable.html"
	}, {
		active: false,
		url: "views/sessionTable.html"
	}, {
		active: false,
		url: "views/sessionTable.html"
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
		$scope.detail.device.websites = webSitesByIp.query({
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
		$scope.detail.device.websites = webSitesByHostname.query({
			'hostName': $scope.detail.device.hostName
		}, function() {
			$scope.loading = false;
		});
	};

	$scope.getsessionsByIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.allTab].active = true;
		$scope.loading = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from/to";
		$scope.sessionParams.ipAddr = ipaddr;
		$scope.detail.device.allSessions = sessionsByIp.query($scope.sessionParams, function() {
			$scope.loading = false;
		});
		return $scope.detail.device.allSessions;
	};

	$scope.getsessionsBySrcIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.fromTab].active = true;
		$scope.loading = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "from";
		$scope.sessionParams.ipAddr = ipaddr;
		$scope.detail.device.fromSessions = sessionsBySrcIp.query($scope.sessionParams, function() {
			$scope.loading = false;
		});
		return $scope.detail.device.fromSessions;
	};

	$scope.getsessionsByDestIp = function(ipaddr) {
		$scope.navTabs.tabs[$scope.navTabs.toTab].active = true;
		$scope.ipAddress = ipaddr;
		$scope.direction = "to";
		$scope.loading = true;
		$scope.sessionParams.ipAddr = ipaddr;
		$scope.detail.device.toSessions = sessionsByDestIp.query($scope.sessionParams, function() {
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
			if ($scope.sessionParams.ipAddr === undefined) {
				$scope.getWebSitesVisitedByHost();
			} else {
				$scope.getWebSitesVisitedByIp($scope.sessionParams.ipAddr);
			}
		} else if (tabId === $scope.navTabs.allTab) {
			if (!refresh && ($scope.detail.device.allSessions !== undefined && $scope.detail.device.allSessions.length > 0)) {
				$scope.currentSessions = $scope.detail.device.allSessions;
			} else {
				$scope.currentSessions = $scope.getsessionsByIp($scope.sessionParams.ipAddr);
			}
		} else if (tabId === $scope.navTabs.fromTab) {
			if ($scope.detail.device.fromSessions !== undefined && $scope.detail.device.fromSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.fromSessions;
			} else {
				$scope.currentSessions = $scope.getsessionsBySrcIp($scope.sessionParams.ipAddr);
			}
		} else if (tabId === $scope.navTabs.toTab) {
			if ($scope.detail.device.toSessions !== undefined && $scope.detail.device.toSessions.length > 0) {
				$scope.currentSessions = $scope.detail.device.toSessions;
			} else {
				$scope.currentSessions = $scope.getsessionsByDestIp($scope.sessionParams.ipAddr);
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
		var url = currentPath + '#/device/graph/' + $scope.sessionParams.ipAddr + '/' + direction;
		if ($scope.sessionParams.start !== undefined && $scope.sessionParams.end !== undefined) {
			url = url + '?start=' + $scope.sessionParams.start + '&end=' + $scope.sessionParams.end;
		}
		$window.location = url;
		// $window.open(url);
	};
});
