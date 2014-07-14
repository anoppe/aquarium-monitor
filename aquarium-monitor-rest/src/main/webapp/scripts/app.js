'use strict';
	
var app = angular.module('aqua.monitor').config([ '$routeProvider', '$controllerProvider', '$provide', '$httpProvider',
		function($routeProvider, $controllerProvider, $provide, $httpProvider) {

			app.register = {
				controller : $controllerProvider.register,
				factory : $provide.factory
			};

			function resolveController(names) {
				return {
					load : [ '$q', '$rootScope', function($q, $rootScope) {
						var defer = $q.defer();
						require(names, function() {
							defer.resolve();
							$rootScope.$apply();
						});
						return defer.promise;
					} ]
				};
			}
			$routeProvider.when('/', {
				templateUrl : 'views/graphs.html',
//				controller : 'MainController',
			}).when('/graphs', {
				templateUrl : 'views/graphs.html',
//				controller : 'MainController',
			}).otherwise({
				redirectTo : '/'
			});
			
			$httpProvider.interceptors.push(function($q, $rootScope) {
                return {
                    'request': function(config) {
                    	console.log('request');
                        $rootScope.$broadcast('loading-started');
                        return config || $q.when(config);
                    },
                    'response': function(response) {
                    	console.log('response');
                        $rootScope.$broadcast('loading-complete');
                        return response || $q.when(response);
                    }
                };
            });
		}
]);

