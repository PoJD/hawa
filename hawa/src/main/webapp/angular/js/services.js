'use strict';

/* Services */

var services = angular.module('homeAutomation.services', [ 'ngResource' ])

.value('version', '%VERSION%')

.factory('systemState', [ '$resource', function($resource) {
	return $resource('rest/systemstate/:type', null, {
        'shutdown': { method:'PUT' }
    });
} ])

.factory('rooms', [ '$resource', function($resource) {
	return $resource('rest/rooms/:detail', null, {
        'switchOffLights': { method:'POST', url: 'rest/rooms/control/switchOffLights' }
    });
} ])

.factory('outdoor', [ '$resource', function($resource) {
	return $resource('rest/outdoor/:type');
} ])

.factory('liveview', [ '$resource', function($resource) {
	return $resource('rest/liveview');
} ])

.factory('security', [ '$resource', function($resource) {
	return $resource('rest/security', null, {
		'dismiss':  { method:'POST', url: 'rest/security/control/dismiss' }
	});
} ]);
