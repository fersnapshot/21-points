'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('weight', {
                parent: 'entity',
                url: '/weights',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.weight.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/weight/weights.html',
                        controller: 'WeightController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('weight');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('weight.detail', {
                parent: 'entity',
                url: '/weight/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.weight.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/weight/weight-detail.html',
                        controller: 'WeightDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('weight');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Weight', function($stateParams, Weight) {
                        return Weight.get({id : $stateParams.id});
                    }]
                }
            })
            .state('weight.new', {
                parent: 'weight',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/weight/weight-dialog.html',
                        controller: 'WeightDialogController',
                        size: 'lg',
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
                        $state.go('weight', null, { reload: true });
                    }, function() {
                        $state.go('weight');
                    });
                }]
            })
            .state('weight.edit', {
                parent: 'weight',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/weight/weight-dialog.html',
                        controller: 'WeightDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Weight', function(Weight) {
                                return Weight.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('weight', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('weight.delete', {
                parent: 'weight',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/weight/weight-delete-dialog.html',
                        controller: 'WeightDeleteController',
                        size: 'md',
                        resolve: {
                            id: function() {
                                return $stateParams.id;
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('weight', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    });
