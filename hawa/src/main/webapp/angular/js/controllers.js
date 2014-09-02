'use strict';

/* Controllers */

angular.module('homeAutomation.controllers', [])

.controller(
		'BaseUpdateController',
		[ '$scope', '$interval', function($scope, $interval) {
				var updates = undefined;
				$scope.autoUpdate = function() {
					if (angular.isDefined(updates))
						return;

					updates = $interval(function() {
						if (angular.isDefined($scope.update)) {
							$scope.update();
						}
					}, 5 * 60 * 1000);
				};

				$scope.stopUpdates = function() {
					if (angular.isDefined(updates)) {
						$interval.cancel(updates);
						updates = undefined;
					}
				};
				$scope.$on('$destroy', function() {
					$scope.stopUpdates();
				});
				$scope.autoUpdate();
			} ])

.controller(
		'HomeController',
		[ '$scope', '$controller', 'systemState', 'rooms', 'outdoor', function($scope, $controller, systemState, rooms, outdoor) {
				$controller('BaseUpdateController', {$scope: $scope}); // inherit from BaseUpdateController

				$scope.update = function() {
					$scope.systemState = systemState.get();
					$scope.rooms = rooms.query();
					$scope.outdoor = outdoor.get();
				};

				$scope.update();
			} ])

.controller('LiveViewController', [ '$scope', 'liveview', function($scope, liveview) {
	$scope.liveview = liveview.get(function(result) {
		if (result.cameraOK) { // only in this case change dynamically the img element to start fetching data...
			$scope.source = "http://rpi:7070/?action=stream";
		}
	});
} ])

.controller('RoomController', 
		[ '$scope', '$controller', '$routeParams', 'rooms', function($scope, $controller, $routeParams, rooms) {
				$controller('BaseUpdateController', {$scope: $scope}); // inherit from BaseUpdateController
				
				$scope.update = function() {
				    $scope.roomDetail = rooms.get({ roomName: $routeParams.roomName }, function(roomDetail) {
				    	// after load, avoid having the history in the roomDetail object directly - otherwise save would always send it back to the server, which is unnecessary
				        $scope.temperatureHistory = roomDetail.temperatureHistory;
				        roomDetail.temperatureHistory = undefined;
				    });
				};

				$scope.update();
			    
			    $scope.xAxisTickFormat = function() {
			    	return function(d){
			    		return d3.time.format('%e.%m. %H:%M')(new Date(d));
			    	};
			    };
} ]);
