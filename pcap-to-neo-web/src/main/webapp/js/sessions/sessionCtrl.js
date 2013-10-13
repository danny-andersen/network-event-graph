/*jslint white:true */

function SessionCtrl($scope, $routeParams, SessionDetailByIp) {
	// var params = JSON.parse($routeParams.session);
	$scope.srcAddr = $routeParams.srcIpAddr;
	$scope.destAddr = $routeParams.destIpAddr;
	$scope.start = $routeParams.start;
	$scope.end = $routeParams.end;
	$scope.loading = true;
	var start = new Date($routeParams.start * 1000).toUTCString();
	var end = new Date($routeParams.end * 1000).toUTCString();
	$scope.timePhrase = "between " + start + " and " + end;
	$scope.sessions = SessionDetailByIp.query({
		'srcAddr': $routeParams.srcIpAddr, 
		'destAddr': $routeParams.destIpAddr, 
		'start': $routeParams.start, 
		'end': $routeParams.end}, function() {
			$scope.loading = false;
		});
}