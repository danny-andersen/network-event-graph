/*jslint white:true */

function DeviceCtrl($scope, DeviceModel, DeviceByIpAddr, LocalDevices, RemoteDevices) {
	$scope.data = {};
	$scope.data.loading = false;
	$scope.data.query = DeviceModel.getQuery();
	$scope.data.devices = DeviceModel.getDevices();
	var setDevices = function(devices) {
		$scope.data.loading = false;
		DeviceModel.setQuery($scope.data.query);
		DeviceModel.devices = devices;
	};
	$scope.getDevices = function() {
		$scope.data.loading = true;
		$scope.data.query = "Ipaddr: " + $scope.ipaddr;
		$scope.data.devices = DeviceByIpAddr.query({
			'ipAddr': $scope.ipaddr
		}, setDevices);
	};
	$scope.getLocalDevices = function() {
		$scope.data.loading = true;
		$scope.data.query = "All Local devices";
		$scope.data.devices = LocalDevices.query({}, setDevices);
	};
	$scope.getRemoteDevices = function() {
		$scope.data.loading = true;
		$scope.data.query = "All Remote devices";
		$scope.data.devices = RemoteDevices.query({}, setDevices);
	};
	$scope.addDevice = function(deviceId) {
		$scope.menuItems.push({"href" : "#/device/"+deviceId, "name": "Device(id="+deviceId+")" });
	};
}

