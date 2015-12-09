'use strict';

angular.module('21pointsApp')
    .controller('PreferenceController', function ($scope, $state, Preference, PreferenceSearch) {

        $scope.preferences = [];
        $scope.loadAll = function() {
            Preference.query(function(result) {
               $scope.preferences = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PreferenceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.preferences = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.preference = {
                version: null,
                weekly_goal: null,
                weight_units: null,
                id: null
            };
        };
    });
