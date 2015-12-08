'use strict';

angular.module('21pointsApp')
    .controller('GoalDetailController', function ($scope, $rootScope, $stateParams, entity, Goal, User, Entry) {
        $scope.goal = entity;
        $scope.load = function (id) {
            Goal.get({id: id}, function(result) {
                $scope.goal = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:goalUpdate', function(event, result) {
            $scope.goal = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
