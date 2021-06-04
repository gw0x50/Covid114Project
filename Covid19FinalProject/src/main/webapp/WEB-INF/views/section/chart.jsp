<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="chart_section">
	<div class="chart_title">
		<div class="chart_title_text">확진자 차트</div>
		<div class="chart_select_wrap">
			<div class="chart_select_type_box">
				<dl class="dropdown">
					<dt>
						<a href="#" style="padding-top:1px; padding-bottom:4px;">
							<div class="text">기간</div>
							<div class="chart_select_type" id="chart_select_type">일일</div>
						</a>
					</dt>
					<dd>
						<ul class="chart_dropdown_type">
							<li><a href="#">일일</a></li>
							<li><a href="#">주간</a></li>
							<li><a href="#">월간</a></li>
						</ul>
					</dd>
				</dl>
			</div>
			<div class="chart_select_location_box">
				<dl class="dropdown">
					<dt>
						<a href="#" style="padding-top:1px; padding-bottom:4px;">
							<div class="text">지역</div>
							<div class="chart_select_location" id="chart_select_location">전체</div>
						</a>
					</dt>
					<dd>
						<ul class="chart_dropdown_location">
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
	</div>
	<div class="chart_wrap">
		<canvas class="chart_area" id="chart_area" ></canvas>
	</div>
</div>