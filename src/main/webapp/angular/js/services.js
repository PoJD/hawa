'use strict';

/* Services */

var services = angular.module('homeAutomation.services', ['ngResource']);

services.value('version', '0.1');

services.factory('systemState', ['$resource', function($resource) {
	return $resource('rest/systemstate', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);
