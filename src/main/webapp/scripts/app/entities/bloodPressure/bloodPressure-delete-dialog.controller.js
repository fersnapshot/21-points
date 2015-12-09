'use strict';

angular.module('21pointsApp')
	.controller('BloodPressureDeleteController', function($scope, $uibModalInstance, entity, BloodPressure) {

        $scope.bloodPressure = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BloodPressure.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
