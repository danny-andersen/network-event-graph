/*jslint white:true */
'use strict';

angular.module('networkEventGraphApp').controller('DevicesCtrl', function ($scope, $routeParams, deviceModel, deviceService) {
  $scope.data = {};
  $scope.data.loading = false;
  $scope.data.query = deviceModel.getQuery();
  $scope.data.devices = deviceModel.getDevices();
  $scope.port = $routeParams.port;
  var setDevices = function (devices) {
    deviceModel.setQuery($scope.data.query);
    deviceModel.intersectDevices(devices);
    $scope.filterCount--;
    if ($scope.filterCount <= 0) {
      $scope.data.loading = false;
      $scope.data.devices = deviceModel.getDevices();
    }
  };
  $scope.getDevices = function () {
    $scope.data.loading = true;
    $scope.data.devices = [];
    $scope.data.query = '';
    $scope.filterCount = 0;
    deviceModel.setDevices([]);
    if ($scope.ipaddr !== undefined && $scope.ipaddr !== '') {
      $scope.filterCount++;
      $scope.data.query = 'Ipaddr: ' + $scope.ipaddr + ' ';
      deviceService.deviceByIpAddr.query({
        'ipAddr': $scope.ipaddr
      }, function (devices) {
        setDevices(devices);
      });
    }
    if ($scope.port !== undefined && $scope.port !== '') {
      $scope.filterCount++;
      $scope.data.query = $scope.data.query + 'Port: ' + $scope.port + ' ';
      $scope.data.devices = deviceService.deviceByPort.query({
        'port': $scope.port
      }, function (devices) {
        setDevices(devices);
      });
    }
    if ($scope.protocol !== undefined && $scope.protocol !== '') {
      $scope.filterCount++;
      $scope.data.query = $scope.data.query + 'Protocol: ' + $scope.protocol;
      $scope.data.devices = deviceService.deviceByProtocol.query({
        'protocol': $scope.protocol
      }, function (devices) {
        setDevices(devices);
      });
    }
  };
  $scope.getLocalDevices = function () {
    $scope.data.loading = true;
    $scope.filterCount = 1;
    $scope.data.query = 'All Local devices';
    $scope.data.devices = deviceService.localDevices.query({}, setDevices);
  };
  $scope.getRemoteDevices = function () {
    $scope.data.loading = true;
    $scope.filterCount = 1;
    $scope.data.query = 'All Remote devices';
    $scope.data.devices = deviceService.remoteDevices.query({}, setDevices);
  };
  $scope.addDevice = function (deviceId) {
    $scope.$parent.items.Devices.push({
      'href': '#/device/' + deviceId,
      'name': 'Device(id=' + deviceId + ')'
    });
  };
  if ($scope.port !== undefined && $scope.port !== '') {
    $scope.getDevices();
  }
});