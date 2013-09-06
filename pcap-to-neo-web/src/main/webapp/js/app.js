'use strict';


// Declare app level module which depends on filters, and services
angular.module('NetworkGraphApp', ['ui.bootstrap', 'myApp.filters', 'myApp.services', 'myApp.directives', 
	'myApp.controllers', 'deviceServices', 'websiteServices', 'ipSessionServices']).
config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.when('/devices', {
			templateUrl: 'partials/devices.html',
			controller: 'DeviceCtrl'
		});
		$routeProvider.when('/device/:deviceId', {
			templateUrl: 'partials/device.html',
			controller: 'DeviceDetailCtrl'
		});
		$routeProvider.when('/device', {
			templateUrl: 'partials/device.html',
			controller: 'DeviceDetailCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/devices'
		});
	}
]);