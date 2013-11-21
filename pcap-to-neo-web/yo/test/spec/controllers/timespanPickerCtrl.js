/*jslint white:true */

'use strict';

describe('Controller: TimespanPickerCtrl', function () {

  // load the controller's module
  beforeEach(module('networkEventGraphApp'));

  var timespanPickerCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    scope.sessionParams = {};
    timespanPickerCtrl = $controller('TimespanPickerCtrl', {
      $scope: scope
    });
  }));

  it('sets timespan to none', function () {
    var startDate = new Date(2013, 11, 12, 0, 0, 0, 0);
    scope.currentDate = startDate;
    var endDate = new Date(2013, 11, 12, 23, 59, 59, 0);
    scope.period.toDate = endDate;

    //Run test
    scope.period.timeType = -1;
    expect(scope.period.changed).toEqual(false);
    //Trigger the watch on timeType
    scope.$apply();

    var start = startDate.getTime() / 1000;
    expect(scope.sessionParams.start).toEqual(0);
    expect(scope.sessionParams.end).toEqual(endDate.getTime() / 1000);
    expect(scope.period.changed).toEqual(true);
  });

  it('sets timespan to today', function () {
    var current = new Date(2013, 11, 12, 0, 0, 0, 0);
    scope.currentDate = current;
    var endDate = new Date(2013, 11, 12, 23, 59, 59, 0);
    scope.period.toDate = endDate;

    //Run test
    scope.period.timeType = 0;
    //Trigger the watch on timeType
    scope.$apply();

    var start = current.getTime() / 1000;
    // console.log("Start: " + start);
    expect(scope.sessionParams.start).toEqual(start);
    expect(scope.sessionParams.end).toEqual(endDate.getTime() / 1000);
  });

  it('sets timespan to last week', function () {
    var current = new Date(2013, 11, 12, 0, 0, 0, 0);
    scope.currentDate = current;
    var endDate = new Date(2013, 11, 12, 23, 59, 59, 0);
    scope.period.toDate = endDate;

    //Run test
    scope.period.timeType = 7;
    //Trigger the watch on timeType
    scope.$apply();

    var startDate = new Date(2013, 11, 5, 0, 0, 0, 0);
    var start = startDate.getTime() / 1000;
    expect(scope.sessionParams.start).toEqual(start);
    expect(scope.sessionParams.end).toEqual(endDate.getTime() / 1000);
  });

  it('sets timespan to last month', function () {
    var current = new Date(2013, 11, 12, 0, 0, 0, 0);
    scope.currentDate = current;
    var endDate = new Date(2013, 11, 12, 23, 59, 59, 0);
    scope.period.toDate = endDate;

    //Run test
    scope.period.timeType = 30;
    //Trigger the watch on timeType
    scope.$apply();

    var startDate = new Date(2013, 10, 12, 0, 0, 0, 0);
    var start = startDate.getTime() / 1000;
    expect(scope.sessionParams.start).toEqual(start);
    expect(scope.sessionParams.end).toEqual(endDate.getTime() / 1000);
  });

  it('change timespan from today to none', function () {
    var current = new Date(2013, 11, 12, 0, 0, 0, 0);
    scope.currentDate = current;
    var endDate = new Date(2013, 11, 12, 23, 59, 59, 0);
    scope.period.toDate = endDate;

    //Run test

    //Set to Today
    scope.period.timeType = 0;
    //Trigger the watch on timeType
    scope.$apply();

    var start = current.getTime() / 1000;
    expect(scope.sessionParams.start).toEqual(start);
    expect(scope.sessionParams.end).toEqual(endDate.getTime() / 1000);

    //Change to none
    scope.period.timeType = -1;
    //Trigger the watch on timeType
    scope.$apply();
    expect(scope.sessionParams.start).toEqual(0);
    expect(scope.sessionParams.end).toEqual(endDate.getTime() / 1000);

  });


});