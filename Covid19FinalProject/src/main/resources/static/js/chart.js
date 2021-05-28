$(document).ready(function() {
	var ctx = document.getElementById('chart_area').getContext('2d');
	var myChart = new Chart(ctx, {
		type: 'bar',
		data: {
			labels: [],
			datasets: [{
				labels: '일일 확진자 수',
				backgroundColor: '#bfdaf9',
				borderColor: '#80b6f4',
				pointBorderColor: '#80b6f4',
				pointBackgroundColor: '#80b6f4',
				pointHoverBackgroundColor: '#80b6f4',
				pointHoverBorderColor: '#80b6f4',
				fill: false,
				borderWidth: 4,
				data: []
			}]
		},
		options: {
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero: true
					}
				}]
			}
		}
	});

	function onChange() {
		var chartLabels = [];
		var chartData = [];
		myChart.destroy();
		$.ajax({
			url: './chart/getValues',
			data: {
				'type': $('#chart_category').val(),
				'location': $('#chart_location').val()
			},
			dataType: 'json',
			success: function(data) {
				$.each(data, function(key, value) {
					chartLabels.push(value.result_date);
					chartData.push(value.increment_count);
				});
				if (chartLabels.length == 7) {
					myChart = new Chart(ctx, {
						type: 'bar',
						data: {
							labels: chartLabels,
							datasets: [{
								label: '일일 확진자 수',
								backgroundColor: '#bfdaf9',
								borderColor: '#80b6f4',
								barPercentage: 0.5,
								pointBorderColor: '#80b6f4',
								pointBackgroundColor: '#80b6f4',
								pointHoverBackgroundColor: '#80b6f4',
								pointHoverBorderColor: '#80b6f4',
								fill: false,
								borderWidth: 5,
								data: chartData
							}]
						},
						options: {
							scales: {
								yAxes: [{
									ticks: {
										beginAtZero: true
									}
								}]
							}
						}
					});
				}
				else if (chartLabels.length == 4) {
					var secondChartData = [];
					$.each(chartData, function(key, value) {
						secondChartData.push(value / 7);
					});
					myChart = new Chart(ctx, {
						data: {
							labels: chartLabels,
							datasets: [
								{
									yAxisID: 'A',
									label: '주간 평균 확진자 수',
									data: secondChartData,
									backgroundColor: '#bfdaf9',
									borderColor: '#80b6f4',
									borderWidth: 5,
									pointRadius: 7,
									pointBorderColor: '#80b6f4',
									pointBackgroundColor: '#80b6f4',
									pointHoverBackgroundColor: '#80b6f4',
									pointHoverBorderColor: '#80b6f4',
									fill: false,
									type: 'line',
								},
								{
									yAxisID: 'B',
									label: '주간 누적 확진자 수',
									data: chartData,
									barPercentage: 0.5,
									backgroundColor: '#999999',
									borderColor: '#666666',
									fill: false,
									borderWidth: 5,
									type: 'bar'
								}
							]
						},
						options: {
							scales: {
								'A': {
									stacked: true,
									type: 'linear',
									position: 'right',
									min: 0,
									ticks: {
										stepSize: 60
									}
								},
								'B': {
									type: 'linear',
									position: 'left',
									min: 0,
									ticks: {
										stepSize: 400
									}
								}
							}
						},
					});
				}
				else if (chartLabels.length == 12) {
					myChart = new Chart(ctx, {
						type: 'line',
						data: {
							labels: chartLabels,
							datasets: [{
								label: '월간 확진자 수',
								backgroundColor: '#bfdaf9',
								borderColor: '#80b6f4',
								borderWidth: 5,
								pointRadius: 5,
								pointBorderColor: '#80b6f4',
								pointBackgroundColor: '#80b6f4',
								pointHoverBackgroundColor: '#80b6f4',
								pointHoverBorderColor: '#80b6f4',
								fill: false,
								data: chartData
							}]
						},
						options: {
							scales: {
								yAxes: [{
									ticks: {
										beginAtZero: true
									}
								}]
							}
						}
					});
				}
			}
		});
	}
	onChange();

	$('#chart_category').on('change', function() {
		onChange();
	});
	$('#chart_location').on('change', function() {
		onChange();
	});
});