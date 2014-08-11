'use strict';

angular.module('angular-gauge', []).directive('gauge', function() {
    var uniqueId = 1;
	return {
		restrict : 'A',
		scope : {
			value : '=',
		},
		link : function(scope, element, attributes) {
			var createGauge = function (label, min, max) {
				var config = {
					size : 100,
					label : label,
					min : undefined != min ? min : 0,
					max : undefined != max ? max : 40,
					minorTicks : 5
				};

				var range = config.max - config.min;
				config.greenZones = [ {
					from : config.min + range * 0.0,
					to : config.min + range * 0.5
				} ];
				config.yellowZones = [ {
					from : config.min + range * 0.5,
					to : config.min + range * 0.75
				} ];
				config.redZones = [ {
					from : config.min + range * 0.75,
					to : config.max
				} ];
				
				element[0].id = "gauge".concat(uniqueId++);
				return new Gauge(element[0].id, config);
			};

			var valueChanged = function(value) {
				gauge.redraw(value);
				return gauge;
			};
			
			var gauge = createGauge(attributes.label);
			gauge.render(0);
						
			scope.$watch('value', valueChanged);			
		}
	};
});
