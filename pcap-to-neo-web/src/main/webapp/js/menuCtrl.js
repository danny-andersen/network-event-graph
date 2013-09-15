/*jslint white:true */
'use strict';

/* Controllers */

angular.module('menuControllers', []).
  controller('menuCtrl', function ($scope) {
  	$scope.deviceMenuId = 0;
  	$scope.menuItems = [];
  	$scope.menuItems[$scope.deviceMenuId] = {"href" : "#/devices", "name": "Devices"};
  });