'use strict';

/* App Module */
var springBootAngularWarApp = angular.module('springBootAngularWarApp', [
  'ngRoute',
  'springBootAngularWarControllers'
]);

springBootAngularWarApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
      when('/home', {
        templateUrl: 'partials/home-page.html',
        controller: 'HomeCtrl'
      }).
      otherwise({
        redirectTo: '/home'
      });
}]);