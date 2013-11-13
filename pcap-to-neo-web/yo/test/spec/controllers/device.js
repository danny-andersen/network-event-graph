'use strict';

describe('Controller: DeviceCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));
  //Set up Matcher
  beforeEach(function(){
    this.addMatchers({
      toEqualData: function(expected) {
        return angular.equals(this.actual, expected);
      }
    });
  });

  var DeviceCtrl,
    devModel,
    mockBackend,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, _$httpBackend_, deviceModel, deviceByIpAddr) {
    scope = $rootScope.$new();
    DeviceCtrl = $controller('DeviceCtrl', {
      deviceModel: deviceModel, 
      deviceByIpAddr: deviceByIpAddr,
      $scope: scope
    });
  }));

  it('retrieve device details if not already in model', function () {
    
  });
});
