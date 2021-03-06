'use strict';

angular.module('21pointsApp')
    .factory('Weight', function ($resource, DateUtils) {
        return $resource('api/weights/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.timestamp = DateUtils.convertDateTimeFromServer(data.timestamp);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'lastDays': { 
                method: 'GET',
                url: '/api/w-by-days',
                transformResponse: function (data) {
                    return angular.fromJson(data);
                }
            },
            'byMonth': { 
                method: 'GET', 
                isArray: true, 
                url: 'api/w-by-month/:month'
            }
        });
    });
