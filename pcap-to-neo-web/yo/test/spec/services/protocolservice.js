'use strict';

describe('Service: Protocolservice', function () {

  // load the service's module
  beforeEach(module('networkEventGraphApp'));

  // instantiate service
  var Protocolservice;
  beforeEach(inject(function (_Protocolservice_) {
    Protocolservice = _Protocolservice_;
  }));

  it('should do something', function () {
    expect(!!Protocolservice).toBe(true);
  });

});
