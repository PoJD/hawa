'use strict';

/* Services */

var services = angular.module('homeAutomation.services', ['ngResource']);

services.value('version', '%VERSION%');

services.factory('systemState', ['$resource', function($resource) {
	return $resource('rest/systemstate', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);
