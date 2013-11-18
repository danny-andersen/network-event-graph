'use strict';

describe('Controller: DeviceCtrl', function () {

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

  var DeviceCtrl,
    devModel,
    mockBackend,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, deviceModel, deviceByIpAddr, deviceDetailById) {
    scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    devModel = deviceModel;
    DeviceCtrl = $controller('DeviceCtrl', {
      deviceModel: deviceModel,
      deviceByIpAddr: deviceByIpAddr,
      deviceDetailById: deviceDetailById,
      $scope: scope
    });
  }));

  it('retrieve device details', function () {
    var devId = 1;
    var detail = {
      id: 1,
      ipaddr: [{
        ipAddr: '192.168.1.255'
      }]
    };
    mockBackend.expectGET('/pcap-to-neo-web/rest/device/detail/' + devId).respond(detail);

    expect(scope.loading).toEqual(false);
    //Call method under test
    scope.setDeviceDetailById(devId);
    expect(scope.loading).toEqual(true);
    expect(scope.detail.id).toEqual(devId);
    expect(scope.detail.device).toEqualData({});

    mockBackend.flush();

    expect(scope.sessionParams.ipAddr).toEqual(detail.ipaddr[0].ipAddr);
    detail.hasDescription = false;
    expect(devModel.getDeviceDetail(devId)).toEqualData(detail);
    expect(scope.detail.device).toEqualData(detail);

  });

  it('retrieve device details with desc', function () {
    var devId = 1;
    var detail = {
      id: 1,
      description: 'blah blah',
      ipaddr: [{
        ipAddr: '192.168.1.255'
      }]
    };
    mockBackend.expectGET('/pcap-to-neo-web/rest/device/detail/' + devId).respond(detail);

    expect(scope.loading).toEqual(false);
    //Call method under test
    scope.setDeviceDetailById(devId);
    expect(scope.loading).toEqual(true);
    expect(scope.detail.id).toEqual(devId);
    expect(scope.detail.device).toEqualData({});

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.sessionParams.ipAddr).toEqual(detail.ipaddr[0].ipAddr);
    detail.hasDescription = true;
    expect(devModel.getDeviceDetail(devId)).toEqualData(detail);
    expect(scope.detail.device).toEqualData(detail);

  });

  it('retrieve device details by Ip addr', function () {
    var devId = 1;
    var ip = '192.168.1.1';
    var detail = [{
      deviceId: devId,
      ipaddr: [{
        ipAddr: ip
      }]
    }];
    mockBackend.expectGET('/pcap-to-neo-web/rest/device/ipaddr/' + ip).respond(detail);

    expect(scope.loading).toEqual(false);
    //Call method under test
    scope.setDeviceDetailByIpAddr(ip);
    expect(scope.loading).toEqual(true);
    expect(scope.detail.device).toEqualData(undefined);

    mockBackend.flush();

    expect(scope.loading).toEqual(false);
    expect(scope.detail.id).toEqual(devId);
    expect(scope.sessionParams.ipAddr).toEqual(detail[0].ipaddr[0].ipAddr);
    detail[0].hasDescription = false;
    expect(devModel.getDeviceDetail(devId)).toEqualData(detail[0]);
    expect(scope.detail.device).toEqualData(detail[0]);

  });


});