'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('metrica', {
                parent: 'entity',
                url: '/metricas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.metrica.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metrica/metricas.html',
                        controller: 'MetricaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metrica');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('metrica.detail', {
                parent: 'entity',
                url: '/metrica/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.metrica.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metrica/metrica-detail.html',
                        controller: 'MetricaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metrica');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Metrica', function($stateParams, Metrica) {
                        return Metrica.get({id : $stateParams.id});
                    }]
                }
            })
            .state('metrica.new', {
                parent: 'metrica',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/metrica/metrica-dialog.html',
                        controller: 'MetricaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    version: null,
                                    name: null,
                                    amount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('metrica', null, { reload: true });
                    }, function() {
                        $state.go('metrica');
                    })
                }]
            })
            .state('metrica.edit', {
                parent: 'metrica',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/metrica/metrica-dialog.html',
                        controller: 'MetricaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Metrica', function(Metrica) {
                                return Metrica.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('metrica', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('metrica.delete', {
                parent: 'metrica',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/metrica/metrica-delete-dialog.html',
                        controller: 'MetricaDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Metrica', function(Metrica) {
                                return Metrica.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('metrica', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
