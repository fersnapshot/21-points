'use strict';

angular.module('21pointsApp')
    .factory('BloodPressureSearch', function ($resource, DateUtils) {
        return $resource('api/_search/bloodPressures', {}, {
            'query': { 
                method: 'POST', 
                isArray: true,
                transformRequest: function (data) {
                    data.date = DateUtils.convertDateUTCToLocal(data.date);
                    return angular.toJson(data);
                }
            }
        });
    });
