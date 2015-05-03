'use strict';

describe('Controller: PortsCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var PortsCtrl,
    mockBackend,
    scope;

  beforeEach(function () {
    this.addMatchers({
      toEqualData: function (expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    PortsCtrl = $controller('portsCtrl', {
      $scope: scope
    });
  }));

  it('should retrieve port usage', function () {
    scope.startPort = 1000;
    scope.endPort = 2000;
    scope.sessionParams = {
      'start': 0,
      'end': 55555
    };

    var ports = [{
      "sessionCount": 300,
      "deviceCount": 0,
      "port": {
        "portId": 31,
        "port": 1900
      }
    }, {
      "sessionCount": 100,
      "deviceCount": 0,
      "port": {
        "portId": 31,
        "port": 100
      }
    }];

    var portsDevs = [{
      "sessionCount": 0,
      "deviceCount": 50,
      "port": {
        "portId": 31,
        "port": 1900
      }
    }, {
      "sessionCount": 0,
      "deviceCount": 10,
      "port": {
        "portId": 31,
        "port": 100
      }
    }];

    var proto = [{
      "sessionCount": 25,
      "deviceCount": 0,
      "protocol": {
        "name": "ip"
      }
    }];

    var result = [{
      "sessionCount": 300,
      "deviceCount": 50,
      "protocols": 'ip(25)',
      "port": 1900
    }, {
      "sessionCount": 100,
      "deviceCount": 10,
      "protocols": 'ip(25)',
      "port": 100
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/port/usage/session?minPort=' + scope.startPort + '&maxPort=' + scope.endPort + '&startDate=' + scope.sessionParams.start + '&endDate=' + scope.sessionParams.end).respond(ports);
    mockBackend.expectGET('/pcap-to-neo-web/rest/protocol/usage/port?port=1900&startDate=0&endDate=55555').respond(proto);
    mockBackend.expectGET('/pcap-to-neo-web/rest/protocol/usage/port?port=100&startDate=0&endDate=55555').respond(proto);
    mockBackend.expectGET('/pcap-to-neo-web/rest/port/usage/device?minPort=' + scope.startPort + '&maxPort=' + scope.endPort + '&startDate=' + scope.sessionParams.start + '&endDate=' + scope.sessionParams.end).respond(portsDevs);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.getPortUsage();

    expect(scope.loading).toEqual(true);

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.ports[0].sessionCount).toEqual(300);
    expect(scope.ports[1].sessionCount).toEqual(100);
    expect(scope.ports[0].deviceCount).toEqual(50);
    expect(scope.ports[1].deviceCount).toEqual(10);
    expect(scope.ports).toEqualData(result);

  });


});