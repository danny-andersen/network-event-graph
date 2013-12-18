'use strict';

angular.module('networkEventGraphApp').controller('sessionTabCtrl', function ($scope, $window, $timeout, webSitesByIp, webSitesByHostname, ipSessionService, graphService, chartService) {
  $scope.showTable = true;
  $scope.showGraph = false;
  $scope.showChart = false;
  $scope.currentSessions = [];

  $scope.$watch('$parent.period.changed', function () {
    if ($scope.$parent.period !== undefined && $scope.$parent.period.changed) {
      $scope.refresh = true;
      if ($scope.$parent.detail === undefined) {
        $scope.$parent.detail = {
          device: {}
        };
      } else if ($scope.$parent.detail.device === undefined) {
        $scope.$parent.detail.device = {};
      }
      $scope.$parent.detail.device.allSessions = [];
      $scope.$parent.detail.device.toSessions = [];
      $scope.$parent.detail.device.fromSessions = [];
      $scope.$parent.period.changed = false;
    }
  });

  $scope.$watch('loading', function () {
    if ($scope.loading === false && $scope.currentSessions.length > 0) {
      if ($scope.showGraph) {
        $scope.showSessionGraph();
      } else if ($scope.showChart) {
        $scope.showSessionChart($scope.currentChartType);
      }
    }
  });

  //Tabs
  $scope.loading = false;
  $scope.navTabs = {};
  $scope.navTabs.protoTab = 0;
  $scope.navTabs.webTab = 1;
  $scope.navTabs.allTab = 2;
  $scope.navTabs.fromTab = 3;
  $scope.navTabs.toTab = 4;
  $scope.navTabs.tabs = [{
    active: true,
    url: ''
  }, {
    active: false,
    url: ''
  }, {
    active: false,
    url: 'views/sessionTable.html'
  }, {
    active: false,
    url: 'views/sessionTable.html'
  }, {
    active: false,
    url: 'views/sessionTable.html'
  }];

  $scope.showSessionSrc = function (session) {
    return session.srcHostname === null ? session.srcIpAddr : session.srcHostName;
  };

  $scope.showSessionDest = function (session) {
    return session.destHostname === null ? session.destIpAddr : session.destHostName;
  };

  $scope.getWebSitesVisitedByIp = function (ipaddr) {
    $scope.navTabs.tabs[$scope.navTabs.webTab].active = true;
    $scope.loading = true;
    $scope.webAddress = ipaddr;
    $scope.$parent.detail.device.websites = [];
    $scope.$parent.detail.device.websites = webSitesByIp.query({
      'ipAddr': ipaddr
    }, function () {
      $scope.loading = false;
    });
  };

  $scope.getWebSitesVisitedByHost = function () {
    $scope.navTabs.tabs[$scope.navTabs.webTab].active = true;
    $scope.loading = true;
    $scope.webAddress = $scope.detail.device.hostName;
    $scope.$parent.detail.device.websites = [];
    $scope.$parent.detail.device.websites = webSitesByHostname.query({
      'hostName': $scope.detail.device.hostName
    }, function () {
      $scope.loading = false;
    });
  };

  $scope.getsessionsByIp = function () {
    $scope.navTabs.tabs[$scope.navTabs.allTab].active = true;
    $scope.loading = true;
    $scope.ipAddress = $scope.sessionParams.ipAddr;
    $scope.$parent.detail.device.allSessions = ipSessionService.sessionsByIp.query($scope.sessionParams, function () {
      $scope.loading = false;
    });
    return $scope.$parent.detail.device.allSessions;
  };

  $scope.getsessionsBySrcIp = function () {
    $scope.navTabs.tabs[$scope.navTabs.fromTab].active = true;
    $scope.loading = true;
    $scope.ipAddress = $scope.sessionParams.ipAddr;
    $scope.$parent.detail.device.fromSessions = ipSessionService.sessionsBySrcIp.query($scope.sessionParams, function () {
      $scope.loading = false;
    });
    return $scope.$parent.detail.device.fromSessions;
  };

  $scope.getsessionsByDestIp = function () {
    $scope.navTabs.tabs[$scope.navTabs.toTab].active = true;
    $scope.loading = true;
    $scope.ipAddress = $scope.sessionParams.ipAddr;
    $scope.$parent.detail.device.toSessions = ipSessionService.sessionsByDestIp.query($scope.sessionParams, function () {
      $scope.loading = false;
    });
    return $scope.$parent.detail.device.toSessions;
  };

  $scope.setSessions = function (tabId, refresh) {
    refresh = $scope.refresh || refresh || false;
    var start = new Date($scope.sessionParams.start * 1000).toUTCString();
    var end = new Date($scope.sessionParams.end * 1000).toUTCString();
    if ($scope.sessionParams.start !== 0) {
      $scope.timePhrase = 'between ' + start + ' and ' + end;
    } else {
      $scope.timePhrase = 'for all dates';
    }

    $scope.activeTab = tabId;
    if (tabId === $scope.navTabs.webTab) {
      $scope.$parent.detail.device.websites = [];
      if ($scope.sessionParams.ipAddr === undefined) {
        $scope.getWebSitesVisitedByHost();
      } else {
        $scope.getWebSitesVisitedByIp($scope.sessionParams.ipAddr);
      }
    } else if (tabId === $scope.navTabs.allTab) {
      $scope.direction = 'from/to';
      if (!refresh && ($scope.$parent.detail.device.allSessions !== undefined && $scope.$parent.detail.device.allSessions.length > 0)) {
        $scope.currentSessions = $scope.$parent.detail.device.allSessions;
      } else {
        $scope.currentSessions = $scope.getsessionsByIp();
      }
    } else if (tabId === $scope.navTabs.fromTab) {
      $scope.direction = 'from';
      if ($scope.$parent.detail.device.fromSessions !== undefined && $scope.$parent.detail.device.fromSessions.length > 0) {
        $scope.currentSessions = $scope.$parent.detail.device.fromSessions;
      } else {
        $scope.currentSessions = $scope.getsessionsBySrcIp();
      }
    } else if (tabId === $scope.navTabs.toTab) {
      $scope.direction = 'to';
      if ($scope.$parent.detail.device.toSessions !== undefined && $scope.$parent.detail.device.toSessions.length > 0) {
        $scope.currentSessions = $scope.$parent.detail.device.toSessions;
      } else {
        $scope.currentSessions = $scope.getsessionsByDestIp();
      }
    }
    $scope.refresh = false;
  };

  $scope.filterSessions = function (direction) {
    graphService.filterSessions(direction);
  };

  $scope.showSessionTable = function () {
    $scope.showTable = true;
    $scope.showGraph = false;
    $scope.showChart = false;
  };

  $scope.showSessionChart = function (type) {
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

  //Graphing
  $scope.showSessionGraph = function () {
    $scope.showTable = false;
    $scope.showGraph = true;
    $scope.showChart = false;
    $scope.filter = 'all';
    graphService.showKey();
    $scope.fireGraph = true;
    $timeout(function () {
      //Do it in the timeout so that the DOM has updated and graph canvas is visible
      if ($scope.fireGraph && $scope.currentSessions !== undefined && $scope.currentSessions.length !== 0) {
        $scope.fireGraph = false;
        graphService.showGraph('.active ', $scope, $window, $scope.currentSessions, $scope.ipAddress);
      }
    });
  };

});