'use strict';

describe('Controller: ProtocolsCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var ProtocolsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ProtocolsCtrl = $controller('ProtocolsCtrl', {
      $scope: scope
    });
  }));

});