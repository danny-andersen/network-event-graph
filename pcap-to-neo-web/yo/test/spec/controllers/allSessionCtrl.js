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
    byIp,
    bySrcIp,
    byDestIp,
    routeParams,
    graph,
    model,
    allSessionCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, $window, $timeout, graphService, sessionsByIp, deviceModel, deviceByIpAddr, localDevices, remoteDevices) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    window = $window;
    graph = graphService;
    model = deviceModel;
    spyOn(graph, 'showKey');
    allSessionCtrl = $controller('allSessionCtrl', {
      $scope: scope,
      $window: $window,
      graphService: graphService,
      sessionsByIp: sessionsByIp,
      deviceModel: deviceModel,
      deviceByIpAddr: deviceByIpAddr,
      localDevices: localDevices,
      remoteDevices: remoteDevices
    });
  }));

  it('controller initialised correctly', function () {
    expect(graph.showKey).toHaveBeenCalled();
    expect(scope.data).toEqual({});
    expect(scope.timeType).toEqual(-1);
    expect(scope.fromDate.getTime()).toEqual(0);
    expect(scope.data.devices).toBe(undefined);
  });

  it('sets time range to today if timeType is 0', function () {
    var currentDate = new Date();
    currentDate.setHours(0);
    currentDate.setMinutes(0);
    currentDate.setSeconds(0);
    currentDate.setMilliseconds(0);
    scope.timeType = 0;

    //Call method
    scope.setTimePeriod();

    expect(scope.fromDate.getTime()).toEqual(currentDate.getTime());
  });

  it('sets time range to past week if timeType is 7', function () {
    var currentDate = new Date();
    currentDate.setHours(0);
    currentDate.setMinutes(0);
    currentDate.setSeconds(0);
    currentDate.setMilliseconds(0);
    var fromTime = currentDate.getTime() - (7 * 24 * 3600 * 1000);
    scope.timeType = 7;

    //Call method
    scope.setTimePeriod();

    expect(scope.fromDate.getTime()).toEqual(fromTime);
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
    var params = '1.1.1.1' + '?startdate=&enddate=';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions1);
    params = '2.2.2.2' + '?startdate=&enddate=';
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/summary/ipaddr/' + params).respond(sessions2);
    spyOn(graph, 'plotSessions');

    expect(scope.graphLoading).toEqual(false);
    //Call method under test
    scope.plotGraph();

    expect(scope.graphLoading).toEqual(true);
    expect(scope.data.devices).toEqual(devices);
    expect(graph.initGraph.calls.length).toEqual(1);
    expect(graph.plotDevices.calls.length).toEqual(1);

    //Flush tha call to the mock
    mockBackend.flush();
    expect(scope.graphLoading).toEqual(false);
    expect(graph.plotSessions.calls.length).toEqual(2);
    expect(graph.plotSessions.calls[0].args[0]).toEqual(scope);
    expect(graph.plotSessions.calls[0].args[1]).toEqual(window);
    expect(graph.plotSessions.calls[0].args[4]).toEqual('1.1.1.1');
    expect(graph.plotSessions.calls[0].args[5]).toEqualData(sessions1);
    expect(graph.plotSessions.calls[1].args[4]).toEqual('2.2.2.2');
    expect(graph.plotSessions.calls[1].args[5]).toEqualData(sessions2);
  });
});