'use strict';

(function() {
	
	app.controller('AquaMetricsController', ['$scope', 'AquaMetricsService', 'MessageBusService', function($scope, AquaMetricsService, MessageBusService) {
		var _this = this;
		$scope.moonLight = false;
		
		$scope.temperatureGraph = function() {
			var options = angular.copy(splineOptions);
			options.series = [ {
				name : 'Temperature',
//				data : temperatureData,
				data: [],
				tooltip: {
					valueDecimals: 1,
					valueSuffix: '°C'
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
		
		$scope.phGraph = function() {
			var options = angular.copy(splineOptions);
			options.series = [{
				name : 'PH',
//				data: phData,
				data: [],
				tooltip: {
					valueDecimals: 1,
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
		
		$scope.temperature = function() {
			var options = angular.copy(GaugeOptions);
			options.yAxis.max = 40;
			options.series = [{
				name: 'Temperature',
				data: [],
//				data: function() {
//					if (temperatureData == null || temperatureData[temperatureData.length - 1] == null) {
//						return 0;
//					}
//					var rawTemp = temperatureData[temperatureData.length-1].y;
//					return [Math.round(rawTemp * 100) / 100];
//				}(),
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
		
		$scope.ph = function() {
			var options = angular.copy(GaugeOptions);
			options.yAxis.max = 10;
			options.series = [{
				name: 'PH',
				data: [],
//				data: function() {
//					if (phData == null || phData[phData.length -1] == null) {
//						return 0;
//					}
//					var rawPh = phData[phData.length -1].y;
//					return [Math.round(rawPh * 100) / 100];
//				}(),
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

		
		AquaMetricsService.rest.pastHour().$promise.then(function(data) {
			_this.applyDataset(data);
		});
		
		var aquaMetricsPromise = MessageBusService.subscribeToAquaMetrics();
		aquaMetricsPromise.then(null, null, function(metric) {
			
			$scope.moonLight = metric.moonLightState;
			
			if (!$scope.temperatureGraph.loading) {
				if ($scope.temperatureGraph.series[0].data.length >= 3600) {
					$scope.temperatureGraph.series[0].data.splice(0,1);
				}
				$scope.temperatureGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.temperature});
			}

			if (!$scope.phGraph.loading) {
				if ($scope.phGraph.series[0].data.length >= 3600) {
					$scope.phGraph.series[0].data.splice(0,1);
				}
				$scope.phGraph.series[0].data.push({x:metric.occuredDatetime, y:metric.ph});
			}
			
			if (!$scope.temperature.loading) {
				$scope.temperature.series[0].data[0] = metric.temperature;
			}
			
			if (!$scope.ph.loading) {
				$scope.ph.series[0].data[0] = metric.ph;
			}
			
		});
		
		this.applyDataset = function(data) {
			 var temperatureData = [];
             var phData = [];
             angular.forEach(data, function(v, k) {
                     temperatureData.push({x: v.occuredDatetime, y: v.temperature});
                     phData.push({x: v.occuredDatetime, y: v.ph});
             });
             temperatureData.concat($scope.temperatureGraph.series[0].data);
             phData.concat($scope.phGraph.series[0].data);
             $scope.temperatureGraph.series[0].data = temperatureData;
             $scope.phGraph.series[0].data = phData;
		}
		
		$scope.pastHour = function() {
			AquaMetricsService.rest.pastHour().$promise.then(function(data) {
				_this.applyDataset(data);
			});
		};
		$scope.pastDay = function() {
			AquaMetricsService.rest.pastDay().$promise.then(function(data) {
				_this.applyDataset(data);
			});
		};
		$scope.pastWeek = function() {
			AquaMetricsService.rest.pastWeek().$promise.then(function(data) {
				_this.applyDataset(data);
			});
		};
		$scope.pastMonth = function() {
			AquaMetricsService.rest.pastMonth().$promise.then(function(data) {
				_this.applyDataset(data);
			});
		};
		
		$scope.toggleMoonLight = function() {
			AquaMetricsService.rest.toggleMoonLight().$promise.then(function(data) {
				console.log(data);
			});
		};

	}]); // controller end.
	
	
})();


var GaugeOptions = {
	options: {
        chart: {
            type: 'solidgauge'
        },
        pane: {
	    	center: ['50%', '50%'],
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
        text : null,
    },
    loading: false
};

var splineOptions = {
	credits : {
		enabled : false
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
	        text : null,
	    }
	},
	xAxis : {
		type : 'datetime',
		title : {
			text : 'Date'
		}
	},
	loading : false,
//	useHighStocks : true
};