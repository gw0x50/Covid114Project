<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="board_section">
	<select id="board_location">
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
	<div class="board_live_count_box">
		<div class="board_live_title">실시간 확진자</div>
		<div id="board_live_count" class="board_live_count">0</div>	
	</div>
	<div class="board_total_count_box">
		<div class="board_total_title">누적 확진자</div>
		<div id="board_total_count" class="board_total_count">0</div>	
		<div id="board_increment_count" class="board_increment_count">0</div>	
	</div>
	<div class="board_clear_count_box">
		<div class="board_clear_title">누적 완치자</div>
		<div id="board_clear_count" class="board_clear_count">0</div>
		<div id="board_compare_clear_count" class="board_compare_clear_count">0</div>	
	</div>
	<div class="board_death_count_box">
		<div class="board_death_title">누적 사망자</div>
		<div id="board_death_count" class="board_death_count">0</div>
		<div id="board_compare_death_count" class="board_compare_death_count">0</div>
	</div>			
</div>