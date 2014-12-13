'use strict';

/* Directives */

angular.module('homeAutomation.directives', [])

.directive('haAppVersion', [ 'version', function(version) {
	return function(scope, elm, attrs) {
		elm.text(version);
	};
} ])

.directive('haState', [ function() {
	return {
		restrict : 'E',
		scope : {
			state : '=',
			entity : '=',
		},
		templateUrl : 'angular/components/state.html',
		link: function (scope, element, attrs) {
	        scope.entityEnabledChanged = function() {
			    // mimics the same logic from the server - the control gets switched off when disabled...
				if (attrs.shouldSwitchOffWhenDisabled && !scope.state.enabled) {
					scope.state.switchedOn = false;
				}
				scope.entity.$save();
			}
	    }
	};
} ])

.directive('haLightDetails', [ function() {
	return {
		restrict : 'E',
		scope : {
			entity : '=',
		},
		templateUrl : 'angular/components/lightDetails.html'
	};
} ])

.directive('haRoomPanel', [ function() {
	return {
		restrict : 'E',
		scope : {
			room : '=',
		},
		templateUrl : 'angular/components/roomPanel.html',
		link: function (scope) {
	        scope.hasOpenedEntry = function(room) {
	        	for (var i = 0; i < room.entries.length; i++) {
	        		var entry = room.entries[i];
	        		if (entry.reedSwitch && entry.reedSwitch.initiated && entry.reedSwitch.enabled && !entry.reedSwitch.switchedOn) {
	        			// switched on translates to "closed", i.e. the circuit is closed
	        			return true;
	        		}
	        	}
	        	return false;
			};
	    }		
	};
} ])

.directive('haLightControl', [ function() {
	return {
		restrict : 'E',
		scope : {
			entity : '=',
			small : '@'
		},
		templateUrl : 'angular/components/lightControl.html',
		link: function (scope) {
	        scope.lightControlClicked = function() {
				if (scope.entity.lightControl.enabled) { 
					scope.entity.lightControl.switchedOn = !scope.entity.lightControl.switchedOn; 
					scope.entity.$save(); 
				}
			};
	    }
	};
} ])

.directive('haProperty', [ function() {
	return {
		restrict : 'E',
		scope : {
			property : '=',
		},
		templateUrl : 'angular/components/property.html'
	};
} ])

.directive('haSystemPanels', [ function() {
	return {
		restrict : 'E',
		scope : {
			state : '=',
		},
		templateUrl : 'angular/components/systemPanels.html'
	};
} ])

.directive('haGraphsSwitch', [ function() {
	return {
		restrict : 'E',
		scope : {
			data : '='
		},
		templateUrl : 'angular/components/graphsSwitch.html',
		link: function (scope) {
	        scope.switchGraphs = function(days) {
    			// data has all the graphs, so we need to loop through all of them and cut the data
	        	// each element is a series of data, so bidimensional array here...
	        	for (var i = 0; i < scope.data.length; i++) {
	        		var graphSeries = scope.data[i];
	        		for (var j = 0; j < graphSeries.length; j++) {
	        			var graph = graphSeries[j];

	        			// first store the current values (assuming first load gets all the data)
	    	        	if (!graph.originalValues) {
	    	        		graph.originalValues = graph.values.slice(0);
	        			}
	    	        	// now reset to original data (we may have cut the values before)
	    	        	graph.values = graph.originalValues;

	        			if (graph.values.length > 0) {
		        			var beginIndex = scope.findBeginIndex(graph.values, days);
		        			graph.values = graph.values.slice(beginIndex);
	        			}
	        		}
	        	}
	        	
	        	// now refresh all graphs
	            for (var i = 0; i < nv.graphs.length; i++) {
	            	nv.graphs[i].update();
	            }
			};
			
			scope.findBeginIndex = function(values, days) {
				var firstDate = new Date(values[0][0]);
				var lastDate = new Date(values[values.length-1][0]);
				
				// if the diff between the above is pretty much a week, then take the appropriate index...
				var interval = lastDate - firstDate;
				if (Math.round(interval / (24 * 3600 * 1000)) == 7) {
					var result = Math.round( values.length - 1 - ( days * values.length / 7 ) );
					return result >= 0 ? result : 0;
				}
				
				// otherwise just return 0, do not attempt any lucky or smart guess..
				return 0;
			};
	    }
	};
} ]);
