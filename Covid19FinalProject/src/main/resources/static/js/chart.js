$(document).ready(function(){
	var ctx = document.getElementById("myChart").getContext("2d");
	var myChart = new Chart(ctx, {
		type: "bar",
		data: {
			labels: [],
			datasets: [{
				labels: "일일 확진자 수",
				backgroundColor:"#bfdaf9",
				borderColor: "#80b6f4",
				pointBorderColor: "#80b6f4",
				pointBackgroundColor: "#80b6f4",
				pointHoverBackgroundColor: "#80b6f4",
				pointHoverBorderColor: "#80b6f4",
				fill: false,
				borderWidth: 4,
				data : []
			}]
		},
		options : {
			scales : {
				yAxes : [{
					ticks : {
						beginAtZero : true
					}
				}]
			}
		}
	});
	
	function onChange() {
		var chartLabels = [];
		var chartData = [];
		console.log($("#chart_category").val());
		console.log($("#chart_location").val());
		myChart.destroy();
		$.ajax({
			url: "./chart/getValues",
			data: {
				"type": $("#chart_category").val(),
				"location": $("#chart_location").val()
			},
			dataType: "json",
			success: function(data) {
				$.each(data, function(key, value) {
					chartLabels.push(value.result_date);
					chartData.push(value.increment_count);
				});
				if(chartLabels.length == 7) {
					myChart = new Chart(ctx, {
						type: "bar",
						data: {
							labels: chartLabels,
							datasets: [{
								label: "일일 확진자 수",
								backgroundColor:"#bfdaf9",
								borderColor: "#80b6f4",
								pointBorderColor: "#80b6f4",
								pointBackgroundColor: "#80b6f4",
								pointHoverBackgroundColor: "#80b6f4",
								pointHoverBorderColor: "#80b6f4",
								fill: false,
								borderWidth: 2,
								data : chartData
							}]
						},
						options : {
							scales : {
								yAxes : [{
									ticks : {
										beginAtZero : true
									}
								}]
							}
						}
					});
				}
				else if(chartLabels.length == 4) {
					myChart = new Chart(ctx, {
						type: "bar",
						data: {
							labels: chartLabels,
							datasets: [{
								label: "주간 확진자 수",
								backgroundColor:"#bfdaf9",
								borderColor: "#80b6f4",
								pointBorderColor: "#80b6f4",
								pointBackgroundColor: "#80b6f4",
								pointHoverBackgroundColor: "#80b6f4",
								pointHoverBorderColor: "#80b6f4",
								fill: false,
								borderWidth: 2,
								data : chartData
							}]
						},
						options : {
							scales : {
								yAxes : [{
									ticks : {
										beginAtZero : true
									}
								}]
							}
						}
					});
				}
				else if(chartLabels.length == 12) {
					myChart = new Chart(ctx, {
						type: "line",
						data: {
							labels: chartLabels,
							datasets: [{
								label: "월간 확진자 수",
								backgroundColor:"#bfdaf9",
								borderColor: "#80b6f4",
								pointBorderColor: "#80b6f4",
								pointBackgroundColor: "#80b6f4",
								pointHoverBackgroundColor: "#80b6f4",
								pointHoverBorderColor: "#80b6f4",
								fill: false,
								borderWidth: 2,
								data : chartData
							}]
						},
						options : {
							scales : {
								yAxes : [{
									ticks : {
										beginAtZero : true
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
	
	$("#chart_category").on("change", function(){
		onChange();
	});
	$("#chart_location").on("change", function(){
		onChange();
	});
});