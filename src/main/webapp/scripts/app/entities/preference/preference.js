'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('preference', {
                parent: 'entity',
                url: '/preferences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.preference.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/preference/preferences.html',
                        controller: 'PreferenceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('preference');
                        $translatePartialLoader.addPart('units');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('preference.detail', {
                parent: 'entity',
                url: '/preference/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.preference.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/preference/preference-detail.html',
                        controller: 'PreferenceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('preference');
                        $translatePartialLoader.addPart('units');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Preference', function($stateParams, Preference) {
                        return Preference.get({id : $stateParams.id});
                    }]
                }
            })
            .state('preference.new', {
                parent: 'preference',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/preference/preference-dialog.html',
                        controller: 'PreferenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    weeklyGoal: null,
                                    weightUnits: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('preference', null, { reload: true });
                    }, function() {
                        $state.go('preference');
                    })
                }]
            })
            .state('preference.edit', {
                parent: 'preference',
                url: '/{id}/edit',
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
                                return Preference.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('preference', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('preference.delete', {
                parent: 'preference',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/preference/preference-delete-dialog.html',
                        controller: 'PreferenceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Preference', function(Preference) {
                                return Preference.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('preference', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
