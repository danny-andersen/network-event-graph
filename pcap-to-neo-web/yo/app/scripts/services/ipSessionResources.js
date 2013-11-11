/*jslint white:true */
var sessServices = angular.module('ipSessionResources', ['ngResource']);

sessServices.factory('sessionsByIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/:ipAddr?startdate=:start&enddate=:end', {});
});

sessServices.factory('sessionsBySrcIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/src/:ipAddr?startdate=:start&enddate=:end', {});
});

sessServices.factory('sessionsByDestIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/dest/:ipAddr?startdate=:start&enddate=:end', {});
});

sessServices.factory('sessionDetailByIp', function ($resource) {
	return $resource('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/:srcAddr?destaddr=:destAddr&startdate=:start&enddate=:end', {});
});