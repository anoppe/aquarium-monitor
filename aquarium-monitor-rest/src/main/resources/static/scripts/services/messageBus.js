'use strict';
angular.module('aqua.monitor.services').service('MessageBusService', ['$q', '$rootScope', 'commonsFactory', function($q, $rootScope, commonsFactory) {
	var _this = this;
	var MessageBusService = {};
	var isConnected = false;
	var systemMetricsSubscription = null;
	var aquaMetricsSubscription = null;
	var stompClient = null;
	var socket = null;
	_this.systemSubscriptionDeferred = $q.defer();
	_this.aquaSubscriptionDeferred = $q.defer();
	
	MessageBusService.subscribeToSystemMetrics = function() {

		_this.connect().then(function() {
			systemMetricsSubscription = stompClient.subscribe('/queue/systemMetrics', function(metric) {
				_this.systemSubscriptionDeferred.notify(JSON.parse(metric.body));
			})
		});
		
		return _this.systemSubscriptionDeferred.promise;
	};
	
	MessageBusService.subscribeToAquaMetrics = function() {

		_this.connect().then(function() {
			aquaMetricsSubscription = stompClient.subscribe('/queue/aquaMetrics', function(metric) {
				_this.aquaSubscriptionDeferred.notify(JSON.parse(metric.body));
			});
		});
		
		return _this.aquaSubscriptionDeferred.promise;
	};
	
	MessageBusService.unsubscribeFromSystemMetrics = function() {
		systemMetricsSubscription.unsubscribe();
	};
	
	MessageBusService.unsubscribeFromAquaMetrics = function() {
		aquaMetricsSubscription.unsubscribe();
	};
	
	this.connect = function() {
		var deferred = $q.defer();
		var defer = $q.defer();
		if (!isConnected) {
			
			socket = new SockJS(window.location.pathname + 'systemMetrics');
			socket.onopen = function() {
				defer.resolve();
			};
			socket.onclose = function() {
				_this.connect();
			};
			
//			defer.promise.then(function() {
				stompClient = Stomp.over(socket);
				stompClient.debug = function(str) {
//					console.log(str);
				};
				
//			}).then(function() {
				stompClient.connect(null, null, function(frame) {
					isConnected = true;
					deferred.resolve();
				}, function(error) {
					console.log(error);
				});
//			});
		} else {
			deferred.resolve();
		}
		
		return deferred.promise;
	};

	
	return MessageBusService;
}]);

