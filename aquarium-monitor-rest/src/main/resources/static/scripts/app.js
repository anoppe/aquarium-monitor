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
			$routeProvider.when('/system', {
				templateUrl : 'views/system.html',
				controller : 'SystemMetricsController',
			}).when('/aquarium', {
				templateUrl : 'views/aquarium.html',
				controller : 'AquaMetricsController',
			}).otherwise({
				redirectTo : '/aquarium'
			});
			
			$httpProvider.interceptors.push(function($q, $rootScope) {
                return {
                    'request': function(config) {
                        $rootScope.$broadcast('loading-started');
                        return config || $q.when(config);
                    },
                    'response': function(response) {
                        $rootScope.$broadcast('loading-complete');
                        return response || $q.when(response);
                    }
                };
            });
		}
]);

