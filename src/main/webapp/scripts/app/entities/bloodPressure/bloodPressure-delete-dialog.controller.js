'use strict';

angular.module('21pointsApp')
	.controller('BloodPressureDeleteController', function($scope, $uibModalInstance, id, BloodPressure) {

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function () {
            BloodPressure.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
