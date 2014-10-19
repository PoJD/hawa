'use strict';


// Declare app level module which depends on filters, and services
angular.module('homeAutomation', [
  'ngRoute',
  'angular-gauge',
  'toggle-switch',
  'nvd3ChartDirectives',
  'homeAutomation.filters',
  'homeAutomation.services',
  'homeAutomation.directives',
  'homeAutomation.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider
  	.when('/home', {templateUrl: 'angular/views/home.html', controller: 'HomeController'})
    .when('/liveview', {templateUrl: 'angular/views/liveview.html', controller: 'LiveViewController'})
    .when('/outdoor', {templateUrl: 'angular/views/outdoor.html', controller: 'OutdoorController'})
    .when('/systemstate', {templateUrl: 'angular/views/systemstate.html', controller: 'SystemStateController'})
    .when('/rooms/:roomName', {templateUrl: 'angular/views/room.html', controller: 'RoomController'})
    .otherwise({redirectTo: '/home'});
}]);
