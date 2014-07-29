'use strict';

app.controller('SystemMetricsController', ['$scope', '$timeout', 'systemMetricsService', 'MessageBusService', function ($scope, $timeout, systemMetricsService, MessageBusService) {
	var _this = this;
	var maxMemory = 16000;
	this.highchartsNG = function() {
		var options = angular.copy(splineOptions);
		options.series = [ {
			name : 'Used Memory',
			data : (function() {
				var sortedData = [];
				systemMetricsService.rest.pastHour().$promise.then(function(data) {
					while (data.length >= 25) {
						data.shift();
					}
					angular.forEach(data, function(v, k) {
						sortedData.push({
							x : v.occuredDatetime,
							y : v.usedMemory
						});
					});
				});

				return sortedData;

			})()
		} ];
		return options;
	}();
	        
	
	this.cpuUsageGraph = {
	        options: {
	            chart: {
	                type: 'spline'
	            }
	        },
	        xAxis: {
                type: 'datetime',
                title: {
                    text: 'Date'
                }
            },
	        series: [{
	        	name : 'Cpu Usage',
	            data: (function() {
	            	var sortedData = []; 
	            	systemMetricsService.rest.pastHour().$promise.then(function(data) {
		            		while (data.length >= 25) { 
		            			data.shift();
		            		}
	            	       angular.forEach(data, function(v, k) {
	            	    	   sortedData.push({x: v.occuredDatetime, y: v.cpuUtilization});
	            	       });
	            	   });

	            	return sortedData;
	            	
	            })()
	        }],
	        loading: false
	    };
	
	this.systemMemNG = function() {
		var options = angular.copy(GaugeOptions);
		options.yAxis.max = maxMemory;
		options.series = [{
			name: 'Used Memory',
			data: [0],
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
	
	this.systemSwapNG = function() {
		var options = angular.copy(GaugeOptions);
		options.yAxis.max = maxMemory;
		options.series = [{
			name: 'Used Swap',
			data: [0],
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
	
	this.systemFreeNG = function() {
		var options = angular.copy(GaugeOptions);
		options.yAxis.max = maxMemory;
		options.series = [{
			name: 'Used Swap',
			data: [0],
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
	
	
	this.cpuUsageNG = function() {
		var options = angular.copy(GaugeOptions);
		options.yAxis.max = 100;
		options.series = [{
			name: 'CPU Utilization',
			data: [0],
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
	
	
	MessageBusService.getMetrics(function(metric) {
		
		_this.highchartsNG.series[0].data.splice(0,1);
		_this.highchartsNG.series[0].data.push({x:metric.occuredDatetime, y:metric.usedMemory});
		
		_this.cpuUsageGraph.series[0].data.splice(0,1);
		_this.cpuUsageGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.cpuUtilization});
	
		if (!_this.systemMemNG.loading) {
			_this.systemMemNG.series[0].data[0] = metric.usedMemory;
			_this.systemFreeNG.series[0].data[0] = metric.freeMemory;
			_this.systemSwapNG.series[0].data[0] = metric.usedSwap;
			_this.cpuUsageNG.series[0].data[0] = Math.round(metric.cpuUtilization * 100) / 100;
		}
	
		$scope.$apply();
	});

	this.getMemoryPastHour = function() {
		systemMetricsService.rest.pastHour().$promise.then(function(data) {
			var sortedData = [];
    		while (data.length >= 50) { 
    			data.shift();
    		}
	       angular.forEach(data, function(v, k) {
	    	   sortedData.push({x: v.occuredDatetime, y: v.usedMemory});
	       });
	       _this.highchartsNG.series[0].data = sortedData;
	   });
	};
	
	this.getMemoryPastDay = function() {
		systemMetricsService.rest.pastDay().$promise.then(function(data) {
			var sortedData = [];
    		while (data.length >= 50) { 
    			data.shift();
    		}
	       angular.forEach(data, function(v, k) {
	    	   sortedData.push({x: v.occuredDatetime, y: v.usedMemory});
	       });
	       _this.highchartsNG.series[0].data = sortedData;
		});
	};
	
	this.getCpuPastHour = function() {
		systemMetricsService.rest.pastHour().$promise.then(function(data) {
			var sortedData = [];
    		while (data.length >= 50) { 
    			data.shift();
    		}
	       angular.forEach(data, function(v, k) {
	    	   sortedData.push({x: v.occuredDatetime, y: v.cpuUtilization});
	       });
	       _this.cpuUsageGraph.series[0].data = sortedData;
	   });
	};
	
	this.getCpuPastDay = function() {
		systemMetricsService.rest.pastDay().$promise.then(function(data) {
			var sortedData = [];
    		while (data.length >= 50) { 
    			data.shift();
    		}
	       angular.forEach(data, function(v, k) {
	    	   sortedData.push({x: v.occuredDatetime, y: v.cpuUtilization});
	       });
	       _this.cpuUsageGraph.series[0].data = sortedData;
		});
	};
	
}]);


var GaugeOptions = {
	options: {
        chart: {
            type: 'solidgauge'
        },
        pane: {
	    	center: ['50%', '30%'],
	    	size: '90%',
	        startAngle: -90,
	        endAngle: 90,
            background: {
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                innerRadius: '60%',
                outerRadius: '100%',
                shape: 'arc'
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
    title: {
        text : '',
        style : {display: 'none'}
    },
    loading: false
};

var splineOptions = {
	options : {
		chart : {
			type : 'spline'
		}
	},
	xAxis : {
		type : 'datetime',
		title : {
			text : 'Date'
		}
	},
	title : {
		text: '',
		style: {
			display: 'none'
		}
	},
	loading : false,
	useHighStock : true
};
