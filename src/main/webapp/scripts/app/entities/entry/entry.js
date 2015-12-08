'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entry', {
                parent: 'entity',
                url: '/entrys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.entry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/entry/entrys.html',
                        controller: 'EntryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('entry');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('entry.detail', {
                parent: 'entity',
                url: '/entry/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.entry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/entry/entry-detail.html',
                        controller: 'EntryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('entry');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Entry', function($stateParams, Entry) {
                        return Entry.get({id : $stateParams.id});
                    }]
                }
            })
            .state('entry.new', {
                parent: 'entry',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/entry/entry-dialog.html',
                        controller: 'EntryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    version: null,
                                    date: null,
                                    exercise: null,
                                    meals: null,
                                    alcohol: null,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('entry', null, { reload: true });
                    }, function() {
                        $state.go('entry');
                    })
                }]
            })
            .state('entry.edit', {
                parent: 'entry',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/entry/entry-dialog.html',
                        controller: 'EntryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Entry', function(Entry) {
                                return Entry.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('entry', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('entry.delete', {
                parent: 'entry',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/entry/entry-delete-dialog.html',
                        controller: 'EntryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Entry', function(Entry) {
                                return Entry.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('entry', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
