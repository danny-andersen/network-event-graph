'use strict';

angular.module('networkEventGraphApp')
  .service('portService', function portService($resource) {
    this.portUsage = $resource('/pcap-to-neo-web/rest/port/usage/:usageType?minPort=:startPort&maxPort=:endPort&startDate=:startTime&endDate=:endTime', {});
    this.portUsageOfDevice = $resource('/pcap-to-neo-web/rest/port/usage/device/:deviceId?startDate=:startTime&endDate=:endTime', {});
  });