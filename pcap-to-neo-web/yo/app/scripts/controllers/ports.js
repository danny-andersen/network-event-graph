'use strict';

angular.module('networkEventGraphApp')
  .controller('portsCtrl', function ($scope, portService) {
    $scope.startPort = 0;
    $scope.endPort = 65535;
    $scope.loading = false;
    $scope.showTable = true;
    $scope.sessionParams = {};
    $scope.refreshSessions = false;
    $scope.getPortUsage = function (usageType) {
      if (usageType === undefined || usageType === "") {
        $scope.usageType = 'session';
      } else {
        $scope.usageType = usageType;
      }
      $scope.loading = true;
      $scope.ports = portService.portUsage.query({
        'usageType': $scope.usageType,
        'startPort': $scope.startPort,
        'endPort': $scope.endPort,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (ports) {
        $scope.loading = false;
      });
    };
  });