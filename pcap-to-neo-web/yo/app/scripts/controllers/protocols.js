'use strict';

angular.module('networkEventGraphApp')
  .controller('protocolsCtrl', function ($scope, protocolService) {
    $scope.loading = false;
    $scope.showTable = false;
    $scope.sessionParams = {};
    $scope.refreshSessions = false;
    var getUsageByPort = function () {
      $scope.protocols = protocolService.protocolUsageByPort.query({
        'protocol': $scope.protocol,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (protocols) {
        $scope.loading = false;
      });
    };
    var getUsageBySession = function () {
      $scope.protocols = protocolService.protocolUsage.query({
        'protocol': $scope.protocol,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (protocols) {
        $scope.loading = false;
      });
    };
    var getUsageByDevice = function () {
      $scope.protocols = protocolService.protocolUsageByDevice.query({
        'protocol': $scope.protocol,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (protocols) {
        $scope.loading = false;
      });
    };

    $scope.getProtocolUsage = function (usageType) {
      if (usageType === undefined || usageType === "") {
        $scope.usageType = 'session';
      } else {
        $scope.usageType = usageType;
      }
      $scope.usageColumn = $scope.usageType.slice(0, 1).toUpperCase() + $scope.usageType.slice(1, $scope.usageType.length);
      $scope.loading = true;
      $scope.showTable = true;
      if ($scope.usageType === 'session') {
        getUsageBySession();
      } else if ($scope.usageType === 'device') {
        getUsageByDevice();
      } else if ($scope.usageType === 'port') {
        getUsageByPort();
      }
    };


  });