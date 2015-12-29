'use strict';

angular.module('21pointsApp')
	.controller('WeightDeleteController', function($scope, $uibModalInstance, id, Weight) {

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function () {
            Weight.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
