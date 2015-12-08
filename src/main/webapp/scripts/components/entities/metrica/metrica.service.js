'use strict';

angular.module('21pointsApp')
    .factory('Metrica', function ($resource, DateUtils) {
        return $resource('api/metricas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
