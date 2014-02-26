'use strict';

describe('Controller: PortsCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var PortsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    PortsCtrl = $controller('PortsCtrl', {
      $scope: scope
    });
  }));

});