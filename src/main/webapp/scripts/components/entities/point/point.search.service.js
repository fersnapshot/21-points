'use strict';

angular.module('21pointsApp')
    .factory('PointSearch', function ($resource) {
        return $resource('api/_search/points/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
