'use strict';

angular.module('networkEventGraphApp').directive('dsaTimespanPicker', function () {
  return {
    templateUrl: 'views/timeSpanPickerTemplate.html',
    restrict: 'E',
    replace: true,
    link: function postLink() {
      $('#fromTimepicker').timepicker({
        showMeridian: false
      });
      $('#toTimepicker').timepicker({
        showMeridian: false
      });
    }
  };
});