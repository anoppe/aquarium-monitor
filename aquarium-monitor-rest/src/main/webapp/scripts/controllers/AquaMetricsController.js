'use strict';

(function() {
	
	app.controller('AquaMetricsController', ['$scope', 'AquaMetricsService', 'MessageBusService', function($scope, AquaMetricsService, MessageBusService) {
		var _this = this;
		
		AquaMetricsService.rest.pastHour().$promise.then(function(data) {
			var temperatureData = [];
			var phData = [];

			angular.forEach(data, function(v, k) {
				temperatureData.push({
					x : v.occuredDatetime,
					y : v.temperature
				});
				phData.push({
					x : v.occuredDatetime,
					y : ph
				})
			});
			
			_this.temperatureGraph = function() {
				var options = angular.copy(splineOptions);
				options.series = [ {
					name : 'Temperature',
					data : temperatureData,
					tooltip: {
						valueDecimals: 1,
						valueSuffix: 'Mb'
					},
					turboThreshold: 0
				}];
				options.yAxis = {
					title : {
						text : '°C'
					}
				};
				
				return options;
			}();
			_this.phGraph = function() {
				var options = angular.copy(splineOptions);
				options.series = [{
					name : 'PH',
					data: phData,
					tooltip: {
		            	valueDecimals: 1,
		            	valueSuffix: 'Mb'
		            },
		            turboThreshold: 0
				}];
				options.yAxis = {
					title: {
						text: 'PH'
					}
				}
				return options;
			}();
			
			_this.temperature = function() {
				var options = angular.copy(GaugeOptions);
				options.yAxis.max = 40;
				options.series = [{
					name: 'Temperature',
					data: [temperatureData[temperatureData.lengt-1].temperature],
					dataLabels: {
						format: '<div style="text-align:center"><span style="font-size:25px;color:' + 
						((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' + 
						'<span style="font-size:12px;color:silver">Celcius</span></div>'
					},
					tooltip: {
						valueSuffix: ' °C'
					}
				}];
				return options;
			}();
			
			_this.ph = function() {
				var options = angular.copy(GaugeOptions);
				options.yAxis.max = 10;
				options.series = [{
					name: 'PH',
					data: [phData[phData.length-1].ph],
					dataLabels: {
						format: '<div style="text-align:center"><span style="font-size:25px;color:' + 
						((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' + 
						'<span style="font-size:12px;color:silver">PH</span></div>'
					},
					tooltip: {
						valueSuffix: ' PH'
					}
				}];
				return options;
			}();
		});
		
		MessageBusService.getAquariumMetrics(function(metric) {
			_this.temperatureGraph.series[0].data.splice(0,1);
			_this.temperatureGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.temperature});
			
			_this.phGraph.series[0].data.splice(0,1);
			_this.phGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.ph});
		
			if (!_this.temperature.loading) {
				_this.temperature.series[0].data[0] = metric.temperature;
				_this.ph.series[0].data[0] = metric.ph;
			}
		
			$scope.$apply();
		});
		
		this.getTemperaturePastHour = function() {
			AquaMetricsService.rest.pastHour().$promise.then(function(data) {
				var sortedData = [];
	    		while (data.length >= 50) { 
	    			data.shift();
	    		}
		       angular.forEach(data, function(v, k) {
		    	   sortedData.push({x: v.occuredDatetime, y: v.temperature});
		       });
		       _this.temperatureGraph.series[0].data = sortedData;
		   });
		};
		
		this.getTemperaturePastDay = function() {
			AquaMetricsService.rest.pastDay().$promise.then(function(data) {
				var sortedData = [];
	    		while (data.length >= 50) { 
	    			data.shift();
	    		}
		       angular.forEach(data, function(v, k) {
		    	   sortedData.push({x: v.occuredDatetime, y: v.temperature});
		       });
		       _this.temperatureGraph.series[0].dataGrouping = {approximation: "average", enabled: true}
		       _this.temperatureGraph.series[0].data = sortedData;
			});
		};
		
		this.getPhPastHour = function() {
			AquaMetricsService.rest.pastHour().$promise.then(function(data) {
				var sortedData = [];
	    		while (data.length >= 50) { 
	    			data.shift();
	    		}
		       angular.forEach(data, function(v, k) {
		    	   sortedData.push({x: v.occuredDatetime, y: v.ph});
		       });
		       _this.phGraph.series[0].data = sortedData;
		   });
		};
		
		this.getPhPastDay = function() {
			AquaMetricsService.rest.pastDay().$promise.then(function(data) {
				var sortedData = [];
	    		while (data.length >= 50) { 
	    			data.shift();
	    		}
		       angular.forEach(data, function(v, k) {
		    	   sortedData.push({x: v.occuredDatetime, y: v.ph});
		       });
		       _this.phGraph.series[0].data = sortedData;
			});
		};
		
	}]);
	
})();


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
			[0.1, '#DF5353'], // red
        	[0.5, '#DDDF0D'], // yellow
        	[0.6, '#55BF3B'], // green
        	[0.7, '#DDDF0D'], // yellow
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
	title : {
		text: '',
		style: {
			display: 'none'
		}
	},
	loading : false
};