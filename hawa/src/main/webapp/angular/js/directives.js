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
		link: function link(scope, element, attrs) {
	        scope.entityEnabledChanged = function() {
			    // mimics the same logic from the server - the control gets switched off when disabled...
				if (attrs.shouldSwitchOffWhenDisabled && !scope.state.enabled) {
					scope.state.on = false;
				}
				scope.entity.$save();
			}
	    }
	};
} ])

.directive('haRoomPanel', [ function() {
	return {
		restrict : 'E',
		scope : {
			room : '=',
		},
		templateUrl : 'angular/components/roomPanel.html'
	};
} ])

.directive('haLightControl', [ function() {
	return {
		restrict : 'E',
		scope : {
			entity : '=',
			'class' : '@'
		},
		templateUrl : 'angular/components/lightControl.html',
		link: function link(scope) {
	        scope.lightControlClicked = function() {
				if (scope.entity.lightControl.enabled) { 
					scope.entity.lightControl.on = !scope.entity.lightControl.on; 
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
} ]);
