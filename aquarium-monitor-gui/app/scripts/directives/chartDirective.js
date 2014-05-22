'use strict';

angular.module('aqua.monitor.directives').directive('myCharts', [function() {
    return {
        restrict: 'A',
        order: 9999,
        link: function(scope, elem, attrs) {
        	console.log(attrs);
        	
        	var chart = null;
        	var options = {
           			lines: {
           				show: true
           			},
           			points: {
           				show: true
           			},
           			xaxis: {
    				    type: "time",   
    				    timeformat: "%M:%S"
           			}
           		};
            var data = scope[attrs.ngModel];
            scope.$watch(function() {
            	return data;
            }, function(v) {
                chart = $.plot(elem, v , options);
            }, true);
        },
    };
}]);
