'use strict';

angular.module('21pointsApp')
	.controller('PointDeleteController', function($scope, $uibModalInstance, entity, Point) {

        $scope.point = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Point.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
