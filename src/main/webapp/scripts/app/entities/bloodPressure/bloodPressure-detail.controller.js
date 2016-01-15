(function () {
    'use strict';

    angular.module('21pointsApp')
            .controller('BloodPressureDetailController', BloodPressureDetailController);

    BloodPressureDetailController.$inject = ['$scope', '$rootScope', 'entity', 'BloodPressure'];

    function BloodPressureDetailController($scope, $rootScope, entity, BloodPressure) {

        $scope.bloodPressure = entity;
        $scope.load = function (id) {
            BloodPressure.get({id: id}, function(result) {
                $scope.bloodPressure = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:bloodPressureUpdate', function(event, result) {
            $scope.bloodPressure = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }

})();
