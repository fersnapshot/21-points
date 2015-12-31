'use strict';

angular.module('21pointsApp')
        .controller('MainController', function ($scope, Principal, Point, Preference, BloodPressure, Weight, LineChart, $translate) {
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

            BloodPressure.lastDays(function (bpReadings) {
                $scope.existeBpReadings = bpReadings.readings.length;
                if ($scope.existeBpReadings) {
                    $scope.bpOptions = angular.copy(LineChart.getLineChartConfig());
                    //$scope.bpOptions.title.text = bpReadings.period;
                    //$scope.bpOptions.chart.yAxis.axisLabel = "Blood Pressure";
                    traduccionesBP(bpReadings.period);
                    var today = new Date();
                    var priorDate = new Date();
                    priorDate.setDate(today.getDate() - bpReadings.period);
                    priorDate.setHours(0,0,0,0);
                    $scope.bpOptions.chart.xDomain = [priorDate, today];
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
            
            function traduccionesBP(days) {
                $translate('main.blood.title', {days: days}).then(function (title) {
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

            Weight.lastDays(function (wReadings) {
                $scope.existeWReadings = wReadings.readings.length;
                if ($scope.existeWReadings) {
                    $scope.wOptions = angular.copy(LineChart.getLineChartConfig());
                    traduccionesW(wReadings.period);
                    var today = new Date();
                    var priorDate = new Date().setDate(today.getDate() - wReadings.period);
                    $scope.wOptions.chart.xDomain = [priorDate, today];
                    var weights = [];
                    wReadings.readings.forEach(function (item) {
                        var fecha = new Date(item.timestamp);
                        weights.push({
                            x: fecha,
                            y: item.weight
                        });
                    });
                    $scope.wData = [{
                            values: weights,
                            key: 'Weights',
                            color: '#673ab7'
                        }];
                }
            });
            
            function traduccionesW(days) {
                $translate('main.weight.title', {days: days}).then(function (title) {
                    $scope.wOptions.title.text = title;
                });   
                $translate('main.weight.xAxis').then(function (xAxis) {
                    $scope.wOptions.chart.xAxis.axisLabel = xAxis;
                });   
                $translate('main.weight.yAxis', {units: $scope.preferences.weightUnits}).then(function (yAxis) {
                    $scope.wOptions.chart.yAxis.axisLabel = yAxis;
                });   
                $translate('main.weight.weight').then(function (weight) {
                    $scope.wData[0].key = weight;
                });   
            }
            
            $translate.refresh();

        });
