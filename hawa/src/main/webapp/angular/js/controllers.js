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
						}, 5 * 60 * 1000);
					};

					$scope.stopUpdates = function() {
						if (angular.isDefined(updates)) {
							$interval.cancel(updates);
							updates = undefined;
						}
					};

					$scope.update = function() {
						$scope.systemState = systemState.query();
						$scope.rooms = rooms.query();
						$scope.outdoor = outdoor.query();
					};

					$scope.$on('$destroy', function() {
						$scope.stopUpdates();
					});

					$scope.update();
					$scope.autoUpdate();
				} ])

.controller('SystemController', [ '$scope', function($scope) {

} ])

.controller('RoomController', [ '$scope', '$routeParams', function($scope, $routeParams) {
    $scope.roomName = $routeParams.roomName;
} ]);
