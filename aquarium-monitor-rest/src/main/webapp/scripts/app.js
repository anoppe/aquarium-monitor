'use strict';
	
var app = angular.module('aqua.monitor').config([ '$routeProvider', '$controllerProvider', '$provide',
		function($routeProvider, $controllerProvider, $provide) {

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
				templateUrl : 'views/main.html',
//				controller : 'MainController',
			}).when('/graphs', {
				templateUrl : 'views/graphs.html',
//				controller : 'MainController',
			}).otherwise({
				redirectTo : '/'
			});
		}]);

