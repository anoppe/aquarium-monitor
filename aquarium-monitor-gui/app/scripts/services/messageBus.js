define(['app','sockjs', 'stomp'], function(sockjs, stomp) {
	'use strict';

	
	angular.module('aqua.monitor.services').service('MessageBusService', ['$q', '$rootScope', function($q, $rootScope) {
		
		var MessageBusService = {};
		var listeners = null;
		var defer = $q.defer();
		var SockJS = require('sockjs');
		var bus, sendName, form, connectButton, disconnectButton, responseContainer;
	
		var socket = new SockJS('http://localhost:8080/aqua-monitor/systemMetrics');

		var stompClient = Stomp.over(socket);
		stompClient.debug = function(str) {
//			console.log('response ' + str);
		}
		stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);
			stompClient.subscribe('/queue/systemMetrics', function(metric){
				MessageBusService.callback(JSON.parse(metric.body));
			});
		});
		
		MessageBusService.getMetrics = function(callback) {
			MessageBusService.callback = callback;
		};
		
	
		return MessageBusService;
	}]);

});