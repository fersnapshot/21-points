'use strict';

angular.module('21pointsApp')
    .controller('MetricaDetailController', function ($scope, $rootScope, $stateParams, entity, Metrica, Entry) {
        $scope.metrica = entity;
        $scope.load = function (id) {
            Metrica.get({id: id}, function(result) {
                $scope.metrica = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:metricaUpdate', function(event, result) {
            $scope.metrica = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
