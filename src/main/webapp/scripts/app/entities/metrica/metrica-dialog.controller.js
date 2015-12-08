'use strict';

angular.module('21pointsApp').controller('MetricaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Metrica', 'Entry',
        function($scope, $stateParams, $uibModalInstance, entity, Metrica, Entry) {

        $scope.metrica = entity;
        $scope.entrys = Entry.query();
        $scope.load = function(id) {
            Metrica.get({id : id}, function(result) {
                $scope.metrica = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('21pointsApp:metricaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.metrica.id != null) {
                Metrica.update($scope.metrica, onSaveSuccess, onSaveError);
            } else {
                Metrica.save($scope.metrica, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
