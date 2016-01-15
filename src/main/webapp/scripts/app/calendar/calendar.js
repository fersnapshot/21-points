(function () {
    'use strict';

    angular.module('21pointsApp')
            .config(CalendarConfig);

    CalendarConfig.$inject = ['$stateProvider'];

    function CalendarConfig($stateProvider) {
        $stateProvider
                .state('calendar', {
                    parent: 'home',
                    url: 'calendar',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    views: {
                        'content@': {
                            template: '<div ui-calendar="uiConfig.calendar" class="calendar" ng-model="eventSources"></div>',
                            controller: 'CalendarController'
                        }
                    }
                })

                .state('calendar.point', {
                    parent: 'calendar',
                    url: '/point/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'scripts/app/entities/point/point-dialog.html',
                                controller: 'PointDialogController',
                                size: 'md',
                                resolve: {
                                    entity: ['Point', function (Point) {
                                            return Point.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function (result) {
                                $state.go('calendar', null, {reload: true});
                            }, function () {
                                $state.go('calendar');
                            });
                        }]
                })


                .state('calendar.blood', {
                    parent: 'calendar',
                    url: '/blood/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'scripts/app/entities/bloodPressure/bloodPressure-dialog.html',
                                controller: 'BloodPressureDialogController',
                                size: 'md',
                                resolve: {
                                    entity: ['BloodPressure', function (BloodPressure) {
                                            return BloodPressure.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function (result) {
                                $state.go('calendar', null, {reload: true});
                            }, function () {
                                $state.go('calendar');
                            });
                        }]
                })

                .state('calendar.weight', {
                    parent: 'calendar',
                    url: '/weight/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                            $uibModal.open({
                                templateUrl: 'scripts/app/entities/weight/weight-dialog.html',
                                controller: 'WeightDialogController',
                                size: 'md',
                                resolve: {
                                    entity: ['Weight', function (Weight) {
                                            return Weight.get({id: $stateParams.id});
                                        }]
                                }
                            }).result.then(function (result) {
                                $state.go('calendar', null, {reload: true});
                            }, function () {
                                $state.go('calendar');
                            });
                        }]
                })

                ;
    }

})();
