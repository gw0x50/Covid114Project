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
			responsive: true,
			maintainAspectRatio: false,
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero: true
					}
				}]
			}
		}
	});
	$('.chart_area').attr('width', $('.chart_title').width());
	$('.chart_area').attr('height', $('.chart_title').width());
	function onChange() {
		var chartLabels = [];
		var chartData = [];
		myChart.destroy();

		$.ajax({
			url: './chart/getValues',
			data: {
				'type': $('#chart_select_category').text(),
				'location': $('#chart_select_location').text()
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
							responsive: true,
							maintainAspectRatio: false,
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
							responsive: true,
							maintainAspectRatio: false,
							scales: {
								'A': {
									stacked: true,
									type: 'linear',
									position: 'right',
									min: 0,
									ticks: {
										stepSize: 80
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
							responsive: true,
							maintainAspectRatio: false,
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

	$(".chart_select_category_box .dropdown img.flag").addClass("flagvisibility");

	$(".chart_select_category_box .dropdown dt a").click(function(e) {
		e.preventDefault();
		$(".chart_select_category_box .dropdown dd .chart_dropdown_category").toggle();
	});

	$(".chart_select_category_box .dropdown dd ul li a").click(function(e) {
		e.preventDefault();
		var text = $(this).html();
		$(".chart_select_category_box .dropdown dt a .chart_select_category").html(text);
		$(".chart_select_category_box .dropdown dd .chart_dropdown_category").hide();
		onChange();
	});

	function getSelectedValue(id) {
		return $("#" + id).find("dt a #chart_select_category.value").html();
	}

	$(document).bind('click', function(e) {
		e.preventDefault();
		var $clicked = $(e.target);
		if (!$clicked.parents().hasClass("chart_select_category_box"))
			$(".chart_select_category_box .dropdown dd .chart_dropdown_category").hide();
	});

	$(".chart_select_category_box .dropdown img.flag").toggleClass("flagvisibility");

	$('#chart_select_category').on('change', function() {
		onChange();
	});


	$(".chart_select_location_box .dropdown img.flag").addClass("flagvisibility");

	$(".chart_select_location_box .dropdown dt a").click(function(e) {
		e.preventDefault();
		$(".chart_select_location_box .dropdown dd .chart_dropdown_location").toggle();
	});

	$(".chart_select_location_box .dropdown dd ul li a").click(function(e) {
		e.preventDefault();
		var text = $(this).html();
		$(".chart_select_location_box .dropdown dt a .chart_select_location").html(text);
		$(".chart_select_location_box .dropdown dd .chart_dropdown_location").hide();
		onChange();
	});

	function getSelectedValue(id) {
		return $("#" + id).find("dt a #chart_select_location.value").html();
	}

	$(document).bind('click', function(e) {
		var $clicked = $(e.target);
		if (!$clicked.parents().hasClass("chart_select_location_box"))
			$(".chart_select_location_box .dropdown dd .chart_dropdown_location").hide();
	});

	$(".chart_select_location_box .dropdown img.flag").toggleClass("flagvisibility");

	$('#chart_select_location').on('change', function() {
		onChange();
	});
});
