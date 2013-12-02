'use strict';

describe('Controller: allSessionCtrl', function () {

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
    byIp,
    bySrcIp,
    byDestIp,
    routeParams,
    graph,
    model,
    allSessionCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $window, $timeout, $q, graphService, ipSessionService, deviceModel, deviceService) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    window = $window;
    graph = graphService;
    model = deviceModel;
    spyOn(graph, 'showKey');
    allSessionCtrl = $controller('allSessionCtrl', {
      $scope: scope,
      $window: $window,
      $q: $q,
      graphService: graphService,
      ipSessionService: ipSessionService,
      deviceModel: deviceModel,
      deviceService: deviceService
    });
  }));

  it('controller initialised correctly', function () {
    expect(graph.showKey).toHaveBeenCalled();
    expect(scope.data).toEqual({});
    expect(scope.data.devices).toBe(undefined);
  });

  it('for each device gets all their sessions and plots graph', function () {
    var devices = [{
      id: 1,
      ipaddr: [{
        ipAddr: '1.1.1.1'
      }]
    }, {
      id: 2,
      ipaddr: [{
        ipAddr: '2.2.2.2'
      }]
    }];
    var sessions1 = [{
      id: 1
    }, {
      id: 2
    }];
    var sessions2 = [{
      id: 3
    }, {
      id: 4
    }];
    model.setDevices(devices);

    //Set up mocks
    spyOn(graph, 'initGraph');
    spyOn(graph, 'plotDevices');
    spyOn(graph, 'startForceAtlas2');
    var params = '1.1.1.1?';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions1);
    params = '2.2.2.2?';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions2);
    spyOn(graph, 'plotSessions');

    expect(scope.graphLoading).toEqual(false);
    //Call method under test
    scope.plotGraph();

    expect(scope.graphLoading).toEqual(true);
    expect(scope.data.devices).toEqual(devices);
    expect(scope.retrieved).toEqual(0);
    expect(scope.percentRetrieved).toEqual([{
      "value": 0,
      "type": "success"
    }, {
      "value": 100,
      "type": "danger"
    }]);
    expect(graph.initGraph.calls.length).toEqual(1);
    expect(graph.plotDevices.calls.length).toEqual(1);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(graph.plotSessions.calls.length).toEqual(2);
    expect(scope.retrieved).toEqual(2);
    expect(scope.percentRetrieved).toEqual([{
      "value": 100,
      "type": "success"
    }, {
      "value": 0,
      "type": "danger"
    }]);
    expect(graph.plotSessions.calls[0].args[0]).toEqual(scope);
    expect(graph.plotSessions.calls[0].args[1]).toEqual(window);
    expect(graph.plotSessions.calls[0].args[4]).toEqual('1.1.1.1');
    expect(graph.plotSessions.calls[0].args[5]).toEqualData(sessions1);
    expect(graph.plotSessions.calls[1].args[4]).toEqual('2.2.2.2');
    expect(graph.plotSessions.calls[1].args[5]).toEqualData(sessions2);
    expect(scope.graphLoading).toEqual(false);
  });

  it('for each device gets sessions for date range and plots graph', function () {
    var devices = [{
      id: 1,
      ipaddr: [{
        ipAddr: '1.1.1.1'
      }]
    }, {
      id: 2,
      ipaddr: [{
        ipAddr: '2.2.2.2'
      }]
    }];
    var sessions1 = [{
      id: 1
    }, {
      id: 2
    }];
    var sessions2 = [{
      id: 3
    }, {
      id: 4
    }];
    var pstr;

    scope.sessionParams = {};
    scope.sessionParams.start = 1000;
    scope.sessionParams.end = 30000;
    model.setDevices(devices);

    //Set up mocks
    spyOn(graph, 'initGraph');
    spyOn(graph, 'plotDevices');
    spyOn(graph, 'startForceAtlas2');
    pstr = '1.1.1.1?' + 'enddate=' + scope.sessionParams.end + '&startdate=' + scope.sessionParams.start;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + pstr).respond(sessions1);
    pstr = '2.2.2.2?' + 'enddate=' + scope.sessionParams.end + '&startdate=' + scope.sessionParams.start;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + pstr).respond(sessions2);
    spyOn(graph, 'plotSessions');

    expect(scope.graphLoading).toEqual(false);
    //Call method under test
    scope.plotGraph();

    expect(scope.graphLoading).toEqual(true);
    expect(scope.data.devices).toEqual(devices);
    expect(scope.retrieved).toEqual(0);
    expect(scope.percentRetrieved).toEqual([{
      'value': 0,
      'type': 'success'
    }, {
      'value': 100,
      'type': 'danger'
    }]);
    expect(graph.initGraph.calls.length).toEqual(1);
    expect(graph.plotDevices.calls.length).toEqual(1);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(graph.plotSessions.calls.length).toEqual(2);
    expect(scope.retrieved).toEqual(2);
    expect(scope.percentRetrieved).toEqual([{
      "value": 100,
      "type": "success"
    }, {
      "value": 0,
      "type": "danger"
    }]);
    expect(graph.plotSessions.calls[0].args[0]).toEqual(scope);
    expect(graph.plotSessions.calls[0].args[1]).toEqual(window);
    expect(graph.plotSessions.calls[0].args[4]).toEqual('1.1.1.1');
    expect(graph.plotSessions.calls[0].args[5]).toEqualData(sessions1);
    expect(graph.plotSessions.calls[1].args[4]).toEqual('2.2.2.2');
    expect(graph.plotSessions.calls[1].args[5]).toEqualData(sessions2);
    expect(scope.graphLoading).toEqual(false);
  });
});