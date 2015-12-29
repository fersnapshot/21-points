(function () {
    'use strict';

    angular
            .module('21pointsApp')
            .factory('Chart', Chart);

    Chart.$inject = ['$filter'];
    function Chart($filter) {
        var today = new Date();
        var priorDate = new Date().setDate(today.getDate() - 30);
        var bpChartConfig = {
            chart: {
                type: "lineChart",
                height: 200,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 50,
                    left: 55
                },
                x: function (d) {
                    return d.x;
                },
                y: function (d) {
                    return d.y;
                },
                useInteractiveGuideline: true,
                dispatch: {},
                xAxis: {
                    axisLabel: "",
                    showMaxMin: false,
                    tickFormat: function (d) {
                        //return d3.time.format("%b %d")(new Date(d));
                        return $filter('date')(new Date(d),'MMM d');
                    }
                },
                xDomain: [priorDate, today],
                yAxis: {
                    axisLabel: "",
                    axisLabelDistance: -10
                },
                transitionDuration: 250
            },
            title: {
                enable: true
            }
        };
        return {
            getBpChartConfig: function () {
                return bpChartConfig;
            }
        };
    }

})();
