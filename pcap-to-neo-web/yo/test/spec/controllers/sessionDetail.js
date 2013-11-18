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
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, sessionDetailByIp) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/?destaddr=&startdate=&enddate=').respond(null);
    sessionDetailCtrl = $controller('sessionDetailCtrl', {
      sessionDetailByIp: sessionDetailByIp,
      $scope: scope
    });
    mockBackend.flush();
  }));

  it('should retrieve sessions', function () {
    scope.start = 1000;
    scope.end = 2000;
    scope.srcAddr = '102.1.1.1';
    scope.destAddr = '3.1.32.1';

    var sessions = [{
      id: 1
    }, {
      id: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/session/ip/detail/ipaddr/src/' + scope.srcAddr + '?destaddr=' + scope.destAddr + '&startdate=' + scope.start + '&enddate=' + scope.end).respond(sessions);

    expect(scope.loading).toEqual(false);

    //Call method under test
    scope.setSessions();

    expect(scope.loading).toEqual(true);

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.sessions).toEqualData(sessions);

  });
});