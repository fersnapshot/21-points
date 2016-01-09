'use strict';

angular.module('21pointsApp').controller('PointController', 
    ['$scope', '$state', 'Point', 'PointSearch', 'ParseLinks', '$translate', 
        function ($scope, $state, Point, PointSearch, ParseLinks, $translate) {

        $scope.points = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Point.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.points.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.points = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            if ($scope.pointSearch) {
                PointSearch.query($scope.pointSearch, function(result) {
                    $scope.points = result;
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
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.point = {
                version: null,
                date: null,
                exercise: null,
                meals: null,
                alcohol: null,
                notes: null,
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
