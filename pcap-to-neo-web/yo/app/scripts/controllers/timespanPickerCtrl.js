'use strict';

angular.module('networkEventGraphApp').controller('TimespanPickerCtrl', function ($scope, $timeout) {
  //Hello

  var date = new Date();
  date.setHours(0);
  date.setMinutes(0);

  $scope.period = {
    timeType: -1,
    showTimePicker: false,
    fromDate: new Date(0),
    fromTime: '00:00',
    toTime: '00:00',
    fromOpened: false,
    toOpened: false,
    currentDate: date
  };
  var d = new Date(date.getTime());
  d.setHours(23);
  d.setMinutes(59);
  d.setSeconds(59);
  $scope.period.toDate = d;

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
    var parts = $scope.period.fromTime.split(':');
    var ftime = Number(parts[0]) * 60 + Number(parts[1]);
    parts = $scope.period.toTime.split(':');
    var ttime = Number(parts[0]) * 60 + Number(parts[1]);
    if ($scope.period.fromDate !== undefined) {
      var secs = $scope.period.fromDate.getTime() / 1000.0;
      $scope.sessionParams.start = Number(secs.toFixed(0)) + ftime;
    }
    if ($scope.period.toDate !== undefined) {
      $scope.sessionParams.end = Number(($scope.period.toDate.getTime() / 1000.0).toFixed(0)) + ttime;
    }
  };

  var setTimePeriod = function () {
    var t;
    switch ($scope.period.timeType) {
    case 0:
      //Today
      $scope.period.showTimePicker = true;
      $scope.period.fromDate = $scope.period.currentDate;
      setPeriodParams();
      break;
    case -1:
      //None  
      // $scope.period.fromDate = $scope.period.currentDate;
      $scope.period.showTimePicker = false;
      break;
    default:
      //Number of t days ago
      $scope.period.showTimePicker = true;
      t = $scope.period.currentDate.getTime();
      t -= $scope.period.timeType * 24 * 3600 * 1000;
      var d = new Date(t);
      $scope.period.fromDate = d;
      setPeriodParams();
      break;
    }
  };

  $scope.$watch('period.timeType', function () {
    setTimePeriod($scope.period.currentDate);
  });
  $scope.$watch('period.fromDate', setPeriodParams);
  $scope.$watch('period.fromTime', setPeriodParams);
  $scope.$watch('period.toDate', setPeriodParams);
  $scope.$watch('period.toTime', setPeriodParams);
});