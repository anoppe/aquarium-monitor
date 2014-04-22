require
		.config({
			shim : {
				'angular-route' : [ 'angular' ],
				'angular-resource' : [ 'angular' ],
				app : [ 'angular-route' ],
				flot : ['jquery'],
				flotTime : ['flot'],
				chartDirective : ['flotTime']
			},
			paths : {
				'stomp' : 'bower_components/stomp-websocket/lib/stomp',
				sockjs : 'bower_components/sockjs/sockjs',
				json3 : 'bower_components/json3/lib/json3.min',
				'es5-shim' : 'bower_components/es5-shim/es5-shim',
				'angular-scenario' : 'bower_components/angular-scenario/angular-scenario',
				'angular-sanitize' : 'bower_components/angular-sanitize/angular-sanitize',
				'angular-route' : 'bower_components/angular-route/angular-route',
				'angular-resource' : 'bower_components/angular-resource/angular-resource',
				'angular-mocks' : 'bower_components/angular-mocks/angular-mocks',
				'angular-cookies' : 'bower_components/angular-cookies/angular-cookies',
				'angular-bootstrap' : 'bower_components/angular-bootstrap/ui-bootstrap-tpls',
				angular : 'bower_components/angular/angular',
				app : 'scripts/app',
				modules : 'scripts/modules',
				messageBus : 'scripts/services/messageBus',
				chartDirective : 'scripts/directives/chartDirective',
				mainController : 'scripts/controllers/MainController',
				'requirejs-domready' : 'bower_components/requirejs-domready/domReady',
				requirejs : 'bower_components/requirejs/require',
				jquery : 'bower_components/jquery/dist/jquery',
				bootstrap : 'bower_components/bootstrap/dist/js/bootstrap',
				flot : 'bower_components/flot/jquery.flot',
				flotTime : 'bower_components/flot/jquery.flot.time'
			},
			deps : [ 'app' ]
		});
