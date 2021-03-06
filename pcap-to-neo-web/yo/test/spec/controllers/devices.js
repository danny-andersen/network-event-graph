'use strict';

describe('Controller: DevicesCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var DevicesCtrl,
    $scope,
    devModel,
    mockBackend;

  //Set up Matcher
  beforeEach(function () {
    this.addMatchers({
      toEqualData: function (expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });

  // Initialize the controller and a mock $scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, deviceModel, deviceService) {
    $scope = $rootScope.$new();
    mockBackend = _$httpBackend_;
    devModel = deviceModel;
    DevicesCtrl = $controller('DevicesCtrl', {
      $scope: $scope,
      deviceModel: deviceModel,
      deviceService: deviceService
    });
  }));


  it('should retrieve devices by an ipaddress and set the deviceModel', function () {
    $scope.ipaddr = "192.168.*";
    var devices = [{
      deviceId: 1
    }, {
      deviceId: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/device/ipaddr/' + $scope.ipaddr).respond(devices);
    expect($scope.data.loading).toBe(false);
    expect($scope.data.query).toBe('');
    expect($scope.data.devices).toBeUndefined();

    //Call method undertest
    $scope.getDevices();
    expect($scope.data.loading).toBe(true);
    expect($scope.data.query).toBe('Ipaddr: 192.168.* ');

    mockBackend.flush();

    expect($scope.data.loading).toBe(false);
    expect(devModel.getQuery()).toBe($scope.data.query);
    expect(devModel.getDevices()).toEqualData(devices);

  });

  it('should retrieve devices by a port and set the deviceModel', function () {
    $scope.port = "53";
    var devices = [{
      deviceId: 1
    }, {
      deviceId: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/device/port/' + $scope.port).respond(devices);
    expect($scope.data.loading).toBe(false);
    expect($scope.data.query).toBe('');
    expect($scope.data.devices).toBeUndefined();

    //Call method undertest
    $scope.getDevices();
    expect($scope.data.loading).toBe(true);
    expect($scope.data.query).toBe('Port: 53 ');

    mockBackend.flush();

    expect($scope.data.loading).toBe(false);
    expect(devModel.getQuery()).toBe($scope.data.query);
    expect(devModel.getDevices()).toEqualData(devices);

  });

  it('should retrieve devices by a protcol and set the deviceModel', function () {
    $scope.protocol = "udp";
    var devices = [{
      deviceId: 1
    }, {
      deviceId: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/device/protocol/' + $scope.protocol).respond(devices);
    expect($scope.data.loading).toBe(false);
    expect($scope.data.query).toBe('');
    expect($scope.data.devices).toBeUndefined();

    //Call method undertest
    $scope.getDevices();
    expect($scope.data.loading).toBe(true);
    expect($scope.data.query).toBe('Protocol: udp');

    mockBackend.flush();

    expect($scope.data.loading).toBe(false);
    expect(devModel.getQuery()).toBe($scope.data.query);
    expect(devModel.getDevices()).toEqualData(devices);

  });

  it('should retrieve devices by an ipaddr and protocol and set the deviceModel', function () {
    $scope.ipaddr = "192.168.1.1";
    var devicesIp = [{
      deviceId: 1
    }, {
      deviceId: 2
    }];
    $scope.protocol = "udp";
    var devicesProto = [{
      deviceId: 1
    }, {
      deviceId: 3
    }];

    var devicesResult = [{
      deviceId: 1
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/device/ipaddr/' + $scope.ipaddr).respond(devicesIp);
    mockBackend.expectGET('/pcap-to-neo-web/rest/device/protocol/' + $scope.protocol).respond(devicesProto);
    expect($scope.data.loading).toBe(false);
    expect($scope.data.query).toBe('');
    expect($scope.data.devices).toBeUndefined();

    //Call method undertest
    $scope.getDevices();
    expect($scope.data.loading).toBe(true);
    expect($scope.data.query).toBe('Ipaddr: 192.168.1.1 Protocol: udp');

    mockBackend.flush();

    expect($scope.data.loading).toBe(false);
    expect(devModel.getQuery()).toBe($scope.data.query);
    expect(devModel.getDevices()).toEqualData(devicesResult);

  });

  it('should retrieve local devices and set the deviceModel', function () {
    var devices = [{
      deviceId: 1
    }, {
      deviceId: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/device/local').respond(devices);
    expect($scope.data.loading).toBe(false);
    expect($scope.data.query).toBe('');
    expect($scope.data.devices).toBeUndefined();

    //Call method undertest
    $scope.getLocalDevices();
    expect($scope.data.loading).toBe(true);
    expect($scope.data.query).toBe('All Local devices');

    mockBackend.flush();

    expect($scope.data.loading).toBe(false);
    expect(devModel.getQuery()).toBe($scope.data.query);
    expect(devModel.getDevices()).toEqualData(devices);

  });

  it('should retrieve all remote devices and set the deviceModel', function () {
    var devices = [{
      deviceId: 1
    }, {
      deviceId: 2
    }];

    mockBackend.expectGET('/pcap-to-neo-web/rest/device/remote').respond(devices);
    expect($scope.data.loading).toBe(false);
    expect($scope.data.query).toBe('');
    expect($scope.data.devices).toBeUndefined();

    //Call method undertest
    $scope.getRemoteDevices();
    expect($scope.data.loading).toBe(true);
    expect($scope.data.query).toBe('All Remote devices');

    mockBackend.flush();

    expect($scope.data.loading).toBe(false);
    expect(devModel.getQuery()).toBe($scope.data.query);
    expect(devModel.getDevices()).toEqualData(devices);

  });
});