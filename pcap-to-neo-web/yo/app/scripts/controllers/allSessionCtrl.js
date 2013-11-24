angular.module('networkEventGraphApp').controller('allSessionCtrl', function ($scope, $window, $timeout, graphService, sessionsByIp, deviceModel, deviceByIpAddr, localDevices, remoteDevices) {
  var nodes = {};
  var edges = {};
  $scope.data = {};
  $scope.data.devices = deviceModel.getDevices();
  $scope.deviceCnt = 0;
  $scope.deviceLoading = false;
  $scope.graphLoading = false;
  $scope.timeType = -1;
  $scope.showTimePicker = false;
  $scope.fromDate = 0;
  $scope.fromTime = "00:00";
  // $scope.timepicker = {
  // "time": "00:00"
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

  $scope.fromOpen = function () {
    $timeout(function () {
      $scope.fromOpened = true;
    });
  };

  $scope.toOpen = function () {
    $timeout(function () {
      $scope.toOpened = true;
    });
  };

  $scope.setTimePeriod = function () {
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

  var setDevices = function (devices) {
    deviceModel.devices = devices;
    $scope.deviceCnt = devices.length;
    $scope.deviceLoading = false;
  };
  $scope.getDevices = function () {
    $scope.deviceLoading = true;
    $scope.data.devices = deviceByIpAddr.query({
      'ipAddr': $scope.ipaddr
    }, setDevices);
  };
  $scope.getLocalDevices = function () {
    $scope.data.devices = localDevices.query({}, setDevices);
  };
  $scope.getRemoteDevices = function () {
    $scope.data.devices = remoteDevices.query({}, setDevices);
  };
  $scope.stopLayout = function () {
    if (!$scope.graphLoading) {
      graphService.stopForceAtlas2();
    }
  };
  $scope.startLayout = function () {
    if (!$scope.graphLoading) {
      graphService.startForceAtlas2();
    }
  };
  graphService.showKey();
  $scope.plotGraph = function () {
    var i, j, ipAddr, devices = [],
      sessions = [],
      ipaddrs = [];
    $scope.graphLoading = true;
    $scope.data = {};
    $scope.data.devices = deviceModel.getDevices();
    devices = $scope.data.devices;
    graphService.initGraph();
    graphService.plotDevices(devices, nodes);

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
        sessions = sessionsByIp.query(params, function (sessions) {
          graphService.plotSessions($scope, $window, nodes, edges, ipAddr, sessions);
        });
      }
    }
    $scope.graphLoading = false;
    graphService.startForceAtlas2();
  };

  $scope.filterSessions = function (direction) {
    graphService.filterSessions(direction);
  };


});