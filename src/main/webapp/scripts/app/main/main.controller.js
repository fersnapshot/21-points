'use strict';

angular.module('21pointsApp')
        .controller('MainController', function ($scope, Principal, Point, Preference, BloodPressure, Chart, $translate) {
            Principal.identity().then(function (account) {
                $scope.account = account;
                $scope.isAuthenticated = Principal.isAuthenticated;
            });
            Point.thisWeek(function (data) {
                $scope.pointsThisWeek = data;
                $scope.pointsPercentage = data.points * 100 / 21;
            });
            Preference.user(function (data) {
                $scope.preferences = data;
            });

            BloodPressure.lastDays({days: 30}, function (bpReadings) {
                $scope.existeBpReadings = bpReadings.readings.length;
                if ($scope.existeBpReadings) {
                    $scope.bpOptions = angular.copy(Chart.getBpChartConfig());
                    //$scope.bpOptions.title.text = bpReadings.period;
                    //$scope.bpOptions.chart.yAxis.axisLabel = "Blood Pressure";
                    traducciones();
                    var systolics, diastolics;
                    systolics = [];
                    diastolics = [];
                    bpReadings.readings.forEach(function (item) {
                        var fecha = new Date(item.timestamp);
                        systolics.push({
                            x: fecha,
                            y: item.systolic
                        });
                        diastolics.push({
                            x: fecha,
                            y: item.diastolic
                        });
                    });
                    $scope.bpData = [{
                            values: systolics,
                            key: 'Systolic',
                            color: '#673ab7'
                        }, {
                            values: diastolics,
                            key: 'Diastolic',
                            color: '#03a9f4'
                        }];
                }

            });
            
            function traducciones() {
                $translate('main.blood.title').then(function (title) {
                    $scope.bpOptions.title.text = title;
                });   
                $translate('main.blood.xAxis').then(function (xAxis) {
                    $scope.bpOptions.chart.xAxis.axisLabel = xAxis;
                });   
                $translate('main.blood.yAxis').then(function (yAxis) {
                    $scope.bpOptions.chart.yAxis.axisLabel = yAxis;
                });   
                $translate('main.blood.systolic').then(function (systolic) {
                    $scope.bpData[0].key = systolic;
                });   
                $translate('main.blood.diastolic').then(function (diastolic) {
                    $scope.bpData[1].key = diastolic;
                });   
            }

        });
