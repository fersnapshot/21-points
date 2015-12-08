'use strict';

angular.module('21pointsApp')
	.controller('GoalDeleteController', function($scope, $uibModalInstance, entity, Goal) {

        $scope.goal = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Goal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
