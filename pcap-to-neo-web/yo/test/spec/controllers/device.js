'use strict';

describe('Controller: DeviceCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var DeviceCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DeviceCtrl = $controller('DeviceCtrl', {
      $scope: scope
    });
  }));

  it('retrieve device details if not already in model', function () {
    
  });
});
