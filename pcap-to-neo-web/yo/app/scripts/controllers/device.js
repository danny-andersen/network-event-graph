/*jslint white:true */
'use strict';

angular.module('networkEventGraphApp')
  .controller('DeviceCtrl', function ($scope, $routeParams, $window, $location, $timeout, deviceModel,
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
	//Date control
  // var d = new Date();
  // d.setHours(0);
  // d.setMinutes(0);
  $scope.period = {
  	timeType: -1,
	showTimePicker: false,
    fromDate: new Date(0),
    fromTime: "00:00",
    toTime: "00:00",
    fromOpened: false,
    toOpened: false
  // $scope.timepicker = {
  // 	"time": "00:00"
  // };
  };
  var d = new Date();
  d.setHours(0);
  d.setMinutes(0);
  $scope.period.toDate = d;

  $scope.period.dateOptions = {
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

  var setPeriodParams = function() {
  			var parts = $scope.period.fromTime.split(":");
  			var ftime = parts[0] * 60 + parts[1];
			parts = $scope.period.toTime.split(":");
  			var ttime = parts[0] * 60 + parts[1];
  			if ($scope.period.fromDate !== undefined) {
  				$scope.sessionParams.start = ($scope.period.fromDate.getTime() / 1000).toFixed(0) + ftime;
  			}
  			if ($scope.period.toDate !== undefined) {
				$scope.sessionParams.end = ($scope.period.toDate.getTime() / 1000).toFixed(0) + ttime;
			}
  };

  var setTimePeriod = function() {
  	var d,t;
  	switch ($scope.period.timeType) {
   		case 0:
		    $scope.period.showTimePicker = true;
			d = new Date();
			d.setHours(0);
			d.setMinutes(0);
  			$scope.period.fromDate = d;
  			setPeriodParams();
  			break;
  		case -1:  
  			$scope.period.fromDate = new Date(0);
		    $scope.period.showTimePicker = false;
  			break;
  		default:
			$scope.period.showTimePicker = true;
			t = new Date().getTime();
			t -= $scope.period.timeType * 24 * 3600 * 1000;
			d = new Date(t); 
			d.setHours(0);
			d.setMinutes(0);
  			$scope.period.fromDate = d;
  			setPeriodParams();
  			break;
  	}
  };

  $scope.$watch('period.timeType', setTimePeriod);
  $scope.$watch('period.fromDate', setPeriodParams);
  $scope.$watch('period.fromTime', setPeriodParams);
  $scope.$watch('period.toDate', setPeriodParams);
  $scope.$watch('period.toTime', setPeriodParams);

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
