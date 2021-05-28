<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="chart_section">
	<select id="chart_category">
		<option value="일별" selected>일별</option>
		<option value="주별">주별</option>
		<option value="월별">월별</option>
	</select>
	<select id="chart_location">
		<option value="전체" selected>전체</option>
		<option value="서울">서울</option>
		<option value="부산">부산</option>
		<option value="인천">인천</option>
		<option value="대구">대구</option>
		<option value="광주">광주</option>
		<option value="대전">대전</option>
		<option value="울산">울산</option>
		<option value="세종">세종</option>
		<option value="경기">경기</option>
		<option value="강원">강원</option>
		<option value="충북">충북</option>
		<option value="충남">충남</option>
		<option value="경북">경북</option>
		<option value="경남">경남</option>
		<option value="전북">전북</option>
		<option value="전남">전남</option>
		<option value="제주">제주</option>
		<option value="검역">검역</option>
	</select>
	<canvas class="chart_area" id="chart_area"></canvas>
</div>