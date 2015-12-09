'use strict';

angular.module('21pointsApp')
    .factory('PreferenceSearch', function ($resource) {
        return $resource('api/_search/preferences/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
