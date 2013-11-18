'use strict';

angular.module('networkEventGraphApp', [
  'ui.bootstrap', 'ngResource', 'menuControllers', 'deviceResources', 'websiteResources', 'ipSessionResources'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/devices', {
        templateUrl: 'views/devices.html',
        controller: 'DevicesCtrl'
      })
      .when('/device/:deviceId', {
        templateUrl: 'views/device.html',
        controller: 'DeviceCtrl'
      })
      .when('/sessionDetail/:srcIpAddr', {
        templateUrl: 'views/sessionDetail.html',
        controller: 'SessionDetailCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });