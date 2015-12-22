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
                        return $translate.refresh();
                    }]
                }
            })

            .state('point.add', {
                parent: 'home',
                url: 'add/point',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/point/point-dialog.html',
                        controller: 'PointDialogController',
                        size: 'lg',
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
                    })
                }],
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('point');
                        return $translate.refresh();
                    }]
                }

            })

            .state('preference.add', {
                parent: 'home',
                url: 'my-preferences',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/preference/preference-dialog.html',
                        controller: 'PreferenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Preference', function(Preference) {
                                return Preference.user();
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    })
                }],
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('preference');
                        $translatePartialLoader.addPart('units');
                        return $translate.refresh();
                    }]
                }
            })
        ;
    });
