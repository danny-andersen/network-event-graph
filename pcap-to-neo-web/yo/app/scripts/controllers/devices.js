/*jslint white:true */
'use strict';

angular.module('networkEventGraphApp')
  .controller('DevicesCtrl', function ($scope, deviceModel, deviceByIpAddr, localDevices, remoteDevices) {
    $scope.data = {};
    $scope.data.loading = false;
    $scope.data.query = deviceModel.getQuery();
    $scope.data.devices = deviceModel.getDevices();
    var setDevices = function(devices) {
        $scope.data.loading = false;
        deviceModel.setQuery($scope.data.query);
        deviceModel.setDevices(devices);
    };
    $scope.getDevices = function() {
        $scope.data.loading = true;
        $scope.data.query = "Ipaddr: " + $scope.ipaddr;
        $scope.data.devices = deviceByIpAddr.query({
            'ipAddr': $scope.ipaddr
        }, setDevices);
    };
    $scope.getLocalDevices = function() {
        $scope.data.loading = true;
        $scope.data.query = "All Local devices";
        $scope.data.devices = localDevices.query({}, setDevices);
    };
    $scope.getRemoteDevices = function() {
        $scope.data.loading = true;
        $scope.data.query = "All Remote devices";
        $scope.data.devices = remoteDevices.query({}, setDevices);
    };
    $scope.addDevice = function(deviceId) {
        $scope.$parent.items.Devices.push({"href" : "#/device/"+deviceId, "name": "Device(id="+deviceId+")" });
    };
});
