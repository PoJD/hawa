'use strict';

/* Controllers */

angular.module('homeAutomation.controllers', [])

.controller(
		'HomeController',
		[ '$scope', '$interval', 'systemState', 'rooms', 'outdoor',
				function($scope, $interval, systemState, rooms, outdoor) {
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
						$scope.outdoor = outdoor.query();
					};

					$scope.$on('$destroy', function() {
						$scope.stopUpdates();
					});

					$scope.switchedRooms = function($scope, $index) {
						$scope.rooms[$index].$save();
					};
					
					$scope.switchedOutdoor = function($scope) {
						$scope.outdoor.$save();
					};

					$scope.update();
					$scope.autoUpdate();
				} ])

.controller('SystemController', [ '$scope', function($scope) {

} ]);
