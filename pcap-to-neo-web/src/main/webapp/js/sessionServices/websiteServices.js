var websiteServices = angular.module('websiteServices', ['ngResource']);

websiteServices.factory('WebSitesByIp', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/websitevisit/ipaddr/:ipAddr', {})
});

websiteServices.factory('WebSitesByHostname', function ($resource) {
  return $resource('/pcap-to-neo-web/rest/websitevisit/hostname/:hostName', {})
});
