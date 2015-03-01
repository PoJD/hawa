'use strict';

var app = angular.module('homeAutomation', [
  'ngRoute',
  'angular-gauge',
  'toggle-switch',
  'nvd3ChartDirectives',
  'ui.calendar',
  'ngDialog',
  'tmh.dynamicLocale',
  'pascalprecht.translate',
  'ngCookies',
  'homeAutomation.filters',
  'homeAutomation.services',
  'homeAutomation.directives',
  'homeAutomation.controllers'
]).
config( 
 function($routeProvider, $translateProvider, tmhDynamicLocaleProvider) {
  $routeProvider
  	.when('/home', {templateUrl: 'angular/views/home.html', controller: 'HomeController'})
    .when('/outdoor', {templateUrl: 'angular/views/outdoor.html', controller: 'OutdoorController'})
    .when('/security', {templateUrl: 'angular/views/security.html', controller: 'SecurityController'})
    .when('/liveview', {templateUrl: 'angular/views/liveview.html', controller: 'LiveViewController'})
    .when('/systemstate', {templateUrl: 'angular/views/systemstate.html', controller: 'SystemStateController'})
    .when('/rooms/:roomName', {templateUrl: 'angular/views/room.html', controller: 'RoomController'})
    .otherwise({redirectTo: '/home'});
    
  $translateProvider.preferredLanguage('en');
  $translateProvider.useCookieStorage();
  
  tmhDynamicLocaleProvider.localeLocationPattern('angular/builtin/js/i18n/angular-locale_{{locale}}.js');
 }
);

// workaround for NVD3 perf issues (also resulted in leaking graphs and errors in js console)
// see https://github.com/angularjs-nvd3-directives/angularjs-nvd3-directives/issues/193
app.run(function($rootScope, $templateCache) {
  //try to clear unused objects to avoid huge memory usage
  $rootScope.$on('$routeChangeStart', function(event, next, current) {
    if (typeof(current) !== 'undefined') {
      //destroy all d3 svg graph
      angular.element(document.body.querySelectorAll('.nvd3')).remove();
      d3.selectAll('svg').remove();
      nv.charts = {};
      nv.graphs = [];
      nv.logs = {};
    }
  });
});
