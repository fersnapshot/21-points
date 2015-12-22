'use strict';

angular.module('21pointsApp')
    .controller('MainController', function ($scope, Principal, Point, Preference) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        Point.thisWeek(function(data) {
            $scope.pointsThisWeek = data;
            $scope.pointsPercentage = data.points * 100 / 21;
        });
        Preference.user(function(data) {
            $scope.preferences = data;
        });
    });
