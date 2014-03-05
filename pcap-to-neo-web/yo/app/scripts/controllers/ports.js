'use strict';

angular.module('networkEventGraphApp')
  .controller('portsCtrl', function ($scope, portService) {
    $scope.startPort = 0;
    $scope.endPort = 65535;
    $scope.loading = false;
    $scope.showTable = false;
    $scope.sessionParams = {};
    $scope.refreshSessions = false;
    $scope.getPortUsage = function (usageType) {
      if (usageType === undefined || usageType === "") {
        $scope.usageType = 'session';
      } else {
        $scope.usageType = usageType;
      }
      $scope.usageColumn = $scope.usageType.slice(0, 1).toUpperCase() + $scope.usageType.slice(1, $scope.usageType.length);
      $scope.loading = true;
      $scope.showTable = true;
      $scope.ports = portService.portUsage.query({
        'usageType': $scope.usageType,
        'startPort': $scope.startPort,
        'endPort': $scope.endPort,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (ports) {
        var i;
        $scope.loading = false;
        for (i = 0; i < $scope.ports.length; i++) {
          if ($scope.usageType === 'session') {
            $scope.ports[i].count = $scope.ports[i].sessionCount;
          } else if ($scope.usageType === 'device') {
            $scope.ports[i].count = $scope.ports[i].deviceCount;
          }
        }
      });
    };
  });