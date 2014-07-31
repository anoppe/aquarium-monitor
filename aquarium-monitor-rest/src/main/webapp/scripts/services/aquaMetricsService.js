'use strict';

angular.module('aqua.monitor.services').service('AquaMetricsService', ['$resource', '$http', function($resource, $http) {
	return {
		rest: $resource('aquametrics', {}, {
			pastHour : {url: 'aquametrics/pastHour', method: 'GET', isArray: true},
			pastDay : {url: 'aquametrics/pastDay', method: 'GET', isArray: true},
			pastWeek : {url: 'aquametrics/pastWeek', method: 'GET', isArray: true},
			pastMonth : {url: 'aquametrics/pastMonth', method: 'GET', isArray: true},
		})
		
	};
}]);