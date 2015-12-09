'use strict';

angular.module('21pointsApp')
	.controller('PreferenceDeleteController', function($scope, $uibModalInstance, entity, Preference) {

        $scope.preference = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Preference.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
