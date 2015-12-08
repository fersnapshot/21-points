'use strict';

angular.module('21pointsApp').controller('GoalDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Goal', 'User', 'Entry',
        function($scope, $stateParams, $uibModalInstance, entity, Goal, User, Entry) {

        $scope.goal = entity;
        $scope.users = User.query();
        $scope.entrys = Entry.query();
        $scope.load = function(id) {
            Goal.get({id : id}, function(result) {
                $scope.goal = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('21pointsApp:goalUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.goal.id != null) {
                Goal.update($scope.goal, onSaveSuccess, onSaveError);
            } else {
                Goal.save($scope.goal, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
