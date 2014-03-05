'use strict';

describe('Controller: SessiondetailCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  beforeEach(function () {
    this.addMatchers({
      toEqualData: function (expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });

  var sessionDetailCtrl,
    mockBackend,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, ipSessionService) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    // mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/?destaddr=&startdate=&enddate=&protocol=').respond(null);
    sessionDetailCtrl = $controller('sessionDetailCtrl', {
      $scope: scope,
      ipSessionService: ipSessionService
    });
    // mockBackend.flush();
  }));

  it('should retrieve sessions for an IP addr', function () {
    scope.start = 1000;
    scope.end = 2000;
    scope.srcAddr = '102.1.1.1';
    scope.destAddr = '3.1.32.1';

    var sessions = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/' + scope.srcAddr + '?destaddr=' + scope.destAddr + '&startdate=' + scope.start + '&enddate=' + scope.end + '&protocol=').respond(sessions);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.setSessions();

    expect(scope.loading).toEqual(true);

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.tableTitle).toContain("Sessions from ipaddr");
    expect(scope.sessions).toEqualData(sessions);

  });

  it('should retrieve sessions for a device', function () {
    scope.start = 1000;
    scope.end = 2000;
    scope.deviceId = '5';
    scope.protocol = 'udp';

    var sessions = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/detail/device/' + scope.deviceId + '?startdate=' + scope.start + '&enddate=' + scope.end + '&protocol=' + scope.protocol).respond(sessions);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.setSessions();

    expect(scope.loading).toEqual(true);
    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.tableTitle).toContain("Sessions from device");
    expect(scope.tableTitle).toContain("using protocol " + scope.protocol);
    expect(scope.sessions).toEqualData(sessions);

  });

  it('should retrieve sessions for a port', function () {
    scope.start = 1000;
    scope.end = 2000;
    scope.port = '500';
    scope.protocol = 'tcp';

    var sessions = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/detail/port/' + scope.port + '?startdate=' + scope.start + '&enddate=' + scope.end + '&protocol=' + scope.protocol).respond(sessions);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.setSessions();

    expect(scope.loading).toEqual(true);
    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.tableTitle).toContain("Sessions from/to port");
    expect(scope.tableTitle).toContain("using protocol " + scope.protocol);
    expect(scope.sessions).toEqualData(sessions);

  });
});