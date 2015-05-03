'use strict';

angular.module('networkEventGraphApp')
  .service('protocolService', function protocolService($resource) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    this.allProtocolUsage = $resource('/pcap-to-neo-web/rest/protocol/usage', {});
    this.protocolUsage = $resource('/pcap-to-neo-web/rest/protocol/usage?protocol=:protocol&startDate=:startTime&endDate=:endTime', {});

    this.protocolUsageByDevice = $resource('/pcap-to-neo-web/rest/protocol/usage/device?deviceId=:deviceId&startDate=:startTime&endDate=:endTime', {});

    this.protocolUsageByPort = $resource('/pcap-to-neo-web/rest/protocol/usage/port?port=:port&startDate=:startTime&endDate=:endTime', {});
  });