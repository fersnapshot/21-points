'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider

            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        $translatePartialLoader.addPart('point');
                        $translatePartialLoader.addPart('bloodPressure');
                        $translatePartialLoader.addPart('weight');
                        $translatePartialLoader.addPart('preference');
                        $translatePartialLoader.addPart('units');
                        return $translate.refresh();
                    }]
                }
            })

            .state('point.add', {
                parent: 'home',
                url: 'add/point',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/point/point-dialog.html',
                        controller: 'PointDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {     // valores por defecto para nueva entidad
                                return {
                                    version: null,
                                    date: new Date(),
                                    exercise: true,
                                    meals: true,
                                    alcohol: true,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    });
                }]
            })

            .state('blood-add', {
                parent: 'home',
                url: 'add/blood',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bloodPressure/bloodPressure-dialog.html',
                        controller: 'BloodPressureDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                var now = new Date();
                                return {
                                    version: null,
                                    timestamp: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes()),
                                    systolic: null,
                                    diastolic: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    });
                }]
            })
            
            .state('weight.add', {
                parent: 'home',
                url: 'add/weight',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/weight/weight-dialog.html',
                        controller: 'WeightDialogController',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                var now = new Date();
                                return {
                                    version: null,
                                    timestamp: new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes()),
                                    weight: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    });
                }]
            })

            .state('preference.add', {
                parent: 'home',
                url: 'my-preferences',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/preference/preference-dialog.html',
                        controller: 'PreferenceDialogController',
                        size: 'sm',
                        resolve: {
                            entity: ['Preference', function(Preference) {
                                return Preference.user();
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    });
                }]
            })
            
        ;
    });
