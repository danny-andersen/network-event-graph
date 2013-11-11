/*jslint white:true */
'use strict';

angular.module('networkEventGraphApp')
	.directive('dsaTimespanPicker', function() {
		return {
			templateUrl: 'views/timeSpanPickerTemplate.html',
			restrict: 'E',
			replace: true,
			link: function postLink(scope, element, attrs) {
				$('#fromTimepicker').timepicker({
					showMeridian: false
				});
				$('#toTimepicker').timepicker({
					showMeridian: false
				});
			}
			// },
			// controller: function controllerConstructor($scope, $element) {
			// 	$('#fromTimepicker').timepicker({
			// 		showMeridian: false
			// 	});
			// 	$('#toTimepicker').timepicker({
			// 		showMeridian: false
			// 	});

			// }
		};
	});