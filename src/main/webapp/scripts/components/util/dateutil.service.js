'use strict';

angular.module('21pointsApp')
    .service('DateUtils', function ($filter) {
      this.convertLocaleDateToServer = function(date) {
        if (date) {
          return new Date($filter('date')(date, 'yyyy-MM-dd'));
        } else {
          return null;
        }
      };
      this.convertLocaleDateFromServer = function(date) {
        if (date) {
          var dateString = date.split("-");
          return new Date(dateString[0], dateString[1] - 1, dateString[2]);
        }
        return null;
      };
      this.convertDateTimeFromServer = function(date) {
        if (date) {
          return new Date(date);
        } else {
          return null;
        }
      };
      this.convertUTCToLocal = function(date) {
        if (date) {
            date.setHours(0,0,0,0);
            return new Date(date.getTime()-(date.getTimezoneOffset()*60*1000));
        } else {
          return null;
        }
      };
    });
