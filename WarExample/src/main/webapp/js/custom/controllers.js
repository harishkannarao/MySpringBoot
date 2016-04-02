var springBootAngularWarControllers = angular.module('springBootAngularWarControllers', []);

springBootAngularWarControllers.controller('HomeCtrl', ['$scope', '$http', function ($scope, $http) {
    var propertiesRequest = {
        method: 'GET',
        url: '/properties',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        data: {}
    }
    $http(propertiesRequest).success(function(data) {
          $scope.properties = data;
    });
}]);