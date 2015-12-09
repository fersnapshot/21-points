'use strict';

angular.module('21pointsApp')
    .controller('WeightDetailController', function ($scope, $rootScope, $stateParams, entity, Weight, User) {
        $scope.weight = entity;
        $scope.load = function (id) {
            Weight.get({id: id}, function(result) {
                $scope.weight = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:weightUpdate', function(event, result) {
            $scope.weight = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
