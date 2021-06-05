$(document).ready(function() {
	// 확진자 현황 갱신 함수
	function boardOnChange() {
		// 실시간 확진자 수 갱신
		$.ajax({
			url: './board/getLiveValue',
			data: {
				'location': $('#board_select_location').text()
			},
			method: 'POST',
			dataType: 'JSON',
			success: function(data) {
				$('#board_live_count').html(data.live_count + "↑"); // 실시간 확진자 수
			}
		});
		// 누적 확진자, 완치자, 사망자 수 갱신
		$.ajax({
			url: './board/getResultValue',
			data: {
				'location': $('#board_select_location').text()
			},
			method: 'POST',
			dataType: 'JSON',
			success: function(data) {
				$('#board_total_count').html(data.total_count.toLocaleString('ko-KR')); // 누적 확진자 수
				$('#board_increment_count').html(data.increment_count.toLocaleString('ko-KR') + "↑"); // 전일 대비 증가한 확진자 수
				$('#board_clear_count').html(data.clear_count.toLocaleString('ko-KR')); // 누적 완치자 수
				$('#board_compare_clear_count').html(data.compare_clear_count.toLocaleString('ko-KR') + "↑"); // 전일 대비 증가한 완치자 수
				$('#board_death_count').html(data.death_count.toLocaleString('ko-KR')); // 누적 사망자 수
				$('#board_compare_death_count').html(data.compare_death_count.toLocaleString('ko-KR') + "↑"); // 전일 대비 증가한 사망자 수
			}
		});
	}
	
	boardOnChange(); // 페이지 로딩 후 최초 갱신

	$(".board_select_box .dropdown img.flag").addClass("flagvisibility");
	
	// dropbox 클릭
	$(".board_select_box .dropdown dt a").click(function(e) {
		e.preventDefault();
		$(".board_dropdown_location").toggle(); // 리스트 on/off
	});
	
	// dropbox list 클릭
	$(".board_select_box .dropdown dd ul li a").click(function(e) {
		e.preventDefault();
		var text = $(this).html(); // 선택한 리스트의 값
		$(".board_select_location").html(text); // 버튼 값 변경
		$(".board_dropdown_location").hide(); // 리스트 off
		boardOnChange(); // 정보 갱신
	});
	
	// 페이지 클릭
	$(document).on('click', function(e) {
		e.preventDefault();
		var $clicked = $(e.target);
		// 리스트를 클릭한 것이 아닐 경우 리스트 닫기
		if (!$clicked.parents().hasClass("board_select_box"))
			$(".board_dropdown_location").hide();
	});

	$(".board_select_box .dropdown img.flag").toggleClass("flagvisibility");
	
	// 버튼의 값이 바뀌었을 경우 정보 갱신
	$('#board_select_location').on('change', function() {
		boardOnChange();
	});
}); // ready end