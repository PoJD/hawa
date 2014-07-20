'use strict';

/* Controllers */

angular.module('homeAutomation.controllers', [])

	.controller('HomeController', [ '$scope', '$interval', 'vmState', function($scope, $interval, vmState) {
			$scope.orderProp = 'name';

			var updates;
			$scope.autoUpdate = function() {
				if (angular.isDefined(updates))
					return;

				updates = $interval(function() {
					$scope.update();
				}, 5000);
			};

			$scope.stopUpdates = function() {
				if (angular.isDefined(updates)) {
					$interval.cancel(updates);
					updates = undefined;
				}
			};

			$scope.update = function() {
				$scope.vmProperties = vmState.query();
			};

			$scope.$on('$destroy', function() {
				$scope.stopUpdates();
			});
			
			$scope.update();
			$scope.autoUpdate();
		} ])
		
	.controller('SystemController', [ '$scope', function($scope) {

} ]);
