/*jslint white:true */
'use strict';

var devServices = angular.module('deviceResources', ['ngResource']);

devServices.factory('localDevices', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/local', {}, {
    query: {method: 'GET', params: {}, isArray: true}
  });
});

devServices.factory('remoteDevices', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/remote', {}, {
    query: {method: 'GET', params: {}, isArray: true}
  });
});

devServices.factory('deviceByIpAddr', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/ipaddr/:ipAddr', {});
  // return $resource('/pcap-to-neo-web/rest/device/ipaddr/:ipAddr', {
  //   query: {method: 'GET', params: {}, isArray: true}
  // });
});

devServices.factory('deviceDetailById', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/detail/:deviceId', {});
});

