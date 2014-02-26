'use strict';

angular.module('networkEventGraphApp')
  .controller('sessionDetailCtrl', function ($scope, $routeParams, ipSessionService) {
    $scope.srcAddr = $routeParams.srcIpAddr;
    $scope.destAddr = $routeParams.destIpAddr;
    $scope.deviceId = $routeParams.deviceId;
    $scope.port = $routeParams.port;
    $scope.start = $routeParams.start;
    $scope.end = $routeParams.end;
    $scope.protocol = $routeParams.protocol;
    $scope.loading = false;

    $scope.setSessions = function () {
      $scope.loading = true;
      var start = new Date($scope.start * 1000).toUTCString();
      var end = new Date($scope.end * 1000).toUTCString();
      $scope.timePhrase = 'between ' + start + ' and ' + end;
      if ($scope.srcAddr !== undefined && $scope.srcAddr !== "") {
        $scope.tableTitle = 'Sessions from ipaddr ' + $scope.srcAddr + ' to ' + $scope.destAddr + ' ' + $scope.timePhrase;
        $scope.sessions = ipSessionService.sessionDetailByIp.query({
          'srcAddr': $scope.srcAddr,
          'destAddr': $scope.destAddr,
          'start': $scope.start,
          'end': $scope.end,
          'proto': $scope.protocol
        }, function () {
          $scope.loading = false;
        });
      } else if ($scope.deviceId !== undefined && $scope.deviceId !== "") {
        $scope.tableTitle = 'Sessions from device ' + $scope.deviceId + ' ' + $scope.timePhrase;
        if ($scope.protocol !== undefined && $scope.protocol !== "") {
          $scope.tableTitle = $scope.tableTitle + ' using protocol ' + $scope.protocol;
        }
        $scope.sessions = ipSessionService.sessionDetailByDeviceId.query({
          'deviceId': $scope.deviceId,
          'start': $scope.start,
          'end': $scope.end,
          'proto': $scope.protocol
        }, function () {
          $scope.loading = false;
        });
      } else if ($scope.port !== undefined && $scope.port !== "") {
        $scope.tableTitle = 'Sessions from port ' + $scope.port + ' ' + $scope.timePhrase;
        if ($scope.protocol !== undefined && $scope.protocol !== "") {
          $scope.tableTitle = $scope.tableTitle + ' using protocol ' + $scope.protocol;
        }
        $scope.sessions = ipSessionService.sessionDetailByPort.query({
          'port': $scope.port,
          'start': $scope.start,
          'end': $scope.end,
          'proto': $scope.protocol
        }, function () {
          $scope.loading = false;
        });
      } else {
        $scope.loading = false;
      }
    };

    $scope.setSessions();
  });