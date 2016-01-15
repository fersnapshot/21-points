(function () {
    'use strict';

    angular
            .module('21pointsApp')
            .factory('BloodPressure', BloodPressure);

    BloodPressure.$inject = ['$resource', 'DateUtils'];
    function BloodPressure($resource, DateUtils) {
        return $resource('api/bloodPressures/:id', {}, {
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
                url: '/api/bp-by-days',
                transformResponse: function (data) {
                    return angular.fromJson(data);
                }
            },
            'byMonth': { 
                method: 'GET', 
                isArray: true, 
                url: 'api/bp-by-month/:month'
            }
        });
    }
    

})();
