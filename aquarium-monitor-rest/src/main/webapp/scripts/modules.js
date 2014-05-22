'use strict';

angular.module('aqua.monitor.directives', []);
angular.module('aqua.monitor.factories', []);
angular.module('aqua.monitor.services',['aqua.monitor.factories']);
angular.module('aqua.monitor.controllers', ['aqua.monitor.services', 'aqua.monitor.directives', 'ngResource']);
angular.module('aqua.monitor', [ 'ngRoute', 'aqua.monitor.controllers' ]);
	
