angular.module('angular-gauge', []).directive('gauge', function() {
	return {
		restrict : 'A',
		scope : {
			value : '='
		},
		link : function(scope, element, attributes) {
			var createGauge, onValueChanged, gauge;

			createGauge = function(label, min, max) {
				var config = {
					size : 120,
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

				var gauge = new Gauge(element[0].id, config);
				gauge.render();

				return gauge;
			};

			onValueChanged = function(value) {
				if (gauge) {
					gauge.redraw(value);
					return gauge;
				} else {
					gauge = createGauge();
					return gauge;
				}
			};

			scope.$watch('value', onValueChanged, true);
		}
	};
});
