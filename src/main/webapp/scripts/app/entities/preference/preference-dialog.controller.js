'use strict';

angular.module('21pointsApp').controller('PreferenceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Preference', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Preference, User) {

        $scope.preference = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Preference.get({id : id}, function(result) {
                $scope.preference = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('21pointsApp:preferenceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.preference.id != null) {
                Preference.update($scope.preference, onSaveSuccess, onSaveError);
            } else {
                Preference.save($scope.preference, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
