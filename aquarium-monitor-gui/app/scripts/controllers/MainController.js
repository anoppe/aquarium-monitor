'use strict';

angular.module('aqua.monitor.controllers').controller('MainController', ['$scope', 'MessageBusService', function ($scope, MessageBusService) {
	$scope.awesomeThings = [];
	$scope.systemMetrics = [[1]];
	var metrics = MessageBusService.getMetrics(function(metrics) {
		if ($scope.systemMetrics[0].length >= 10) { 
			$scope.systemMetrics[0].shift();
		}
		$scope.systemMetrics[0].push([metrics.occuredDatetime, metrics.usedMemory]);
		$scope.$apply();
	});
}]);
