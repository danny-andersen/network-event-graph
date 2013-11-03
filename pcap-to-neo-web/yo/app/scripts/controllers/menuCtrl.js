/*jslint white:true */
'use strict';

/* Controllers */

angular.module('menuControllers', []).
  controller('menuCtrl', function ($scope) {
  	$scope.items = {};
  	$scope.sessionMenuId = 0;
  	$scope.deviceMenuId = 1;
  	$scope.items.menuItems = [];
  	$scope.items.menuItems[$scope.sessionMenuId] = {"href" : "#/allSessionGraph", 
  							"name" : "Sessions"};
  	$scope.items[$scope.items.menuItems[$scope.sessionMenuId].name] = [];
  	$scope.items.menuItems[$scope.deviceMenuId] = {"href" : "#/devices", 
  							"name": "Devices"};
  	$scope.items[$scope.items.menuItems[$scope.deviceMenuId].name] = [];
  });