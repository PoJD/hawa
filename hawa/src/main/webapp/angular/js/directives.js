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
			entity : '='
		},
		templateUrl : 'angular/components/state.html'
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
		templateUrl : 'angular/components/lightControl.html'
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
