'use strict';

/* Services */

var services = angular.module('homeAutomation.services', ['ngResource']);

services.value('version', '0.1');

services.factory('vmState', ['$resource', function($resource) {
	return $resource('rest/vmstate', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);
