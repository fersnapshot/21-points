(function () {
    'use strict';

    angular.module('21pointsApp')
            .controller('BloodPressureDeleteController', BloodPressureDeleteController);

    BloodPressureDeleteController.$inject = ['$scope', '$uibModalInstance', 'id', 'BloodPressure'];

    function BloodPressureDeleteController($scope, $uibModalInstance, id, BloodPressure) {

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function () {
            BloodPressure.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    }

})();
