'use strict';
angular.module('aqua.monitor.factories').factory('commonsFactory', function() {
	return {
		contextRoot : function() { 
			//Location object's href gives the complete URL
			var url = window.location.href;
			//We need to extract the context root from the full URL.
			var regExp = /^http:\/\/[0-9a-zA-Z.]*\/[0-9a-zA-Z]*\//;
			var matches = url.match(regExp);
			console.log(url);
			//var url = "http://172.28.49.18:8084/Cyclops/login.do 
			//it will return http://172.28.49.18:8084/Cyclops/
			console.log(window.location.pathname);
			return matches ;
		}
	};
});