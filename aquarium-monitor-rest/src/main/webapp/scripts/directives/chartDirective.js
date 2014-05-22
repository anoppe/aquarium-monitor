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
    				    mode: "time",   
    				    timeformat: "%h:%M:%S",
    				    timezone: "browser"
           			},
           			points: { show: true, symbol: "cross" }
           		};
            var data = scope[attrs.ngModel];
            scope.$watch(function() {
            	var sortedData = [[]]; 
            	angular.forEach(data[0], function(v, k) {
            		sortedData[0].push([v.occuredDatetime, v.usedMemory]);
            	});

//            	while (sortedData[0].length >= 50) { 
//        			sortedData[0].shift();
//        		}
            	
            	return sortedData;
            }, function(v) {
                chart = $.plot(elem, v , options);
            }, true);
        },
    };
}]);
