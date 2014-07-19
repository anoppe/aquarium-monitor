'use strict';
angular.module('aqua.monitor.services').service('MessageBusService', ['$q', '$rootScope', 'commonsFactory', function($q, $rootScope, commonsFactory) {
	
	var MessageBusService = {};
	var defer = $q.defer();
	var socket = new SockJS(window.location.pathname + 'systemMetrics');

	var stompClient = Stomp.over(socket);
	stompClient.debug = function(str) {
//		console.log(str);
	};
	
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/queue/systemMetrics', function(metric) {
			if (MessageBusService.callback) {
				MessageBusService.callback(JSON.parse(metric.body));
			}
		});
		
		stompClient.subscribe('/queue/aquaMetrics', function(metric) {
			if (MessageBusService.aquariumMetricsCallback) {
				MessageBusService.aquariumMetricsCallback(JSON.parse(metric.body));
			}
		});
	});
	
	MessageBusService.getMetrics = function(callback) {
		MessageBusService.callback = callback;
	};
	
	MessageBusService.getAquariumMetrics = function(callback) {
		MessageBusService.aquariumMetricsCallback = callback;
	};
	
	return MessageBusService;
}]);

