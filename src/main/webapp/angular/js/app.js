'use strict';


// Declare app level module which depends on filters, and services
angular.module('homeAutomation', [
  'ngRoute',
  'homeAutomation.filters',
  'homeAutomation.services',
  'homeAutomation.directives',
  'homeAutomation.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {templateUrl: 'angular/partials/home.html', controller: 'HomeController'});
  $routeProvider.when('/system', {templateUrl: 'angular/partials/system.html', controller: 'SystemController'});
  $routeProvider.otherwise({redirectTo: '/home'});
}]);
