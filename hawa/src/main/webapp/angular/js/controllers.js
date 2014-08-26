'use strict';

/* Controllers */

angular.module('homeAutomation.controllers', [])

.controller(
		'HomeController',
		[ '$scope', '$interval', 'systemState', 'rooms', 'outdoor',
				function($scope, $interval, systemState, rooms, outdoor) {
					var updates = undefined;
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
						$scope.systemState = systemState.get();
						$scope.rooms = rooms.query();
						$scope.outdoor = outdoor.get();
					};

					$scope.$on('$destroy', function() {
						$scope.stopUpdates();
					});

					$scope.update();
					$scope.autoUpdate();
				} ])

.controller('SystemController', [ '$scope', function($scope) {

} ])

.controller('RoomController', [ '$scope', '$routeParams', 'rooms', function($scope, $routeParams, rooms) {
    $scope.roomDetail = rooms.get({ roomName: $routeParams.roomName }, function(roomDetail) {
    	// after load, avoid having the history in the roomDetail object directly - otherwise save would always send it back to the server, which is unnecessary
        $scope.temperatureHistory = roomDetail.temperatureHistory;
        roomDetail.temperatureHistory = undefined;
    });
    
    $scope.xAxisTickFormat = function() {
    	return function(d){
    		return d3.time.format('%e.%m. %H:%M')(new Date(d));
    	};
    };
} ]);
