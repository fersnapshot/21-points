'use strict';

angular.module('21pointsApp')
    .controller('GoalController', function ($scope, $state, Goal, GoalSearch) {

        $scope.goals = [];
        $scope.loadAll = function() {
            Goal.query(function(result) {
               $scope.goals = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            GoalSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.goals = result;
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
            $scope.goal = {
                version: null,
                name: null,
                description: null,
                id: null
            };
        };
    });
