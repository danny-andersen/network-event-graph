'use strict';

angular.module('networkEventGraphApp').controller('TimespanPickerCtrl', function ($scope, $timeout) {

  $scope.currentDate = new Date();
  $scope.currentDate.setHours(0);
  $scope.currentDate.setMinutes(0);
  $scope.currentDate.setSeconds(0);

  $scope.resetSpan = function () {

    $scope.period = {
      timeType: -1,
      showTimePicker: false,
      fromDate: new Date(0),
      fromTime: '00:00:00',
      toTime: '23:59:59',
      fromOpened: false,
      toOpened: false,
      changed: false
    };
    var d = new Date($scope.currentDate.getTime());
    d.setHours(0);
    d.setMinutes(0);
    d.setSeconds(0);
    $scope.period.toDate = d;
  };

  $scope.resetSpan();

  $scope.period.dateOptions = {
    'year-format': '"yy"',
    'starting-day': 1
  };

  $scope.fromOpen = function () {
    $timeout(function () {
      $scope.fromOpened = true;
    });
  };

  $scope.toOpen = function () {
    $timeout(function () {
      $scope.toOpened = true;
    });
  };
  var setPeriodParams = function () {
    var secs;
    var parts = $scope.period.fromTime.split(':');
    var ftime = Number(parts[0]) * 3600 + Number(parts[1] * 60 + Number(parts[2]));
    parts = $scope.period.toTime.split(':');
    var ttime = Number(parts[0]) * 3600 + Number(parts[1] * 60 + Number(parts[2]));
    if ($scope.period.fromDate !== undefined) {
      secs = $scope.period.fromDate.getTime() / 1000.0;
      $scope.sessionParams.start = Number(secs.toFixed(0)) + ftime;
    }
    if ($scope.period.toDate !== undefined) {
      secs = $scope.period.toDate.getTime() / 1000.0;
      $scope.sessionParams.end = Number(secs.toFixed(0)) + ttime;
    }
    $scope.period.changed = true;
  };

  var setTimePeriod = function () {
    var t;
    switch ($scope.period.timeType) {
    case 0:
      //Today
      $scope.period.showTimePicker = true;
      $scope.period.fromDate = $scope.currentDate;
      setPeriodParams();
      break;
    case -1:
      //None  
      // $scope.period.fromDate = $scope.period.currentDate;
      $scope.period.showTimePicker = false;
      $scope.resetSpan();
      setPeriodParams();
      break;
    default:
      //Number of t days ago
      $scope.period.showTimePicker = true;
      t = $scope.currentDate.getTime();
      t -= $scope.period.timeType * 24 * 3600 * 1000;
      var d = new Date(t);
      $scope.period.fromDate = d;
      setPeriodParams();
      break;
    }
  };

  $scope.$watch('period.timeType', function () {
    setTimePeriod();
  });
  $scope.$watch('period.fromDate', setPeriodParams);
  $scope.$watch('period.fromTime', setPeriodParams);
  $scope.$watch('period.toDate', setPeriodParams);
  $scope.$watch('period.toTime', setPeriodParams);
});