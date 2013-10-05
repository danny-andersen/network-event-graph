/*jslint white:true */

function SessionCtrl($scope, $routeParams, SessionDetailByIp) {
	// var params = JSON.parse($routeParams.session);
	$scope.srcAddr = $routeParams.srcIpAddr;
	$scope.destAddr = $routeParams.destIpAddr;
	$scope.start = $routeParams.start;
	$scope.end = $routeParams.end;
	$scope.sessions = SessionDetailByIp.query({
		'srcAddr': $routeParams.srcIpAddr, 
		'destAddr': $routeParams.destIpAddr, 
		'start': $routeParams.earliest, 
		'end': $routeParams.latest});
}