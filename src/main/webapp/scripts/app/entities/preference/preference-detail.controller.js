'use strict';

angular.module('21pointsApp')
    .controller('PreferenceDetailController', function ($scope, $rootScope, $stateParams, entity, Preference) {
        $scope.preference = entity;
        $scope.load = function (id) {
            Preference.get({id: id}, function(result) {
                $scope.preference = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:preferenceUpdate', function(event, result) {
            $scope.preference = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
