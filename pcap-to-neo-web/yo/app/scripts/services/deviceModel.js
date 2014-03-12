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
  this.intersectDevices = function (devices) {
    //Set devices to the intersection of the two sets of devices
    var i, j, intersect;
    intersect = [];
    if (this.devices === undefined || this.devices.length === 0) {
      this.devices = devices;
    } else {
      for (i = 0; i < this.devices.length; i++) {
        //If device is present in incoming set add it to the result
        for (j = 0; j < devices.length; j++) {
          if (this.devices[i].deviceId === devices[j].deviceId) {
            intersect.push(this.devices[i]);
          }
        }
      }
      this.devices = intersect;
    }
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