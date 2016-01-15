(function () {
    'use strict';

    angular.module('21pointsApp')
            .factory('WeightSearch', WeightSearch);

    WeightSearch.$inject = ['$resource', 'DateUtils'];

    function WeightSearch($resource, DateUtils) {
        return $resource('api/_search/weights', {}, {
            'query': {
                method: 'POST',
                isArray: true,
                transformRequest: function (data) {
                    data.date = DateUtils.convertDateUTCToLocal(data.date);
                    return angular.toJson(data);
                }
            }
        });
    }
    
})();

