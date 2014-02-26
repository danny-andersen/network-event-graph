'use strict';

angular.module('networkEventGraphApp', ['ui.bootstrap', 'ngResource', 'menuControllers', 'websiteResources'])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/allSessionGraph', {
        templateUrl: 'views/allSessionGraph.html',
        controller: 'allSessionCtrl'
      })
      .when('/devices', {
        templateUrl: 'views/devices.html',
        controller: 'DevicesCtrl'
      })
      .when('/device/:deviceId', {
        templateUrl: 'views/device.html',
        controller: 'DeviceCtrl'
      })
      .when('/ipaddr/:ipaddr', {
        templateUrl: 'views/device.html',
        controller: 'DeviceCtrl'
      })
      .when('/sessionDetail', {
        templateUrl: 'views/sessionDetail.html',
        controller: 'sessionDetailCtrl'
      })
      .when('/device/graph/:ipAddr/:direction', {
        templateUrl: 'views/deviceGraph.html',
        controller: 'deviceGraphCtrl'
      })
      .when('/device/graph/:ipAddr/:direction?start=:start&end=:end', {
        templateUrl: 'views/deviceGraph.html',
        controller: 'deviceGraphCtrl'
      })
      .when('/ports', {
        templateUrl: 'views/ports.html',
        controller: 'portsCtrl'
      })
      .otherwise({
        redirectTo: '/devices'
      });
  });