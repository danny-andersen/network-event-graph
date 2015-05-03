'use strict';

describe('Controller: MenuCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MainCtrl = $controller('menuCtrl', {
      $scope: scope
    });
  }));

  it('should set menu items and links', function () {
    expect(scope.items.menuItems.length).toBe(3);
    expect(scope.items.menuItems[scope.sessionMenuId].name).toEqual('Sessions');
    expect(scope.items.menuItems[scope.sessionMenuId].href).toEqual('#/allSessionGraph');
    expect(scope.items.menuItems[scope.deviceMenuId].name).toEqual('Devices');
    expect(scope.items.menuItems[scope.deviceMenuId].href).toEqual('#/devices');
    expect(scope.items.menuItems[scope.portMenuId].name).toEqual('Ports');
    expect(scope.items.menuItems[scope.portMenuId].href).toEqual('#/ports');
  });

});