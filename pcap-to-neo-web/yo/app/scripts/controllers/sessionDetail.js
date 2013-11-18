'use strict';

angular.module('networkEventGraphApp')
  .controller('sessionDetailCtrl', function ($scope, $routeParams, sessionDetailByIp) {
    $scope.srcAddr = $routeParams.srcIpAddr;
    $scope.destAddr = $routeParams.destIpAddr;
    $scope.start = $routeParams.start;
    $scope.end = $routeParams.end;
    $scope.loading = false;

    $scope.setSessions = function () {
      $scope.loading = true;
      var start = new Date($scope.start * 1000).toUTCString();
      var end = new Date($scope.end * 1000).toUTCString();
      $scope.timePhrase = "between " + start + " and " + end;
      $scope.sessions = sessionDetailByIp.query({
        'srcAddr': $scope.srcAddr,
        'destAddr': $scope.destAddr,
        'start': $scope.start,
        'end': $scope.end
      }, function () {
        $scope.loading = false;
      });
    };

    $scope.setSessions();
  });