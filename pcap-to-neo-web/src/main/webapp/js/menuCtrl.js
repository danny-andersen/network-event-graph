/*jslint white:true */
'use strict';

/* Controllers */

angular.module('menuControllers', []).
  controller('menuCtrl', function ($scope) {
  	$scope.deviceMenuId = 0;
  	$scope.sessionMenuId = 1;
  	$scope.menuItems = [];
  	$scope.menuItems[$scope.deviceMenuId] = {"href" : "#/devices", 
  							"name": "Devices"};
  	$scope.menuItems[$scope.sessionMenuId] = {"href" : "#/allSessionGraph", 
  							"name" : "AllSessionGraph"};
  });