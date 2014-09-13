'use strict';

/* Services */

var services = angular.module('homeAutomation.services', [ 'ngResource' ])

.value('version', '%VERSION%')

.factory('systemState', [ '$resource', function($resource) {
	return $resource('rest/systemstate');
} ])

.factory('rooms', [ '$resource', function($resource) {
	return $resource('rest/rooms/:roomName');
} ])

.factory('outdoor', [ '$resource', function($resource) {
	return $resource('rest/outdoor/:type');
} ])

.factory('liveview', [ '$resource', function($resource) {
	return $resource('rest/liveview');
} ]);
