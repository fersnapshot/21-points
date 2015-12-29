'use strict';

angular.module('21pointsApp')
	.controller('PreferenceDeleteController', function($scope, $uibModalInstance, id, Preference) {

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function () {
            Preference.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
