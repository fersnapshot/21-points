(function () {
    'use strict';

    angular.module('21pointsApp')
            .controller('CalendarController', CalendarController);

    CalendarController.$inject = ['$scope', '$state', '$compile', '$log', 'uiCalendarConfig', 'Point', 'BloodPressure', 'Weight', '$translate', 'DateUtils'];

    function CalendarController($scope, $state, $compile, $log, uiCalendarConfig, Point, BloodPressure, Weight, $translate, DateUtils) {

        /* event source that calls a function on every view switch */
        $scope.eventSource = function (start, end, timezone, callback) {
            // start and end are for displayed calendar, so see if end is in current month before subtracting a month
            var date = end;
            var endOfMonth = moment(end).endOf('month');
            if (endOfMonth.diff(date, 'days') > 0) {
                date = end.subtract({months: 1});
            }
            date = date.format('YYYY-MM-DD');
            //$log.info("Fetching data for: " + date);
            var events = [];
            Point.byMonth({month: date}, function (data) {
                data.forEach(function (item) {
                    events.push({
                        id: item.id,
                        title: (item.exercise ? 1 : 0) + (item.meals ? 1 : 0) + (item.alcohol ? 1 : 0) + " {{'main.calendar.point.title' | translate}}",
                        tooltip: "{{'main.calendar.point.tooltip' | translate: '{exercise: " + (item.exercise ? 1 : 0) +
                                ", meals: " + (item.meals ? 1 : 0) + ", alcohol: " + (item.alcohol ? 1 : 0) + "}'}}",
                        type: 'point',
                        start: DateUtils.convertDateUTCToLocal(item.date),
                        allDay: true,
                        className: ['label label-success']
                    });
                });

                BloodPressure.byMonth({month: date}, function (data) {
                    data.forEach(function (item) {
                        events.push({
                            id: item.id,
                            title: item.systolic + '/' + item.diastolic,
                            tooltip: "{{'main.calendar.blood.title' | translate}} " + item.systolic + '/' + item.diastolic,
                            type: 'blood',
                            start: DateUtils.convertDateTimeFromServer(item.timestamp),
                            allDay: false,
                            className: ['label label-info']
                        });
                    });

                    Weight.byMonth({month: date}, function (data) {
                        data.forEach(function (item) {
                            events.push({
                                id: item.id,
                                title: item.weight,
                                tooltip: "{{'main.calendar.weight.title' | translate}} " + item.weight,
                                type: 'weight',
                                start: DateUtils.convertDateTimeFromServer(item.timestamp),
                                allDay: false,
                                className: ['label label-primary']
                            });
                        });

                        callback(events);
                    });
                });
            });

        };

        $scope.onEventClick = function (date, jsEvent, view) {
            $state.go('calendar.' + date.type, {id: date.id});
        };

        /* Change View */
        $scope.changeView = function (view, calendar) {
            uiCalendarConfig.calendars[calendar].fullCalendar('changeView', view);
        };

        /* Change View */
        $scope.renderCalender = function (calendar) {
            if (uiCalendarConfig.calendars[calendar]) {
                uiCalendarConfig.calendars[calendar].fullCalendar('render');
            }
        };

        /* Render Tooltip */
        $scope.eventRender = function (event, element, view) {
            var tooltip = (event.tooltip) ? event.tooltip : event.title;
            element.attr({
                'uib-tooltip': tooltip
            });
            $compile(element)($scope);
        };

        /* config object */
        $scope.uiConfig = {
            calendar: {
                editable: false,
                timeFormat: 'HH:mm',
                buttonText: {},
                header: {
                    left: 'month agendaWeek agendaDay',
                    center: 'prev title next',
                    right: 'today'
                },
                eventClick: $scope.onEventClick,
                eventRender: $scope.eventRender
            }
        };

        $scope.eventSources = [$scope.eventSource];

        $translate('main.calendar.firstDay').then(function (data) {
            $scope.uiConfig.calendar.firstDay = data;
        });
        $translate('main.calendar.today').then(function (data) {
            $scope.uiConfig.calendar.buttonText.today = data;
        });
        $translate('main.calendar.month').then(function (data) {
            $scope.uiConfig.calendar.buttonText.month = data;
        });
        $translate('main.calendar.week').then(function (data) {
            $scope.uiConfig.calendar.buttonText.week = data;
        });
        $translate('main.calendar.day').then(function (data) {
            $scope.uiConfig.calendar.buttonText.day = data;
        });

    }

})();
