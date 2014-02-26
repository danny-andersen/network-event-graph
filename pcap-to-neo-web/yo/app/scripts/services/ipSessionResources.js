'use strict';
angular.module('networkEventGraphApp').service('ipSessionService', function ($resource, $http) {
  var byIpUrl = '/pcap-to-neo-web/rest/session/ip/summary/ipaddr/';
  var byIpParams = ':ipAddr?startdate=:start&enddate=:end';

  this.sessionsByIp = $resource(byIpUrl + byIpParams, {});
  //This returns the promise
  this.sessionsByIpHttp = function (params) {
    var url = byIpUrl + params.ipAddr;
    params.ipAddr = undefined;
    return $http.get(url, {
      params: params
    });
  };
  this.sessionsBySrcIp = $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/src/:ipAddr?startdate=:start&enddate=:end', {});
  this.sessionsByDestIp = $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/dest/:ipAddr?startdate=:start&enddate=:end', {});
  this.sessionDetailByIp = $resource('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/:srcAddr?destaddr=:destAddr&startdate=:start&enddate=:end&protocol=:proto', {});
  this.sessionDetailByDeviceId = $resource('/pcap-to-neo-web/rest/session/ip/detail/device/:deviceId?startdate=:start&enddate=:end&protocol=:proto', {});
  this.sessionDetailByPort = $resource('/pcap-to-neo-web/rest/session/ip/detail/port/:port?startdate=:start&enddate=:end&protocol=:proto', {});
});