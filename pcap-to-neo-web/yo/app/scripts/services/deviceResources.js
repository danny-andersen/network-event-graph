/*jslint white:true */
'use strict';

angular.module('networkEventGraphApp').service('deviceService', function ($resource) {

  this.localDevices = $resource('/pcap-to-neo-web/rest/device/local', {}, {
    query: {
      method: 'GET',
      params: {},
      isArray: true
    }
  });

  this.remoteDevices = $resource('/pcap-to-neo-web/rest/device/remote', {}, {
    query: {
      method: 'GET',
      params: {},
      isArray: true
    }
  });

  this.deviceByIpAddr = $resource('/pcap-to-neo-web/rest/device/ipaddr/:ipAddr', {});

  this.deviceDetailById = $resource('/pcap-to-neo-web/rest/device/detail/:deviceId', {});
});