'use strict';

angular.module('networkEventGraphApp').service('deviceModel', function deviceModel() {
  // AngularJS will instantiate a singleton by calling "new" on this function
  this.deviceDetails = {};
  this.query = '';
  this.getQuery = function () {
    return this.query;
  };
  this.setQuery = function (query) {
    this.query = query;
  };
  this.getDevices = function () {
    return this.devices;
  };
  this.setDevices = function (devices) {
    this.devices = devices;
  };
  this.setDeviceDetail = function (deviceId, device) {
    this.deviceDetails[deviceId] = device;
  };
  this.getDeviceDetail = function (deviceId) {
    return this.deviceDetails[deviceId];
  };
  this.getDeviceDetailByIpAddr = function (ipaddr) {
    var i;
    for (i = 0; i < this.deviceDetails.length; i++) {
      if (ipaddr === this.deviceDetails[i].ipaddr.ipAddr) {
        return this.deviceDetails[i];
      }
    }
  };
});