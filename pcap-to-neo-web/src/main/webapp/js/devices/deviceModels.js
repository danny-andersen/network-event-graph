/*jslint white:true */

var devModels = angular.module('deviceModels', ['deviceServices']);

devModels.service('DeviceModel', [function(DeviceDetailById) {
	this.deviceDetails = {};
	this.getDevices = function() {
		return this.devices;
	};
	this.setDevices = function(devices) {
		this.devices = devices;
	};
	this.setDeviceDetail = function(deviceId, device) {
		this.deviceDetails[deviceId] = device;
	};
	this.getDeviceDetail = function(deviceId) {
		return this.deviceDetails[deviceId];
	};
}]);