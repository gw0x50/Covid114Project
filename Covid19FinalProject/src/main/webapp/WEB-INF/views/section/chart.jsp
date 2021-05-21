<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
window.onload = function(){
	const labels = [
		  'January',
		  'February',
		  'March',
		  'April',
		  'May',
		  'June',
	];
	const data = {
		labels: labels,
		datasets: [{
		    label: '일일 확진자 수',
		    backgroundColor: 'rgb(255, 99, 132)',
		    borderColor: 'rgb(255, 99, 132)',
		    data: [0, 10, 5, 2, 20, 30, 45],
		}]
	};	
	const config = {
		type: 'line',
		data,
		options: {}
	};
	var myChart = new Chart(
		    document.getElementById('myChart'),
		    config
	);
}
</script>
<div class="chart_section">
	<div class="chart_title">
		<canvas id="myChart" width="400" height="400"></canvas>
	</div>
	<div class="chart" id="chart"></div>
</div>