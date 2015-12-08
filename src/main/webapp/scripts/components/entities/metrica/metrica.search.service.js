'use strict';

angular.module('21pointsApp')
    .factory('MetricaSearch', function ($resource) {
        return $resource('api/_search/metricas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
