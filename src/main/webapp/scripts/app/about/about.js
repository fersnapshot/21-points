(function () {
    'use strict';

    angular.module('21pointsApp')
            .config(AboutConfig);

    AboutConfig.$inject = ['$stateProvider'];

    function AboutConfig($stateProvider) {
        $stateProvider
                .state('about', {
                    parent: 'site',
                    url: '/about',
                    data: {
                        roles: []
                    },
                    views: {
                        'content@': {
                            templateUrl: 'scripts/app/about/about.html'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('about');
                                return $translate.refresh();
                            }]
                    }
                });
    }

})();
