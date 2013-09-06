var sessServices = angular.module('ipSessionServices', ['ngResource']);

sessServices.factory('SessionsByIp', function($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/:ipAddr', {})
});

sessServices.factory('SessionsBySrcIp', function($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/src/:ipAddr', {})
});

sessServices.factory('SessionsByDestIp', function($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/dest/:ipAddr', {})
});