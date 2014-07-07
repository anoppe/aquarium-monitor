'use strict';
// <highchart id="chart1" config="highchartsNG"></highchart>
angular.module('aqua.monitor.directives').directive('highchart', [function() {
    return {
        restrict: 'A',
        order: 9999,
        scope : {
        	chartData : '=ngModel'
        },
        link: function(scope, elem, attrs) {
        	console.log(attrs);
        	
        	var chart = null;
        	var options = {
           			lines: {
           				show: true
           			},
           			xaxis: {
    				    mode: "time",   
    				    timeformat: "%h:%M:%S",
    				    timezone: "browser"
           			},
           			points: { show: false }
           		};
        	console.log(scope.chartData);
            scope.$watch(function() {
            	var sortedData = [[]]; 
            	angular.forEach(scope.chartData, function(v, k) {
            		sortedData[0].push([v.occuredDatetime, v.usedMemory]);
            	});

            	while (sortedData[0].length >= 50) { 
        			sortedData[0].shift();
        		}
            	
            	return sortedData;
            }, function(v) {
                chart = $.plot(elem, v , options);
            }, true);
        },
    };
}]);
