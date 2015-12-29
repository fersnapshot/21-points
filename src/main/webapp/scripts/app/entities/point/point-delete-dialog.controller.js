'use strict';

angular.module('21pointsApp')
	.controller('PointDeleteController', function($scope, $uibModalInstance, id, Point) {

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function () {
            Point.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
