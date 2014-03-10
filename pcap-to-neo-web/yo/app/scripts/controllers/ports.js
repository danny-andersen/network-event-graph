'use strict';

angular.module('networkEventGraphApp')
  .controller('portsCtrl', function ($scope, portService, protocolService, chartService) {
    $scope.startPort = 0;
    $scope.endPort = 65535;
    $scope.loading = false;
    $scope.protoLoading = false;
    $scope.showTable = false;
    $scope.displayChoices = false;
    $scope.displayType = 'table';
    $scope.sessionParams = {};
    $scope.refreshSessions = false;
    var getProtocolUsage = function (portNum, port) {
      $scope.protoLoading = true;
      protocolService.protocolUsageByPort.query({
        'port': port,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (protocols) {
        var i, protoStr;
        for (i = 0; i < protocols.length; i++) {
          if (protoStr === undefined) {
            protoStr = protocols[i].protocol.name + '(' + protocols[i].sessionCount + ')';
          } else {
            protoStr = protoStr + ',' + protocols[i].protocol.name + '(' + protocols[i].sessionCount + ')';
          }
        }
        $scope.ports[portNum].protocols = protoStr;
        $scope.protoLoading = false;
      });
    };
    $scope.getPortUsage = function () {
      // $scope.usageColumn = $scope.usageType.slice(0, 1).toUpperCase() + $scope.usageType.slice(1, $scope.usageType.length);
      $scope.loading = true;
      $scope.ports = [];
      if ($scope.displayType === 'table') {
        $scope.showSessionTable();
      }
      portService.portUsage.query({
        'usageType': 'session',
        'startPort': $scope.startPort,
        'endPort': $scope.endPort,
        'startTime': $scope.sessionParams.start,
        'endTime': $scope.sessionParams.end
      }, function (ports) {
        var i;
        for (i = 0; i < ports.length; i++) {
          $scope.ports[i] = {};
          $scope.ports[i].sessionCount = ports[i].sessionCount;
          $scope.ports[i].port = ports[i].port.port;
          getProtocolUsage(i, $scope.ports[i].port, 'port');
        }
        portService.portUsage.query({
          'usageType': 'device',
          'startPort': $scope.startPort,
          'endPort': $scope.endPort,
          'startTime': $scope.sessionParams.start,
          'endTime': $scope.sessionParams.end
        }, function (ports) {
          var i, j;
          $scope.loading = false;
          for (i = 0; i < ports.length; i++) {
            for (j = 0; j < $scope.ports.length; j++) {
              if ($scope.ports[j].port === ports[i].port.port) {
                $scope.ports[j].deviceCount = ports[i].deviceCount;
              }
            }
          }
        });
      });
    };

    $scope.showSessionTable = function () {
      $scope.showTable = true;
      $scope.showChart = false;
      $scope.displayChoices = true;
      $scope.displayType = 'table';
    };

    $scope.showSessionChart = function (type) {
      $scope.showTable = false;
      $scope.showChart = true;
      $scope.displayChoices = true;
      $scope.displayType = type;
      var i, j, points = [];
      if ($scope.ports !== undefined) {
        for (i = 0; i < $scope.ports.length; i++) {
          var port = $scope.ports[i];
          var ip, found = false;
          points.push({
            'label': port.port + '(' + port.sessionCount + ')',
            'title': 'Port: ' + port.port + ' Sessions:' + port.sessionCount + '\nDevices: ' + port.deviceCount + '\nProtocols: ' + port.protocols,
            'size': port.sessionCount
          });
        }
        var selector = ' #sessionChart';
        if (points.length > 0) {
          if (type === 'bubble') {
            chartService.drawBubble(selector, 700, 700, points);
          } else if (type === 'circle') {
            chartService.drawCircles(selector, 700, 700, points);
          }
        }
      }
    };

  });