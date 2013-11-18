'use strict';

angular.module('networkEventGraphApp').controller('sessionTabCtrl', function ($scope, $window, webSitesByIp, webSitesByHostname, sessionsByIp, sessionsBySrcIp, sessionsByDestIp) {
  $scope.$watch('period.timeType', function () {
    $scope.refresh = true;
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
    $scope.detail.device.websites = [];
    $scope.detail.device.websites = webSitesByIp.query({
      'ipAddr': ipaddr
    }, function () {
      $scope.loading = false;
    });
  };

  $scope.getWebSitesVisitedByHost = function () {
    $scope.navTabs.tabs[$scope.navTabs.webTab].active = true;
    $scope.loading = true;
    $scope.webAddress = $scope.detail.device.hostName;
    $scope.detail.device.websites = [];
    $scope.detail.device.websites = webSitesByHostname.query({
      'hostName': $scope.detail.device.hostName
    }, function () {
      $scope.loading = false;
    });
  };

  $scope.getsessionsByIp = function () {
    $scope.navTabs.tabs[$scope.navTabs.allTab].active = true;
    $scope.loading = true;
    $scope.ipAddress = $scope.sessionParams.ipAddr;
    $scope.detail.device.allSessions = sessionsByIp.query($scope.sessionParams, function () {
      $scope.loading = false;
    });
    return $scope.detail.device.allSessions;
  };

  $scope.getsessionsBySrcIp = function () {
    $scope.navTabs.tabs[$scope.navTabs.fromTab].active = true;
    $scope.loading = true;
    $scope.ipAddress = $scope.sessionParams.ipAddr;
    $scope.detail.device.fromSessions = sessionsBySrcIp.query($scope.sessionParams, function () {
      $scope.loading = false;
    });
    return $scope.detail.device.fromSessions;
  };

  $scope.getsessionsByDestIp = function () {
    $scope.navTabs.tabs[$scope.navTabs.toTab].active = true;
    $scope.loading = true;
    $scope.ipAddress = $scope.sessionParams.ipAddr;
    $scope.detail.device.toSessions = sessionsByDestIp.query($scope.sessionParams, function () {
      $scope.loading = false;
    });
    return $scope.detail.device.toSessions;
  };

  $scope.setSessions = function (tabId, refresh) {
    refresh = $scope.refresh || refresh || false;
    $scope.activeTab = tabId;
    if (tabId === $scope.navTabs.webTab) {
      $scope.detail.device.websites = [];
      if ($scope.sessionParams.ipAddr === undefined) {
        $scope.getWebSitesVisitedByHost();
      } else {
        $scope.getWebSitesVisitedByIp($scope.sessionParams.ipAddr);
      }
    } else if (tabId === $scope.navTabs.allTab) {
      $scope.direction = 'from/to';
      if (!refresh && ($scope.detail.device.allSessions !== undefined && $scope.detail.device.allSessions.length > 0)) {
        $scope.currentSessions = $scope.detail.device.allSessions;
      } else {
        $scope.currentSessions = $scope.getsessionsByIp();
      }
    } else if (tabId === $scope.navTabs.fromTab) {
      $scope.direction = 'from';
      if ($scope.detail.device.fromSessions !== undefined && $scope.detail.device.fromSessions.length > 0) {
        $scope.currentSessions = $scope.detail.device.fromSessions;
      } else {
        $scope.currentSessions = $scope.getsessionsBySrcIp();
      }
    } else if (tabId === $scope.navTabs.toTab) {
      $scope.direction = 'to';
      if ($scope.detail.device.toSessions !== undefined && $scope.detail.device.toSessions.length > 0) {
        $scope.currentSessions = $scope.detail.device.toSessions;
      } else {
        $scope.currentSessions = $scope.getsessionsByDestIp();
      }
    }
  };

  //Graphing
  $scope.showSessionGraph = function () {
    var direction = 'both';
    switch ($scope.activeTab) {
    case $scope.navTabs.allTab:
      direction = 'both';
      break;
    case $scope.navTabs.fromTab:
      direction = 'from';
      break;
    case $scope.navTabs.toTab:
      direction = 'to';
      break;
    }
    var currentPath = $window.location.pathname;
    var url = currentPath + '#/device/graph/' + $scope.sessionParams.ipAddr + '/' + direction;
    if ($scope.sessionParams.start !== undefined && $scope.sessionParams.end !== undefined) {
      url = url + '?start=' + $scope.sessionParams.start + '&end=' + $scope.sessionParams.end;
    }
    $window.location = url;
    // $window.open(url);
  };
});