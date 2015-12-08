'use strict';

angular.module('21pointsApp')
	.controller('MetricaDeleteController', function($scope, $uibModalInstance, entity, Metrica) {

        $scope.metrica = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Metrica.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
