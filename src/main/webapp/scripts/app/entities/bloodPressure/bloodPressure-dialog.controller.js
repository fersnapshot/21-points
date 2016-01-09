'use strict';

angular.module('21pointsApp').controller('BloodPressureDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BloodPressure', 'User', '$translate',
        function($scope, $stateParams, $uibModalInstance, entity, BloodPressure, User, $translate) {

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
        $translate('datePicker.startingDay').then(function (startingDay) {
            $scope.datePickerForDate.startingDay = startingDay;
        });
}]);
