(function () {
    'use strict';

    angular.module('21pointsApp')
            .controller('BloodPressureDialogController', BloodPressureDialogController);

    BloodPressureDialogController.$inject = ['$scope', '$uibModalInstance', 'entity', 'BloodPressure', 'User'];

    function BloodPressureDialogController($scope, $uibModalInstance, entity, BloodPressure, User) {

        $scope.bloodPressure = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            BloodPressure.get({id : id}, function(result) {
                $scope.bloodPressure = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('21pointsApp:bloodPressureUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.bloodPressure.id != null) {
                BloodPressure.update($scope.bloodPressure, onSaveSuccess, onSaveError);
            } else {
                BloodPressure.save($scope.bloodPressure, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        
        $scope.datePickerForDate = {};
        $scope.datePickerForDate.status = {
            opened: false
        };
        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
        
    }

})();
