(function () {
'use strict';

angular.module('21pointsApp').controller('WeightController', 
    ['$scope', '$state', 'Weight', 'WeightSearch', 'ParseLinks', '$translate',
        function ($scope, $state, Weight, WeightSearch, ParseLinks, $translate) {

        $scope.weights = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Weight.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.weights = result;
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.weights = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            if ($scope.weightSearch) {
                WeightSearch.query($scope.weightSearch, function(result) {
                    $scope.weights = result;
                }, function(response) {
                    if(response.status === 404) {
                        $scope.loadAll();
                    }
                });
            } else {
                $scope.reset();
            }
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.weight = {
                version: null,
                timestamp: null,
                weight: null,
                id: null
            };
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

})();
