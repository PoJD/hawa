'use strict';

/* Controllers */
angular.module('homeAutomation.controllers', [])

.controller('MenuController', function($scope, $translate, tmhDynamicLocale) {
	$scope.changeLanguage = function (key) {
		$translate.use(key);
		tmhDynamicLocale.set(key);
	};
	// make sure the default loaded in translate (from cookies) is used in the dynamic locale too
	$scope.changeLanguage($translate.use());	
})

.controller('BaseController', function($scope, $interval, $translate, $filter, $timeout, security, ngDialog) {	
				var updates = undefined;
				$scope.autoUpdate = function() {
					if (angular.isDefined(updates))
						return;

					updates = $interval(function() {
						if (angular.isDefined($scope.update)) {
							$scope.update();
						}
						$scope.checkSecurity();
					}, 1 * 60 * 1000);
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
				
			    $scope.xAxisTickFormat = function() {
			    	return function(date){
			    		return $filter('date')(date, 'short');
			    	};
			    };
			    
			    $scope.parseCalendarDate = function (date) {
			    	// we have date in ISO-8601 format, so use the constructor to recreate the date object and then let the filter parse it
			    	return $filter('date')(new Date(date), 'short');
			    };
				
				$scope.checkSecurity = function() {
				    $scope.securityStatus = security.get({}, function(securityStatus) {
				    	if (securityStatus.currentBreach) {
				    		$scope.calendarEvent = securityStatus.currentBreach;
					    	ngDialog.open({ template: 'calendarEvent.html', className: 'ngdialog-theme-default', scope: $scope, data: { alarm: true } });				    		
				    	} else {
				    		ngDialog.closeAll(); // just in case some dialog is open from before and some other user dimissed the alarm in the meantime
				    	}
				    } );
				};
				
				$scope.dismiss = function() {
					security.dismiss();
					ngDialog.closeAll();
				};

				// register for changes in locale - register with a 1sec delay to be sure (scripts may not always be ready yet)
			    $scope.$watch(
			    		function($scope) { return $translate.use(); }, 
			    		function(value) { if ($scope.localeChanged) $timeout(function() { $scope.localeChanged(value); }, 200); }
			    		);
			    
	    		$scope.updateGraphs = function() {
		            for (var i = 0; i < nv.graphs.length; i++) {
		            	nv.graphs[i].update();
		            }
	    		}
	    		
		        $scope.translateGraph = function(graphData) {
		        	if (!graphData.origKey) { // store the original key
		        		graphData.origKey = graphData.key;
		        	}
		        	graphData.key = $translate.instant(graphData.origKey);
		        }
			    
				$scope.checkSecurity();
				$scope.autoUpdate();
			} )

.controller('HomeController', function($scope, $controller, systemState, rooms, outdoor) {
				$controller('BaseController', {$scope: $scope}); // inherit from BaseController

				$scope.update = function() {
					$scope.systemState = systemState.get();
					$scope.rooms = rooms.query();
					$scope.outdoor = outdoor.get();
				};
				
				$scope.switchOffLights = function() {
					rooms.switchOffLights();
		        	for (var i = 0; i < $scope.rooms.length; i++) {
		        		var room = $scope.rooms[i];
		        		if (room.lightControl) {
		        			room.lightControl.switchedOn = false;
		        		}
		        	}
				};

				$scope.update();
			} )

.controller('LiveViewController', function($scope, $controller, liveview) {
				$controller('BaseController', {$scope: $scope}); // inherit from BaseController
				
				$scope.update = function(isFirstUpdate) {
					$scope.liveview = liveview.get(function(result) {
						// dynamically change the img element source to start fetching data only if camera is OK and not done before
						if (result.cameraOK && isFirstUpdate) {
							$scope.source = "http://rpi:7070/?action=stream";
						}
					});
				};

				$scope.update(true);				
			} )

.controller('OutdoorController', function($scope, $controller, $translate, outdoor) {
				$controller('BaseController', {$scope: $scope}); // inherit from BaseController

				$scope.translateGraphs = function() {
					$scope.translateGraph($scope.temperatureHistory[0]);
					$scope.translateGraph($scope.humidityHistory[0]);
					$scope.translateGraph($scope.pressureHistory[0]);
				}

				$scope.localeChanged = function() {
					if ($scope.temperatureHistory) {
						$scope.translateGraphs();
						$scope.updateGraphs();
					}
				}

				$scope.update = function() {
				    $scope.outdoorDetail = outdoor.get({ type: 'withHistory' }, function(outdoorDetail) {
				    	// after load, avoid having the history in the outdoorDetail object directly - otherwise save would always send it back to the server, which is unnecessary
				        $scope.temperatureHistory = [ outdoorDetail.outdoorHistory[0] ];
				        $scope.humidityHistory = [ outdoorDetail.outdoorHistory[1] ];
				        $scope.pressureHistory = [ outdoorDetail.outdoorHistory[2] ];
				        
				        $scope.temperatureHistory[0].color = '#E64B53';
				        $scope.humidityHistory[0].color = '#4B81E6';
				        $scope.pressureHistory[0].color = '#4BE68C';
				        
				        $scope.translateGraphs();
				        
				        outdoorDetail.outdoorHistory = undefined;
				    });
				};
				
				$scope.update();
			} )
			
.controller('SystemStateController', function($scope, $controller, systemState) {
				$controller('BaseController', {$scope: $scope}); // inherit from BaseController

				$scope.update = function() {
				    $scope.systemState = systemState.get({ type: 'withDetails' }, function(systemState) {
				    	// after load, avoid having the log in the systemState object directly - otherwise save would always send it back to the server, which is unnecessary
				        $scope.logSystem = systemState.logSystem.join("\n"); //system log does not have new lines wherease below app log does...
				        $scope.logApplication = systemState.logApplication.join("");
				        
				        systemState.logSystem = undefined;
				        systemState.logApplication = undefined;
				    });
				};
				
				$scope.shutdown = function() {
					systemState.shutdown();
				};

				$scope.update();
			} )
						
.controller('RoomController', function($scope, $controller, $routeParams, $translate, rooms) {
				$controller('BaseController', {$scope: $scope}); // inherit from BaseController
				
				$scope.update = function() {
				    $scope.roomDetail = rooms.get({ detail: $routeParams.roomName }, function(roomDetail) {
				    	// after load, avoid having the history in the roomDetail object directly - otherwise save would always send it back to the server, which is unnecessary
				        $scope.temperatureHistory = roomDetail.temperatureHistory;
				        $scope.temperatureHistory[0].color = '#E64B53';
				        $scope.translateGraph($scope.temperatureHistory[0]);
				        roomDetail.temperatureHistory = undefined;
				    });
				};

				$scope.localeChanged = function() {
					if ($scope.temperatureHistory) {
						$scope.translateGraph($scope.temperatureHistory[0]);
						$scope.updateGraphs();
					}
				}
				
				$scope.update();
} )

.controller('SecurityController', function($scope, $controller, $translate, security, ngDialog) {
				$controller('BaseController', {$scope: $scope}); // inherit from BaseController
								
				$scope.update = function(isFirstUpdate) {
				    $scope.securityStatus = security.get(); 
					if(!isFirstUpdate) { // first update is redundant since the calendar itself already fetched the data
						$scope.eventsCalendar.fullCalendar('refetchEvents');
					}
					$scope.changed = false;
				};

				$scope.apply = function() {
					$scope.securityStatus.$save();
					$scope.changed = false;
				};
			    
			    $scope.showEventDetail = function(calendarEvent) {
    	        	$scope.calendarEvent = calendarEvent;
    	        	ngDialog.open({ template: 'calendarEvent.html', className: 'ngdialog-theme-default', scope: $scope });
			    };

			    $scope.calendar = {
			    	config: {
		    	        height: 600,
		    	        header:{
		    	          left: 'agendaWeek agendaDay',
		    	          center: 'title',
		    	          right: 'today prev, next'
		    	        },
		    	        timezone : 'local',
		    	        firstDay: 1,
		    	        lang: $translate.use(),
		    	        defaultView : 'agendaWeek',
		    	        eventClick: $scope.showEventDetail
			    	},
				    events: [ { url: 'rest/security/events' } ]
		    	};
			    
	    		$scope.localeChanged = function(locale) {
	    			$scope.calendar.config.lang = locale;	
	    		}

			    $scope.changed = false;
				$scope.update(true);
} );
