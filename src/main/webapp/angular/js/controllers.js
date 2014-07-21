'use strict';

/* Controllers */

angular.module('homeAutomation.controllers', [])

	.controller('HomeController', [ '$scope', '$interval', 'systemState', function($scope, $interval, systemState) {
			$scope.orderProp = 'name';

			var updates;
			$scope.autoUpdate = function() {
				if (angular.isDefined(updates))
					return;

				updates = $interval(function() {
					$scope.update();
				}, 60000);
			};

			$scope.stopUpdates = function() {
				if (angular.isDefined(updates)) {
					$interval.cancel(updates);
					updates = undefined;
				}
			};

			$scope.update = function() {
				$scope.allProperties = systemState.query();
			};

			$scope.$on('$destroy', function() {
				$scope.stopUpdates();
			});
			
			$scope.update();
			$scope.autoUpdate();
		} ])
		
	.controller('SystemController', [ '$scope', function($scope) {

} ]);
