	angular.module('angular-canv-gauge', []).directive('canvGauge', function() {
	return {
		restrict : 'A',
		scope : {
			value : '='
		},
		link : function(scope, element, attributes) {
			var init, onValueChanged, gaugeOptions, gauge;

			var gaugeOptions = {
				width: 150,
				height: 150,
				glow : true,
				animationDuration : 10,
				animationFn : 'bounce',
				units : '°C',
				title : false,
				minValue : -10,
				maxValue : 40,
				majorTicks : [ '-10', '0', '10', '20', '30', '40' ],
				minorTicks : 10,
				strokeTicks : true,
				highlights : [ {
					from : -10,
					to : 10,
					color : '#009'
				}, {
					from : 10,
					to : 25,
					color : '#090'
				}, {
					from : 25,
					to : 40,
					color : '#d00'
				} ]
			};

			init = function() {
				gaugeOptions.renderTo = element[0].id;
				return new Gauge(gaugeOptions);
			};

			onValueChanged = function(value) {
				if (gauge) {
					gauge.setValue(value);
					return gauge.draw();
				} else {
					gauge = init();
					gauge.setValue(value);
					return gauge;
				}
			};

			scope.$watch('value', onValueChanged, true);
		}
	};
});
