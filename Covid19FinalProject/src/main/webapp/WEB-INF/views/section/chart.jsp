<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<script>
$(document).ready(function(){
	
});

</script>
<div class="chart_section">
	<div class="chart_title">
		<select onchange="">
			<option selected>일별</option>
			<option>주별</option>
			<option>월별</option>
		</select>
		<select onchange="">
			<option selected>전체</option>
			<option>서울</option>
			<option>부산</option>
			<option>인천</option>
			<option>대구</option>
			<option>광주</option>
			<option>대전</option>
			<option>울산</option>
			<option>세종</option>
			<option>경기</option>
			<option>강원</option>
			<option>충북</option>
			<option>충남</option>
			<option>경북</option>
			<option>경남</option>
			<option>전북</option>
			<option>전남</option>
			<option>제주</option>
			<option>검역</option>
		</select>
		<canvas id="myChart" width="400" height="400"></canvas>
	</div>
	<div class="chart" id="chart"></div>
</div>