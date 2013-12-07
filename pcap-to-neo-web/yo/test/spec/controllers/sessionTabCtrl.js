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
    graph,
    timeout,
    $scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $window,
    webSitesByIp, webSitesByHostname, ipSessionService, graphService, $timeout) {
    $scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    window = $window;
    timeout = $timeout;
    graph = graphService;
    sessionTabCtrl = $controller('sessionTabCtrl', {
      $scope: $scope,
      $window: $window,
      webSitesByIp: webSitesByIp,
      webSitesByHostname: webSitesByHostname,
      ipSessionService: ipSessionService,
      graphService: graphService
    });
  }));

  it('retrieve websites by hostname when tab selected', function () {
    $scope.sessionParams = {};
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
    $scope.$parent.detail.device.hostName = "danny";
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

  it('retrieve websites by Ip Address when tab selected', function () {
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    expect($scope.timePhrase).toEqual("for all dates");

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.allSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve all sessions By Ip Address already retrieved', function () {
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    expect($scope.refresh).toEqual(false);
    expect($scope.timePhrase).not.toBe(undefined);

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.allSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('clear saved sessions when period changed', function () {
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
    $scope.$parent.detail.device.allSessions = [{
      id: 1
    }];
    // $scope.sessionParams = {
    //   start: 0,
    //   end: 1000000
    // };
    // $scope.sessionParams.ipAddr = '192.168.1.255';
    // var sessions = [{
    //   id: 1
    // }, {
    //   id: 2
    // }];

    // var params = $scope.sessionParams.ipAddr + '?startdate=' + $scope.sessionParams.start + '&enddate=' + $scope.sessionParams.end;
    // mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions);
    // expect($scope.loading).toEqual(false);

    //Change period and apply to force refresh
    $scope.activeTab = $scope.navTabs.allTab;
    $scope.$parent.period = {};
    $scope.$parent.period.changed = true;
    $scope.$apply();

    // expect($scope.loading).toEqual(true);
    expect($scope.detail.device.allSessions).toEqual([]);
    // expect($scope.currentSessions).toEqual([]);
    // expect($scope.direction).toEqual("from/to");

    // mockBackend.flush();

    // expect($scope.loading).toBe(false);
    // expect($scope.detail.device.allSessions).toEqualData(sessions);
    // expect($scope.currentSessions).toEqualData(sessions);
  });


  it('retrieve from sessions By Ip Address', function () {
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    expect($scope.timePhrase).not.toBe(undefined);

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.fromSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve from sessions By Ip Address already retrieved', function () {
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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
    expect($scope.timePhrase).not.toBe(undefined);

    mockBackend.flush();

    expect($scope.loading).toBe(false);
    expect($scope.detail.device.toSessions).toEqualData(sessions);
    expect($scope.currentSessions).toEqualData(sessions);
  });

  it('retrieve to sessions By Ip Address already retrieved', function () {
    $scope.$parent.detail = {};
    $scope.$parent.detail.device = {};
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

  it('shows the session graph', function () {
    $scope.ipAddress = '192.168.1.255';
    $scope.sessionParams = {
      ipAddr: $scope.ipAddress,
      start: 23000,
      end: 1000000
    };

    $scope.activeTab = $scope.navTabs.allTab;
    $scope.currentSessions = [{
      id: 1
    }, {
      id: 2
    }, {
      id: 3
    }];
    spyOn(graph, 'showKey');
    spyOn(graph, 'showGraph');
    //Call method under test
    $scope.showSessionGraph();

    expect(graph.showKey.calls.length).toEqual(1);
    expect(graph.showGraph.calls.length).toEqual(0);

    timeout.flush();

    expect(graph.showGraph.calls.length).toEqual(1);
    expect(graph.showGraph.calls[0].args[0]).toEqual($scope);
    expect(graph.showGraph.calls[0].args[1]).toEqual(window);
    expect(graph.showGraph.calls[0].args[2]).toEqualData($scope.currentSessions);
    expect(graph.showGraph.calls[0].args[3]).toEqual($scope.sessionParams.ipAddr);

  });

});