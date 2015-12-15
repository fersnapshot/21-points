'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('point', {
                parent: 'entity',
                url: '/points',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.point.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/point/points.html',
                        controller: 'PointController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('point');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('point.detail', {
                parent: 'entity',
                url: '/point/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: '21pointsApp.point.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/point/point-detail.html',
                        controller: 'PointDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('point');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Point', function($stateParams, Point) {
                        return Point.get({id : $stateParams.id});
                    }]
                }
            })
            .state('point.new', {
                parent: 'point',
                url: '/new',
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
                        $state.go('point', null, { reload: true });
                    }, function() {
                        $state.go('point');
                    })
                }]
            })
            .state('point.edit', {
                parent: 'point',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/point/point-dialog.html',
                        controller: 'PointDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Point', function(Point) {
                                return Point.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('point', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('point.delete', {
                parent: 'point',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/point/point-delete-dialog.html',
                        controller: 'PointDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Point', function(Point) {
                                return Point.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('point', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
