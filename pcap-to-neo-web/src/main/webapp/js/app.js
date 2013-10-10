/*jslint white:true */

'use strict';


// Declare app level module which depends on filters, and services
angular.module('NetworkGraphApp', ['ui.bootstrap', 'myApp.filters', 'myApp.services', 'myApp.directives', 
	'menuControllers', 'deviceServices', 'deviceModels', 'websiteServices',
	 'ipSessionServices']).
config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.when('/devices', {
			templateUrl: 'partials/devices.html',
			controller: 'DeviceCtrl'
		});
		$routeProvider.when('/allSessionGraph', {
			templateUrl: 'partials/allSessionGraph.html',
			controller: 'AllSessionCtrl'
		});
		$routeProvider.when('/device/:deviceId', {
			templateUrl: 'partials/device.html',
			controller: 'DeviceDetailCtrl'
		});
		$routeProvider.when('/device', {
			templateUrl: 'partials/device.html',
			controller: 'DeviceDetailCtrl'
		});
		$routeProvider.when('/ipaddr/:ipaddr', {
			templateUrl: 'partials/device.html',
			controller: 'DeviceDetailCtrl'
		});
		$routeProvider.when('/sessionDetail/:srcIpAddr', {
			templateUrl: 'partials/sessionDetail.html',
			controller: 'SessionCtrl'
		});
		// $routeProvider.when('/device/graph/:ipAddr/:direction?start=:start&end=:end', {
		$routeProvider.when('/device/graph/:ipAddr/:direction', {
			templateUrl: 'partials/sessionGraph.html',
			controller: 'SessionGraphCtrl'
		});
		$routeProvider.when('/device/graph/:ipAddr/:direction?start=:start&end=:end', {
			templateUrl: 'partials/sessionGraph.html',
			controller: 'SessionGraphCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/devices'
		});
	}
]);