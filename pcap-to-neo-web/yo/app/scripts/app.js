'use strict';

angular.module('networkEventGraphApp', [
  'ngResource', 'menuControllers', 'deviceResources'
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
      .otherwise({
        redirectTo: '/'
      });
  });
