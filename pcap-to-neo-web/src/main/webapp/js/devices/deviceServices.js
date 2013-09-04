var services = angular.module('deviceServices', ['ngResource']);

services.factory('LocalDevice', function($resource){
  return $resource('device/local.json', {}, {
    query: {method:'GET', params:{}, isArray:true}
  });
});

services.factory('RemoteDevice', function($resource){
  return $resource('device/remote.json', {}, {
    query: {method:'GET', params:{}, isArray:true}
  });
});

services.factory('DeviceByIpAddr', function($resource){
  return $resource('device/:ipAddr.json', {}, {
    query: {method:'GET', params:{ipAddr: 'ipAddr'}, isArray:true}
  });
});
