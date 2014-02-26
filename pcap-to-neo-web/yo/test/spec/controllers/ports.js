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

  it('should retrieve session port usage', function () {
    scope.startPort = 1000;
    scope.endPort = 2000;
    scope.sessionParams = {
      'start': 0,
      'end': 55555
    };

    var ports = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/port/usage/session?minPort=' + scope.startPort + '&maxPort=' + scope.endPort + '&startDate=' + scope.sessionParams.start + '&endDate=' + scope.sessionParams.end).respond(ports);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.getPortUsage();

    expect(scope.loading).toEqual(true);

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.ports).toEqualData(ports);

  });

  it('should retrieve device port usage', function () {
    scope.sessionParams = {
      'start': 0,
      'end': 55555
    };

    var ports = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/port/usage/device?minPort=0&maxPort=65535&startDate=' + scope.sessionParams.start + '&endDate=' + scope.sessionParams.end).respond(ports);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.getPortUsage('device');

    expect(scope.loading).toEqual(true);

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.ports).toEqualData(ports);

  });
});