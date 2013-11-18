/*jslint white:true */
'use strict';

describe('Controller: sessionTabCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));
  //Set up Matcher
  beforeEach(function () {
    this.addMatchers({
      toEqualData: function (expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });

  var sessionTabCtrl,
    mockBackend,
    window,
    $scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $window,
    webSitesByIp, webSitesByHostname, sessionsByIp, sessionsBySrcIp, sessionsByDestIp) {
    $scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    window = $window;
    sessionTabCtrl = $controller('sessionTabCtrl', {
      $scope: $scope,
      $window: $window,
      webSitesByIp: webSitesByIp,
      webSitesByHostname: webSitesByHostname,
      sessionsByIp: sessionsByIp,
      sessionsBySrcIp: sessionsBySrcIp,
      sessionsByDestIp: sessionsByDestIp
    });
  }));

  it('retrieve websites By hostname when tab selected', function () {
    $scope.sessionParams = {};
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.detail.device.hostName = "danny";
    var websites = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/websitevisit/hostname/' + $scope.detail.device.hostName).respond(websites);
    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.webTab);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.websites).toEqual([]);
    // expect($scope.data.query).toBe('Ipaddr: 192.168.*');

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.websites).toEqualData(websites);
  });

  it('retrieve websites By Ip Address when tab selected', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {};
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var websites = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/websitevisit/ipaddr/' + $scope.sessionParams.ipAddr).respond(websites);
    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.webTab);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.websites).toEqual([]);
    // expect($scope.data.query).toBe('Ipaddr: 192.168.*');

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.websites).toEqualData(websites);
  });

  it('retrieve all sessions By Ip Address all dates', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 0,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];

    var params = $scope.sessionParams.ipAddr + '?startdate=' + $scope.sessionParams.start + '&enddate=' + $scope.sessionParams.end;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions);
    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.allTab);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.allSessions).toEqual([]);
    expect($scope.currentSessions).toEqual([]);
    expect($scope.direction).toEqual("from/to");

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.allSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve all sessions By Ip Address already retrieved', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 23000,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }, {
      id: 3
    }];
    $scope.detail.device.allSessions = sessions;

    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.allTab);
    expect($scope.direction).toEqual('from/to');
    expect($scope.currentSessions).toEqualData(sessions);
    expect($scope.loading).toEqual(false);
    expect($scope.detail.device.allSessions).toEqualData(sessions);
  });

  it('retrieve all sessions By Ip Address with refresh', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 0,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];
    $scope.detail.device.allSessions = [{
      id: 1
    }];

    var params = $scope.sessionParams.ipAddr + '?startdate=' + $scope.sessionParams.start + '&enddate=' + $scope.sessionParams.end;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions);
    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.allTab, true);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.allSessions).toEqual([]);
    expect($scope.currentSessions).toEqual([]);
    expect($scope.direction).toEqual("from/to");

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.allSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve all sessions By Ip Address with forced refresh', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 0,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];
    $scope.detail.device.allSessions = [{
      id: 1
    }];

    var params = $scope.sessionParams.ipAddr + '?startdate=' + $scope.sessionParams.start + '&enddate=' + $scope.sessionParams.end;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions);
    expect($scope.loading).toEqual(false);

    //Set timetype, and apply to force refresh
    $scope.period = {};
    $scope.period.timeType = 0;

    $scope.$apply();

    //Call method undertest
    $scope.setSessions($scope.navTabs.allTab);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.allSessions).toEqual([]);
    expect($scope.currentSessions).toEqual([]);
    expect($scope.direction).toEqual("from/to");

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.allSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve from sessions By Ip Address', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 23000,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }, {
      id: 3
    }];

    var params = $scope.sessionParams.ipAddr + '?startdate=' + $scope.sessionParams.start + '&enddate=' + $scope.sessionParams.end;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/src/' + params).respond(sessions);
    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.fromTab);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.fromSessions).toEqual([]);
    expect($scope.currentSessions).toEqual([]);
    expect($scope.direction).toEqual("from");

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.fromSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve from sessions By Ip Address already retrieved', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 23000,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }, {
      id: 3
    }];
    $scope.detail.device.fromSessions = sessions;

    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.fromTab);
    expect($scope.direction).toEqual('from');
    expect($scope.currentSessions).toEqualData(sessions);
    expect($scope.loading).toEqual(false);
    expect($scope.detail.device.fromSessions).toEqualData(sessions);
  });

  it('retrieve to sessions By Ip Address', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      start: 23000,
      end: 1000000
    };
    $scope.sessionParams.ipAddr = '192.168.1.255';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }, {
      id: 3
    }];

    var params = $scope.sessionParams.ipAddr + '?startdate=' + $scope.sessionParams.start + '&enddate=' + $scope.sessionParams.end;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/dest/' + params).respond(sessions);
    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.toTab);
    expect($scope.loading).toEqual(true);
    expect($scope.detail.device.toSessions).toEqual([]);
    expect($scope.currentSessions).toEqual([]);
    expect($scope.direction).toEqual('to');

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.toSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve to sessions By Ip Address already retrieved', function () {
    $scope.detail = {};
    $scope.detail.device = {};
    $scope.sessionParams = {
      ipAddr: '192.168.1.255',
      start: 23000,
      end: 1000000
    };
    var sessions = [{
      id: 1
    }, {
      id: 2
    }, {
      id: 3
    }];
    $scope.detail.device.toSessions = sessions;

    expect($scope.loading).toEqual(false);

    //Call method undertest
    $scope.setSessions($scope.navTabs.toTab);
    expect($scope.direction).toEqual('to');
    expect($scope.currentSessions).toEqualData(sessions);
    expect($scope.loading).toEqual(false);
    expect($scope.detail.device.toSessions).toEqualData(sessions);
  });

  it('set window to graph location for both', function () {
    $scope.sessionParams = {
      ipAddr: '192.168.1.255',
      start: 23000,
      end: 1000000
    };
    $scope.activeTab = $scope.navTabs.allTab;
    //Call method under test
    $scope.showSessionGraph();
    var url = '#/device/graph/' + $scope.sessionParams.ipAddr + '/' + 'both' + '?start=' + $scope.sessionParams.start + '&end=' + $scope.sessionParams.end;
    expect(window.location.hash).toEqual(url);

  });

  it('set window to graph location for from', function () {
    $scope.sessionParams = {
      ipAddr: '192.168.1.255',
      start: 23000,
      end: 1000000
    };
    $scope.activeTab = $scope.navTabs.fromTab;
    //Call method under test
    $scope.showSessionGraph();
    var url = '#/device/graph/' + $scope.sessionParams.ipAddr + '/' + 'from' + '?start=' + $scope.sessionParams.start + '&end=' + $scope.sessionParams.end;
    expect(window.location.hash).toEqual(url);

  });
  it('set window to graph location for to', function () {
    $scope.sessionParams = {
      ipAddr: '192.168.1.255',
      start: 23000,
      end: 1000000
    };
    $scope.activeTab = $scope.navTabs.toTab;
    //Call method under test
    $scope.showSessionGraph();
    var url = '#/device/graph/' + $scope.sessionParams.ipAddr + '/' + 'to' + '?start=' + $scope.sessionParams.start + '&end=' + $scope.sessionParams.end;
    expect(window.location.hash).toEqual(url);

  });
});