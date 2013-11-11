/*jslint white:true */
var websiteResources = angular.module('websiteResources', ['ngResource']);

websiteResources.factory('webSitesByIp', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/websitevisit/ipaddr/:ipAddr', {});
});

websiteResources.factory('webSitesByHostname', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/websitevisit/hostname/:hostName', {});
});
