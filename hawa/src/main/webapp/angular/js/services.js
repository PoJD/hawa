'use strict';

/* Services */

var services = angular.module('homeAutomation.services', [ 'ngResource' ]);

services.value('version', '%VERSION%');

services.factory('systemState', [ '$resource', function($resource) {
	return $resource('rest/systemstate');
} ]);

services.factory('rooms', [ '$resource', function($resource) {
	return $resource('rest/rooms/:roomName');
} ]);

services.factory('outdoor', [ '$resource', function($resource) {
	return $resource('rest/outdoor');
} ]);
