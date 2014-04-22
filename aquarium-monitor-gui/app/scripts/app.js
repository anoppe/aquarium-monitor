define(['modules', 'mainController'], function() {
	'use strict';
	
	var app = angular.module('aqua.monitor').config([ '$routeProvider', '$controllerProvider', '$provide',
			function($routeProvider, $controllerProvider, $provide) {

				app.register = {
					controller : $controllerProvider.register,
					factory : $provide.factory
				}

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
					}
				}
				$routeProvider.when('/', {
					templateUrl : 'views/main.html',
					controller : 'MainController',
				}).otherwise({
					redirectTo : '/'
				})
			}]);
		/*
		 .factory('$exceptionHandler', function () {
		        return function (exception, cause) {
		            console.log(exception);
		            console.log(cause);
		        };
		    });
		 */
	angular.bootstrap(document, [ 'aqua.monitor' ]);

	return app;
});
