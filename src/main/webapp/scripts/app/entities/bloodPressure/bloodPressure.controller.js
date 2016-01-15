(function () {
    'use strict';

    angular.module('21pointsApp')
            .controller('BloodPressureController', BloodPressureController);

    BloodPressureController.$inject = ['$scope', 'BloodPressure', 'BloodPressureSearch', 'ParseLinks'];

    function BloodPressureController($scope, BloodPressure, BloodPressureSearch, ParseLinks) {

        $scope.bloodPressures = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function () {
            BloodPressure.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.bloodPressures.push(result[i]);
                }
            });
        };
        $scope.reset = function () {
            $scope.page = 0;
            $scope.bloodPressures = [];
            $scope.loadAll();
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            if ($scope.bloodPressureSearch) {
                BloodPressureSearch.query($scope.bloodPressureSearch, function (result) {
                    $scope.bloodPressures = result;
                }, function (response) {
                    if (response.status === 404) {
                        $scope.loadAll();
                    }
                });
            } else {
                $scope.reset();
            }
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.bloodPressure = {
                version: null,
                timestamp: null,
                systolic: null,
                diastolic: null,
                id: null
            };
        };

        $scope.datePickerForDate = {};
        $scope.datePickerForDate.status = {
            opened: false
        };
        $scope.datePickerForDateOpen = function ($event) {
            $scope.datePickerForDate.status.opened = true;
        };

    }

})();
