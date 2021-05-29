<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="board_section">
	<div class="board_title">
		<div class="board_title_text">확진자 현황</div>
		<div class="board_select_box">
			<dl class="dropdown">
				<dt>
					<a href="#" style="padding-top:2px; padding-bottom:3px;">
						<div class="text">지역</div>
						<div class="board_select_location" id="board_select_location">전체</div>
					</a>
				</dt>
				<dd>
					<ul class="board_dropdown_location">
						<li><a href="#">전체</a></li>
						<li><a href="#">서울</a></li>
						<li><a href="#">부산</a></li>
						<li><a href="#">인천</a></li>
						<li><a href="#">대구</a></li>
						<li><a href="#">광주</a></li>
						<li><a href="#">대전</a></li>
						<li><a href="#">울산</a></li>
						<li><a href="#">세종</a></li>
						<li><a href="#">경기</a></li>
						<li><a href="#">강원</a></li>
						<li><a href="#">충북</a></li>
						<li><a href="#">충남</a></li>
						<li><a href="#">경북</a></li>
						<li><a href="#">경남</a></li>
						<li><a href="#">전북</a></li>
						<li><a href="#">전남</a></li>
						<li><a href="#">제주</a></li>
						<li><a href="#">검역</a></li>
					</ul>
				</dd>
			</dl>
		</div>
	</div>
	<div class="board_count_box">
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
</div>