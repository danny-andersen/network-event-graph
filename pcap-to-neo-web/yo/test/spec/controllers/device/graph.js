'use strict';

describe('Controller: DeviceGraphCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var DeviceGraphCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    DeviceGraphCtrl = $controller('DeviceGraphCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
