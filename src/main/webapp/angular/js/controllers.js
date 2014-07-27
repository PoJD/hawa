'use strict';

/* Controllers */

angular.module('homeAutomation.controllers', [])

.controller(
		'HomeController',
		[ '$scope', '$interval', 'systemState', 'rooms',
				function($scope, $interval, systemState, rooms) {
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
						$scope.systemProperties = systemState.query();
						$scope.rooms = rooms.query();
					};

					$scope.$on('$destroy', function() {
						$scope.stopUpdates();
					});

					$scope.switched = function($scope, $index) {
						// avoid Jersey throwing errors about unknown fields (this field will be set by Angular after first save and fetch from server)
						$scope.rooms[$index].$resolved = undefined;
						$scope.rooms[$index].$save();
					};

					$scope.update();
					$scope.autoUpdate();
				} ])

.controller('SystemController', [ '$scope', function($scope) {

} ]);
