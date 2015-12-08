'use strict';

angular.module('21pointsApp').controller('EntryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Entry', 'Goal', 'Metrica',
        function($scope, $stateParams, $uibModalInstance, entity, Entry, Goal, Metrica) {

        $scope.entry = entity;
        $scope.goals = Goal.query();
        $scope.metricas = Metrica.query();
        $scope.load = function(id) {
            Entry.get({id : id}, function(result) {
                $scope.entry = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('21pointsApp:entryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.entry.id != null) {
                Entry.update($scope.entry, onSaveSuccess, onSaveError);
            } else {
                Entry.save($scope.entry, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
