$(document).ready(function() {
	var ctx = document.getElementById('chart_area').getContext('2d');

	// 영역을 잡기 위한 빈 차트 생성
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

	// chart_area 영역 크기 설정
	$('.chart_area').attr('width', $('.chart_title').width());
	$('.chart_area').attr('height', $('.chart_title').width());

	// 확진자 차트 갱신 함수
	function chartOnChange() {
		var chartLabels = [];
		var chartData = [];
		myChart.destroy(); // 기존 차트 초기화

		var type = $('#chart_select_type').text();
		if (type == '일일') {
			$.ajax({
				url: './chart/get7DaysResult',
				data: {
					'location': $('#chart_select_location').text()
				},
				method: 'POST',
				dataType: 'JSON',
				success: function(data) {
					$.each(data, function(key, value) {
						chartLabels.push(value.result_date);
						chartData.push(value.increment_count);
					});
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
							interaction: {
								intersect: false,
								mode: 'index'
							},
							responsive: true,
							maintainAspectRatio: false,
							scales: {
								xAxes: {
									grid: {
										display: false,
										drawBorder: false,
										drawOnChartArea: false,
										drawTicks: false
									}
								},
								yAxes: {
									ticks: {
										beginAtZero: true
									},
									grid: {
										display: false,
										drawBorder: false,
										drawOnChartArea: false,
										drawTicks: false
									}
								}
							}
						},
						plugins: [{
							afterDatasetsDraw: function(chart) {
								var ctx = chart.ctx;
								chart.data.datasets.forEach(function(dataset, i) {
									var meta = chart.getDatasetMeta(i);
									if (!meta.hidden) {
										meta.data.forEach(function(element, index) {
											// Draw the text in black, with the specified font
											ctx.fillStyle = 'rgb(0, 0, 0)';
											var fontSize = 15;
											var fontStyle = 'normal';
											var fontFamily = 'Helvetica Neue';
											ctx.font = Chart.helpers.fontString(fontSize, fontStyle, fontFamily);
											// Just naively convert to string for now
											var dataString = dataset.data[index].toString();
											// Make sure alignment settings are correct
											ctx.textAlign = 'center';
											ctx.textBaseline = 'middle';
											var padding = 5;
											var position = element.tooltipPosition();
											ctx.fillText(dataString, position.x, position.y - (fontSize / 2) - padding);
										});
									}
								});
							}
						}]
					});
				}
			});
		}
		else if (type == '주간') {
			$.ajax({
				url: './chart/get4WeeksResult',
				data: {
					'location': $('#chart_select_location').text()
				},
				method: 'POST',
				dataType: 'JSON',
				success: function(data) {
					$.each(data, function(key, value) {
						chartLabels.push(value.result_date);
						chartData.push(value.increment_count);
					});
					var secondChartData = [];
					$.each(chartData, function(key, value) {
						secondChartData.push(Math.round(value / 7));
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
									type: 'line'
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
							interaction: {
								intersect: false,
								mode: 'index'
							},
							responsive: true,
							maintainAspectRatio: false,
							scales: {
								xAxes: {
									grid: {
										display: false,
										drawBorder: false,
										drawOnChartArea: false,
										drawTicks: false
									}
								},
								'A': {
									stacked: true,
									type: 'linear',
									position: 'right',
									min: 0,
									ticks: {
										stepSize: 80
									},
									grid: {
										display: false,
										drawBorder: false,
										drawOnChartArea: false,
										drawTicks: false
									}
								},
								'B': {
									type: 'linear',
									position: 'left',
									min: 0,
									ticks: {
										stepSize: 400
									},
									grid: {
										display: false,
										drawBorder: false,
										drawOnChartArea: false,
										drawTicks: false
									}
								}
							}
						}
					});
				}
			});
		}
		else if (type == '월간') {
			$.ajax({
				url: './chart/get12MonthsResult',
				data: {
					'location': $('#chart_select_location').text()
				},
				method: 'POST',
				dataType: 'JSON',
				success: function(data) {
					$.each(data, function(key, value) {
						chartLabels.push(value.result_date);
						chartData.push(value.increment_count);
					});

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
							interaction: {
								intersect: false,
								mode: 'index'
							},
							responsive: true,
							maintainAspectRatio: false,
							scales: {
								yAxes: {
									ticks: {
										beginAtZero: true
									},
									grid: {
										display: false,
										drawBorder: false,
										drawOnChartArea: false,
										drawTicks: false
									}
								}
							}
						}
					});
				}
			});
		}
	}

	chartOnChange(); // 페이지 로딩 후 최초 갱신

	// type 관련 함수
	$('.chart_select_type_box .dropdown img.flag').addClass('flagvisibility');

	$('.chart_select_type_box .dropdown img.flag').toggleClass('flagvisibility');

	// dropbox 클릭
	$('.chart_select_type_box .dropdown dt a').click(function(e) {
		e.preventDefault();
		$('.chart_dropdown_type').toggle(); // 리스트 on/off
	});

	// dropbox list 클릭
	$('.chart_select_type_box .dropdown dd ul li a').click(function(e) {
		e.preventDefault();
		var text = $(this).html(); // 선택한 리스트의 값
		$('.chart_select_type').html(text); // 버튼 값 변경
		$('.chart_dropdown_type').hide(); // 리스트 off
		chartOnChange(); // 정보 갱신
	});

	// 페이지 클릭
	$(document).bind('click', function(e) {
		e.preventDefault();
		var $clicked = $(e.target);
		// 리스트를 클릭한 것이 아닐 경우 리스트 닫기
		if (!$clicked.parents().hasClass('chart_select_type_box')) {
			$('.chart_dropdown_type').hide();
		}
	});

	// 버튼의 값이 바뀌었을 경우 정보 갱신
	$('#chart_select_type').on('change', function() {
		chartOnChange();
	});


	// location 관련 함수
	$('.chart_select_location_box .dropdown img.flag').addClass('flagvisibility');

	$('.chart_select_location_box .dropdown img.flag').toggleClass('flagvisibility');

	// dropbox 클릭
	$('.chart_select_location_box .dropdown dt a').click(function(e) {
		e.preventDefault();
		$('.chart_dropdown_location').toggle();
	});

	// dropbox list 클릭
	$('.chart_select_location_box .dropdown dd ul li a').click(function(e) {
		e.preventDefault();
		var text = $(this).html();
		$('.chart_select_location').html(text);
		$('.chart_dropdown_location').hide();
		chartOnChange();
	});

	// 페이지 클릭
	$(document).bind('click', function(e) {
		var $clicked = $(e.target);
		// 리스트를 클릭한 것이 아닐 경우 리스트 닫기
		if (!$clicked.parents().hasClass('chart_select_location_box')) {
			$('.chart_select_location_box .dropdown dd .chart_dropdown_location').hide();
		}

	});

	// 버튼의 값이 바뀌었을 경우 정보 갱신
	$('#chart_select_location').on('change', function() {
		chartOnChange();
	});
}); // ready end