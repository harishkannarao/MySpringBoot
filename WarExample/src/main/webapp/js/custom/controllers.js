var springBootAngularWarControllers = angular.module('springBootAngularWarControllers', []);

springBootAngularWarControllers.controller('HomeCtrl', ['$scope', function ($scope) {
    $scope.properties = configDataInJs();
}]);