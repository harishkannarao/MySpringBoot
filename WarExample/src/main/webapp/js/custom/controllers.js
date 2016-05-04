var springBootAngularWarControllers = angular.module('springBootAngularWarControllers', []);

springBootAngularWarControllers.controller('HomeCtrl', ['$scope', 'configData', function ($scope, configData) {
//    $scope.properties = configData;
    $scope.properties = configDataInJs();
}]);