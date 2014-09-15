'use strict';

app.controller('SystemMetricsController', ['$scope', '$timeout', 'systemMetricsService', 'MessageBusService', function ($scope, $timeout, systemMetricsService, MessageBusService) {
	var _this = this;
	var maxMemory = 0;

	systemMetricsService.rest.maxMemory().$promise.then(function(data) {
		maxMemory = data.maxMemory;
	}).then(
		systemMetricsService.rest.pastHour().$promise.then(function(data) {
			var memoryData = [];
			var cpuData  = [];
			
	        angular.forEach(data, function(v, k) {
	        	memoryData.push({x: v.occuredDatetime, y: v.usedMemory});
	        	cpuData.push({x: v.occuredDatetime, y: v.cpuUtilization});
	        });
	        
	        $scope.highchartsNG = function() {
				var options = angular.copy(systemSplineOptions);
				options.series = [ {
					name : 'Used Memory',
					data : memoryData,
		            tooltip: {
		            	valueDecimals: 1,
		            	valueSuffix: 'Mb'
		            },
		            turboThreshold: 0
				}];
				return options;
				
			}();
			
			$scope.cpuUsageGraph = function() {
	            	
		       var options = angular.copy(systemSplineOptions);
		        options.series = [{
		        	name : 'Cpu Usage',
		            data: cpuData,
		            tooltip: {
		            	valueDecimals: 1,
		            	valueSuffix: '%'
		            },
		            turboThreshold: 0
		        }];
		        return options;
			}();
		
			$scope.systemMemNG = function() {
				var options = angular.copy(systemGaugeOptions );
				options.yAxis.max = maxMemory;
				options.series = [{
					name: 'Used Memory',
					data: [data[data.length-1].usedMemory],
					dataLabels: {
						format: '<div style="text-align:center"><span style="font-size:25px;color:' + 
						((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' + 
						'<span style="font-size:12px;color:silver">Mb</span></div>'
					},
					tooltip: {
						valueSuffix: ' Mb'
					}
				}];
				return options;
			}();
			
			$scope.systemSwapNG = function() {
				var options = angular.copy(systemGaugeOptions );
				options.yAxis.max = maxMemory;
				options.series = [{
					name: 'Used Swap',
					data: [data[data.length-1].usedSwap],
					dataLabels: {
						format: '<div style="text-align:center"><span style="font-size:25px;color:' + 
						((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' + 
						'<span style="font-size:12px;color:silver">Mb</span></div>'
					},
					tooltip: {
						valueSuffix: ' Mb'
					}
				}];
				return options;
			}();
			
			$scope.systemFreeNG = function() {
				var options = angular.copy(systemGaugeOptions );
				options.yAxis.max = maxMemory;
				options.series = [{
					name: 'Free memory',
					data: [data[data.length-1].freeMemory],
					dataLabels: {
						format: '<div style="text-align:center"><span style="font-size:25px;color:' + 
						((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' + 
						'<span style="font-size:12px;color:silver">Mb</span></div>'
					},
					tooltip: {
						valueSuffix: ' Mb'
					}
				}];
				return options;
			}();
			
			
			$scope.cpuUsageNG = function() {
				var options = angular.copy(systemGaugeOptions );
				options.yAxis.max = 100;
				options.series = [{
					name: 'CPU Utilization',
					data: [data[data.length-1].cpuUtilization],
					dataLabels: {
						format: '<div style="text-align:center"><span style="font-size:25px;color:' + 
						((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' + 
						'<span style="font-size:12px;color:silver">%</span></div>'
					},
					tooltip: {
						valueSuffix: ' %'
					}
				}];
				
				return options;
			}();
	}));
	
	MessageBusService.getMetrics(function(metric) {
		if ($scope.highchartsNG && !$scope.highchartsNG.loading) {
//			$scope.highchartsNG.series[0].data.splice(0,1);
			$scope.highchartsNG.series[0].data.push({x:metric.occuredDatetime, y:metric.usedMemory});
		}
		
		if ($scope.cpuUsageGraph && !$scope.cpuUsageGraph.loading) {
//			$scope.cpuUsageGraph.series[0].data.splice(0,1);
			$scope.cpuUsageGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.cpuUtilization});
		}
	
		if ($scope.systemMemNG && !$scope.systemMemNG.loading) {
			$scope.systemMemNG.series[0].data[0] = metric.usedMemory;
			$scope.systemFreeNG.series[0].data[0] = metric.freeMemory;
			$scope.systemSwapNG.series[0].data[0] = metric.usedSwap;
			$scope.cpuUsageNG.series[0].data[0] = Math.round(metric.cpuUtilization * 100) / 100;
		}
	
	});

	var applyDataset = function(data) {
		var cpuData = [];
		var memoryData = [];
		angular.forEach(data, function(v, k) {
			memoryData.push({x: v.occuredDatetime, y: v.usedMemory});
			cpuData.push({x: v.occuredDatetime, y: v.cpuUtilization});
		});
		$scope.highchartsNG.series[0].data = memoryData;
		$scope.cpuUsageGraph.series[0].data = cpuData;
	};
	
	$scope.pastHour = function() {
		systemMetricsService.rest.pastHour().$promise.then(function(data) {
			applyDataset(data);
	   });
	};
	
	$scope.pastDay = function() {
		systemMetricsService.rest.pastDay().$promise.then(function(data) {
			applyDataset(data);
		});
	};
	
	$scope.pastWeek = function() {
		systemMetricsService.rest.pastWeek().$promise.then(function(data) {
			applyDataset(data);
		});
	};

	$scope.pastMonth = function() {
		systemMetricsService.rest.pastMonth().$promise.then(function(data) {
			applyDataset(data);
		});
	};
}]);


var systemGaugeOptions = {
	options: {
        chart: {
            type: 'solidgauge'
        },
        pane: {
	    	size: '90%',
	        startAngle: -90,
	        endAngle: 90,
            background: {
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                innerRadius: '60%',
                outerRadius: '100%',
                shape: 'solid'
            }
        },
        plotOptions: {
            solidgauge: {
                dataLabels: {
                    y: 5,
                    borderWidth: 0,
                    useHTML: true
                }
            }
        },
        title: {
        	text : null,
        }
    },
    yAxis: {
    	min : 0,
		stops: [
			[0.1, '#55BF3B'], // green
        	[0.5, '#DDDF0D'], // yellow
        	[0.9, '#DF5353'] // red
		],
		lineWidth: 0,
        minorTickInterval: null,
        tickPixelInterval: 400,
        tickWidth: 0,
        labels: {
            y: 16
        }        
    },
    credits: {
    	enabled: false
    },
    loading: false
};

var systemSplineOptions = {
	credits: {
		enabled: false
	},
	options : {
		plotOptions: {
			spline: {
				marker: {
					enabled: false
				}
			}
		},
		chart : {
			zoomType: 'x',
			type : 'spline'
		},
		rangeSelector: {
	        buttons: [{
	            type: 'hour',
	            count: 1,
	            text: '1h'
	        },{
	            type: 'day',
	            count: 1,
	            text: '1d'
	        }, {
	            type: 'week',
	            count: 1,
	            text: '1w'
	        }, {
	            type: 'month',
	            count: 1,
	            text: '1m'
	        }],
	        selected: 1,
	    	allButtonsEnabled: true,
	    	inputEnabled: false
	    },
	    title: {
	    	text: null
	    },
	},
	xAxis : {
		type : 'datetime',
		title : {
			text : 'Date'
		}
	},
	loading : false,
	useHighStocks : true
};
