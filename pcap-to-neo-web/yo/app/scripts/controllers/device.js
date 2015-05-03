'use strict';

angular.module('networkEventGraphApp').controller('DeviceCtrl', function ($scope, $routeParams, deviceModel, deviceService) {
  if ($scope.detail === undefined) {
    $scope.detail = {};
  }
  $scope.sessionParams = {};
  $scope.loading = false;
  $scope.refreshSessions = false;

  var setScopeParams = function (device) {
    $scope.sessionParams.ipAddr = device.ipaddr[0].ipAddr;
    if (device.description !== undefined && device.description !== '') {
      device.hasDescription = true;
    } else {
      device.hasDescription = false;
    }
  };

  $scope.setDeviceDetailById = function (deviceId) {
    $scope.detail.id = deviceId;
    $scope.sessionParams.deviceId = deviceId;
    $scope.detail.device = deviceModel.getDeviceDetail($scope.detail.id);
    if ($scope.detail.device === undefined) {
      $scope.loading = true;
      $scope.detail.device = deviceService.deviceDetailById.get({
        'deviceId': $scope.detail.id
      }, function (device) {
        $scope.loading = false;
        deviceModel.setDeviceDetail($scope.detail.id, device);
        setScopeParams(device);
      });
    } else {
      setScopeParams($scope.detail.device);
    }
  };

  $scope.setDeviceDetailByIpAddr = function (ipaddr) {
    $scope.sessionParams.ipAddr = ipaddr;
    $scope.detail.device = deviceModel.getDeviceDetailByIpAddr($scope.sessionParams.ipAddr);
    if ($scope.detail.device === undefined) {
      var devs = [];
      $scope.loading = true;
      devs = deviceService.deviceByIpAddr.query({
        'ipAddr': $scope.sessionParams.ipAddr
      }, function (devs) {
        $scope.loading = false;
        $scope.detail.device = devs[0];
        $scope.detail.id = devs[0].deviceId;
        deviceModel.setDeviceDetail(devs[0].deviceId, devs[0]);
        setScopeParams(devs[0]);
      });
    } else {
      setScopeParams($scope.detail.device);
    }
  };

  if ($routeParams.deviceId !== undefined) {
    $scope.setDeviceDetailById($routeParams.deviceId);
  } else if ($routeParams.ipaddr !== undefined) {
    $scope.setDeviceDetailByIpAddr($routeParams.ipaddr);
  }

});