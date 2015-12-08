'use strict';

angular.module('21pointsApp')
    .controller('EntryDetailController', function ($scope, $rootScope, $stateParams, entity, Entry, Goal, Metrica) {
        $scope.entry = entity;
        $scope.load = function (id) {
            Entry.get({id: id}, function(result) {
                $scope.entry = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:entryUpdate', function(event, result) {
            $scope.entry = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
