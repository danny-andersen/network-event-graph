var sessServices = angular.module('ipSessionServices', ['ngResource']);

sessServices.factory('SessionsByIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/:ipAddr?startdate=:start&enddate=:end', {});
});

sessServices.factory('SessionsBySrcIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/src/:ipAddr?startdate=:start&enddate=:end', {});
});

sessServices.factory('SessionsByDestIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/dest/:ipAddr?startdate=:start&enddate=:end', {});
});

sessServices.factory('SessionDetailByIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/:srcAddr?destaddr=:destAddr&startdate=:start&enddate=:end', {});
});