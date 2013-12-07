'use strict';

angular.module('networkEventGraphApp').controller('allSessionCtrl', function ($scope, $window, $timeout, $q, graphService, ipSessionService, deviceModel, deviceService) {
  $scope.data = {};
  $scope.sessionParams = {};
  $scope.data.devices = deviceModel.getDevices();
  $scope.deviceCnt = 0;
  $scope.deviceLoading = false;
  $scope.graphLoading = false;
  $scope.timeType = -1;
  $scope.showTimePicker = false;
  graphService.showKey();

  var setDevices = function (devices) {
    deviceModel.devices = devices;
    $scope.deviceCnt = devices.length;
    $scope.deviceLoading = false;
  };

  $scope.getDevices = function () {
    $scope.deviceLoading = true;
    $scope.data.devices = deviceService.deviceByIpAddr.query({
      'ipAddr': $scope.ipaddr
    }, setDevices);
  };
  $scope.getLocalDevices = function () {
    $scope.data.devices = deviceService.localDevices.query({}, setDevices);
  };
  $scope.getRemoteDevices = function () {
    $scope.data.devices = deviceService.remoteDevices.query({}, setDevices);
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

  $scope.plotGraph = function () {
    var i, j, devices = [],
      ipaddrs = [],
      params = {};
    var nodes = {};
    var edges = {};
    $scope.graphLoading = true;
    $scope.data = {};
    $scope.data.devices = deviceModel.getDevices();
    devices = $scope.data.devices;
    graphService.initGraph();
    graphService.plotDevices(devices, nodes);

    if ($scope.sessionParams.start !== undefined) {
      params.startdate = $scope.sessionParams.start;
      params.enddate = $scope.sessionParams.end;
    }
    //For each device, retrieve sessions and then plot them
    var promises = [];
    var plots = [];
    var plot;
    var ipAddr;
    $scope.retrieved = 0;
    $scope.percentRetrieved = [{
      'value': 0,
      'type': 'success'
    }, {
      'value': 100,
      'type': 'danger'
    }];
    var updateProgress = function () {
      $scope.retrieved++;
      var success = 100 * $scope.retrieved / $scope.totalRequests;
      var out = 100 - success;
      $scope.percentRetrieved = [{
        'value': success,
        'type': 'success'
      }, {
        'value': out,
        'type': 'danger'
      }];
    };
    //Assume total requests equals the numbe of devices (one ipaddr per)
    $scope.totalRequests = devices.length;
    for (i = 0; i < devices.length; i++) {
      ipaddrs = devices[i].ipaddr;
      //Adjust total requests made if required
      $scope.totalRequests += ipaddrs.length - 1;
      for (j = 0; j < ipaddrs.length; j++) {
        plot = {};
        ipAddr = ipaddrs[j].ipAddr;
        params.ipAddr = ipAddr;
        plot.ipAddr = ipAddr;
        plot.promise = ipSessionService.sessionsByIpHttp(params);
        plot.promise.then(updateProgress);
        promises.push(plot.promise);
        plots.push(plot);
      }
    }
    $q.all(promises).then(function (sessions) {
      for (i = 0; i < plots.length; i++) {
        graphService.plotSessions($scope, $window, nodes, edges, plots[i].ipAddr, sessions[i].data);
      }
      $scope.graphLoading = false;
      graphService.startForceAtlas2();
    });
  };

  $scope.filterSessions = function (direction) {
    graphService.filterSessions(direction);
  };


});