'use strict';

angular.module('aqua.monitor.directives', []);
angular.module('aqua.monitor.services',[]);
angular.module('aqua.monitor.controllers', ['aqua.monitor.services', 'aqua.monitor.directives']);
angular.module('aqua.monitor', [ 'ngRoute', 'aqua.monitor.controllers' ]);
	
