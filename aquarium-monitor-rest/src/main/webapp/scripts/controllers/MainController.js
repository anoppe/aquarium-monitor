'use strict';

angular.module('aqua.monitor.controllers').controller('MainController', ['$scope', 'systemMetricsService', 'MessageBusService', function ($scope, systemMetricsService, MessageBusService) {
	$scope.awesomeThings = [];
	$scope.systemMetrics = [[1]];
	$scope.systemMetrics[0] = systemMetricsService.pastHour();
	
	MessageBusService.getMetrics(function(metrics) {
		$scope.systemMetrics[0].push([metrics.occuredDatetime, metrics.usedMemory]);
		$scope.$apply();
	});

	$scope.getPastHour = function() {
		$scope.systemMetrics[0] = systemMetricsService.pastHour();
	};
	
	$scope.getPastDay = function() {
		$scope.systemMetrics[0] = systemMetricsService.pastDay();
	};
	
}]);
