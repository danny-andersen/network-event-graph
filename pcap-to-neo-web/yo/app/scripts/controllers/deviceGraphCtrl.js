'use strict';
angular.module('networkEventGraphApp').controller('deviceGraphCtrl', function ($scope, $window, $routeParams, graphService, sessionsByIp, sessionsBySrcIp, sessionsByDestIp) {
  var ipAddr = $routeParams.ipAddr;
  var direction = $routeParams.direction;
  $scope.filter = 'all';
  if (direction === undefined || direction === '' || direction === 'both') {
    direction = 'from/to';
  }
  $scope.direction = direction;
  $scope.ipaddr = ipAddr;
  $scope.hasDate = false;
  //Set date for view
  if ($routeParams.start !== undefined && $routeParams.end !== undefined) {
    var d = new Date();
    d.setTime($routeParams.start * 1000);
    $scope.startDate = d.toUTCString();
    d.setTime($routeParams.end * 1000);
    $scope.endDate = d.toUTCString();
    $scope.hasDate = true;
  }

  $scope.filterSessions = function (direction) {
    graphService.filterSessions(direction);
  };

  graphService.showKey();
  var sessions = [];
  switch (direction) {
  case 'from':
    sessions = sessionsBySrcIp.query({
      'ipAddr': ipAddr,
      'start': $routeParams.start,
      'end': $routeParams.end
    }, function () {
      graphService.showGraph($scope, $window, sessions, ipAddr);
    });
    break;
  case 'to':
    sessions = sessionsByDestIp.query({
      'ipAddr': ipAddr,
      'start': $routeParams.start,
      'end': $routeParams.end
    }, function () {
      graphService.showGraph($scope, $window, sessions, ipAddr);
    });
    break;
  default:
    sessions = sessionsByIp.query({
      'ipAddr': ipAddr,
      'start': $routeParams.start,
      'end': $routeParams.end
    }, function () {
      graphService.showGraph($scope, $window, sessions, ipAddr);
    });
    break;
  }
});