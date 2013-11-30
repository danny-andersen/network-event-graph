'use strict';

describe('Controller: DeviceGraphCtrl', function () {

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

  var mockBackend,
    window,
    ipService,
    bySrcIp,
    byDestIp,
    routeParams,
    graph,
    controller,
    scope;

  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $window, $routeParams, graphService, ipSessionService) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    window = $window;
    controller = $controller;
    routeParams = $routeParams;
    graph = graphService;
    ipService = ipSessionService;
  }));

  it('retrieve all session details and draws graph', function () {
    routeParams.ipAddr = '192.168.1.1';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];
    var params = routeParams.ipAddr + '?startdate=&enddate=';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions);
    //Set spy mocks
    spyOn(graph, 'showKey');
    spyOn(graph, 'showGraph');

    var DeviceGraphCtrl = controller('deviceGraphCtrl', {
      $scope: scope,
      $window: window,
      $routeParams: routeParams,
      graphService: graph,
      ipSessionService: ipService
    });

    expect(scope.filter).toEqual('all');
    expect(scope.startDate).toBe(undefined);
    expect(scope.endDate).toBe(undefined);
    expect(scope.hasDate).toBe(false);
    expect(graph.showKey.calls.length).toEqual(1);
    expect(graph.showGraph.calls.length).toEqual(0);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(graph.showGraph.calls.length).toEqual(1);
    expect(graph.showGraph.calls[0].args[0]).toEqual(scope);
    expect(graph.showGraph.calls[0].args[1]).toEqual(window);
    expect(graph.showGraph.calls[0].args[2]).toEqualData(sessions);
    expect(graph.showGraph.calls[0].args[3]).toEqual(routeParams.ipAddr);

  });

  it('retrieve all session details by date range and draws graph', function () {
    routeParams.ipAddr = '192.168.1.1';
    routeParams.start = 1000;
    routeParams.end = 343333333;
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];
    var params = routeParams.ipAddr + '?startdate=' + routeParams.start + '&enddate=' + routeParams.end;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions);
    //Set spy mocks
    spyOn(graph, 'showKey');
    spyOn(graph, 'showGraph');

    var DeviceGraphCtrl = controller('deviceGraphCtrl', {
      $scope: scope,
      $window: window,
      $routeParams: routeParams,
      graphService: graph,
      ipSessionService: ipService
    });

    expect(scope.filter).toEqual('all');
    expect(scope.startDate).not.toBe(undefined);
    expect(scope.endDate).not.toBe(undefined);
    expect(scope.hasDate).toBe(true);
    expect(graph.showKey.calls.length).toEqual(1);
    expect(graph.showGraph.calls.length).toEqual(0);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(graph.showGraph.calls.length).toEqual(1);
    expect(graph.showGraph.calls[0].args[0]).toEqual(scope);
    expect(graph.showGraph.calls[0].args[1]).toEqual(window);
    expect(graph.showGraph.calls[0].args[2]).toEqualData(sessions);
    expect(graph.showGraph.calls[0].args[3]).toEqual(routeParams.ipAddr);

  });

  it('retrieve from session details and draws graph', function () {
    routeParams.ipAddr = '192.168.1.1';
    routeParams.direction = 'from';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];
    var params = routeParams.ipAddr + '?startdate=&enddate=';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/src/' + params).respond(sessions);
    //Set spy mocks
    spyOn(graph, 'showKey');
    spyOn(graph, 'showGraph');

    var DeviceGraphCtrl = controller('deviceGraphCtrl', {
      $scope: scope,
      $window: window,
      $routeParams: routeParams,
      graphService: graph,
      ipSessionService: ipService
    });

    expect(graph.showGraph.calls.length).toEqual(0);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(graph.showGraph.calls.length).toEqual(1);

  });

  it('retrieve to session details and draws graph', function () {
    routeParams.ipAddr = '192.168.1.1';
    routeParams.direction = 'to';
    var sessions = [{
      id: 1
    }, {
      id: 2
    }];
    var params = routeParams.ipAddr + '?startdate=&enddate=';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/dest/' + params).respond(sessions);
    //Set spy mocks
    spyOn(graph, 'showKey');
    spyOn(graph, 'showGraph');

    var DeviceGraphCtrl = controller('deviceGraphCtrl', {
      $scope: scope,
      $window: window,
      $routeParams: routeParams,
      graphService: graph,
      ipSessionService: ipService
    });

    expect(graph.showGraph.calls.length).toEqual(0);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(graph.showGraph.calls.length).toEqual(1);

  });

});