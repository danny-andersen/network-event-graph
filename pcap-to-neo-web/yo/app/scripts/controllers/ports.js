'use strict';

angular.module('networkEventGraphApp')
  .controller('portsCtrl', function ($scope, portService, protocolService, chartService) {
    $scope.startPort = 0;
    $scope.endPort = 65535;
    $scope.loading = false;
    $scope.protoLoading = false;
    $scope.showTable = false;
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
      $scope.showTable = true;
      $scope.ports = [];
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

    $scope.showSessionChart = function (type) {
      //TODO Change this for ports
      $scope.showTable = false;
      $scope.showGraph = false;
      $scope.showChart = true;
      $scope.currentChartType = type;
      var i, j, points = [];
      for (i = 0; i < $scope.currentSessions.length; i++) {
        var session = $scope.currentSessions[i];
        var ip, found = false;
        if (session.srcIpAddr !== $scope.ipAddress) {
          ip = session.srcIpAddr;
        } else {
          ip = session.destIpAddr;
        }
        for (j = 0; j < points.length; j++) {
          if (points[j].label.search(ip) !== -1) {
            points[j].size += session.numSessions;
            points[j].label = ip + '\n' + points[j].size + ' sessions';
            found = true;
          }
        }
        if (!found) {
          points.push({
            'label': ip + '\n' + session.numSessions + ' sessions',
            'size': session.numSessions
          });
        }
      }
      var selector = ' #sessionChart';
      if ($scope.direction === 'from/to') {
        selector = '.all' + selector;
      } else {
        selector = '.' + $scope.direction + selector;
      }
      if ($scope.currentSessions.length > 0) {
        if (type === 'bubble') {
          chartService.drawBubble(selector, 500, 500, points);
        } else if (type === 'circle') {
          chartService.drawCircles(selector, 500, 500, points);
        }
      }
    };

  });