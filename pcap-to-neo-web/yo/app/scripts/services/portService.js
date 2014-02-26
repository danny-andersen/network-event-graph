'use strict';

angular.module('networkEventGraphApp')
  .service('portService', function portService($resource) {
    this.portUsage = $resource('/pcap-to-neo-web/rest/port/usage/:usageType?minPort=:startPort&maxPort=:endPort&startDate=:startTime&endDate=:endTime', {});
  });