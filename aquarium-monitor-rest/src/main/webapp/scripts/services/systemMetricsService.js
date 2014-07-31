'use strict';

angular.module('aqua.monitor.services').service('systemMetricsService', ['$resource', '$http', function($resource, $http) {
	return {
		rest: $resource('systemMetrics', {}, {
			pastHour : {url: 'systemMetrics/pastHour', method: 'GET', isArray: true},
			pastDay : {url: 'systemMetrics/pastDay', method: 'GET', isArray: true},
			maxMemory : {url: 'systemMetrics/maxmemory', method: 'GET'}
		}),
		
	};
}]);