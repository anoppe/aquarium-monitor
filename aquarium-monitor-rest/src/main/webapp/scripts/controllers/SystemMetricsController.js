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
	        
	        _this.highchartsNG = function() {
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
			
			_this.cpuUsageGraph = function() {
	            	
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
		
			_this.systemMemNG = function() {
				var options = angular.copy(GaugeOptions);
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
			
			_this.systemSwapNG = function() {
				var options = angular.copy(GaugeOptions);
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
			
			_this.systemFreeNG = function() {
				var options = angular.copy(GaugeOptions);
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
			
			
			_this.cpuUsageNG = function() {
				var options = angular.copy(GaugeOptions);
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
		
		if (!_this.highchartsNG && !_this.highchartsNG.loading) {
			_this.highchartsNG.series[0].data.splice(0,1);
			_this.highchartsNG.series[0].data.push({x:metric.occuredDatetime, y:metric.usedMemory});
		}
		
		if (!_this.cpuUsageGraph && !_this.cpuUsageGraph.loading) {
			_this.cpuUsageGraph.series[0].data.splice(0,1);
			_this.cpuUsageGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.cpuUtilization});
		}
	
		if (!_this.systemMemNG && !_this.systemMemNG.loading) {
			_this.systemMemNG.series[0].data[0] = metric.usedMemory;
			_this.systemFreeNG.series[0].data[0] = metric.freeMemory;
			_this.systemSwapNG.series[0].data[0] = metric.usedSwap;
			_this.cpuUsageNG.series[0].data[0] = Math.round(metric.cpuUtilization * 100) / 100;
		}
	
		$scope.$apply();
	});

	var applyDataset = function(data) {
		var cpuData = [];
		var memoryData = [];
		angular.forEach(data, function(v, k) {
			memoryData.push({x: v.occuredDatetime, y: v.usedMemory});
			cpuData.push({x: v.occuredDatetime, y: v.cpuUtilization});
		});
		_this.highchartsNG.series[0].data = memoryData;
		_this.cpuUsageGraph.series[0].data = cpuData;
	};
	
	this.pastHour = function() {
		systemMetricsService.rest.pastHour().$promise.then(function(data) {
			applyDataset(data);
	   });
	};
	
	this.pastDay = function() {
		systemMetricsService.rest.pastDay().$promise.then(function(data) {
			applyDataset(data);
		});
	};
	
	this.pastWeek = function() {
		systemMetricsService.rest.pastWeek().$promise.then(function(data) {
			applyDataset(data);
		});
	};

	this.pastMonth = function() {
		systemMetricsService.rest.pastMonth().$promise.then(function(data) {
			applyDataset(data);
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

var systemSplineOptions = {
	credits: {
		enabled: false
	},
    rangeSelector: {
		inputEnabled: true,
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
        }, {
            type: 'month',
            count: 6,
            text: '6m'
        }, {
            type: 'year',
            count: 1,
            text: '1y'
        }, {
            type: 'all',
            text: 'All'
        }],
        selected: 1,
    	allButtonsEnabled: true
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
		}
	},
	xAxis : {
		type : 'datetime',
		title : {
			text : 'Date'
		}
	},
	title: {
		text: '',
		style: {
			display: 'none'
		}
	},
	loading : false,
	useHighStock : true
};
