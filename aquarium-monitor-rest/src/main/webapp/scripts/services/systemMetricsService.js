'use strict';

angular.module('aqua.monitor.services').service('systemMetricsService', ['$resource', function($resource) {
	return $resource('systemMetrics', {}, {
		pastHour : {url: 'systemMetrics/pastHour', method: 'GET', isArray: true},
		pastDay : {url: 'systemMetrics/pastDay', method: 'GET', isArray: true}
	});
}]);