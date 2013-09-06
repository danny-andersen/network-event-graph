/*jslint white:true */

var devServices = angular.module('deviceServices', ['ngResource']);

devServices.factory('LocalDevice', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/local', {}, {
    query: {method: 'GET', params: {}, isArray: true}
  });
});

devServices.factory('RemoteDevice', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/remote', {}, {
    query: {method: 'GET', params: {}, isArray: true}
  });
});

devServices.factory('DeviceByIpAddr', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/ipaddr/:ipAddr', {});
});

devServices.factory('DeviceDetail', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/device/detail/:deviceId', {});
});
