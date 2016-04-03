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

springBootAngularWarApp.provider('configData', function () {
   var options = {};
   this.setConfig = function (opt) {
       angular.extend(options, opt);
   };
   this.$get = [function () {
       if (!options) {
           throw new Error('Config options must be configured');
       }
       return angular.copy(options);
   }];
});

angular.element(document).ready(function () {

    fetchData().then(bootstrapApplication);

    function fetchData() {
        var initInjector = angular.injector(["ng"]);
        var $http = initInjector.get("$http");

        var propertiesRequest = {
            method: 'GET',
            url: '/properties',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            data: {}
        }
        return $http(propertiesRequest).success(function(data) {
              angular.module('springBootAngularWarApp').config(['configDataProvider', function (configDataProvider) {
                configDataProvider.setConfig(data);
              }]);
        });
    }

    function bootstrapApplication() {
        angular.bootstrap(document, ['springBootAngularWarApp']);
    }

});


