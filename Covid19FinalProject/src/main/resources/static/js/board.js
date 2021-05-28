$(document).ready(function() {
	function onChange() {
		console.log('test');
		console.log($('#board_select_location').text());
		$.ajax({
			url: './board/getLiveValue',
			data: {
				'location': $('#board_select_location').text()
			},
			dataType: 'json',
			success: function(data) {
				$('#board_live_count').html(data.live_count);
			}
		});
		$.ajax({
			url: './board/getResultValue',
			data: {
				'location': $('#board_select_location').text()
			},
			dataType: 'json',
			success: function(data) {
				$('#board_total_count').html(data.total_count);
				$('#board_increment_count').html(data.increment_count);
				$('#board_clear_count').html(data.clear_count);
				$('#board_compare_clear_count').html(data.compare_clear_count);
				$('#board_death_count').html(data.death_count);
				$('#board_compare_death_count').html(data.compare_death_count);
			}
		});
	}
	onChange();

	$(".board_select_box .dropdown img.flag").addClass("flagvisibility");

	$(".board_select_box .dropdown dt a").click(function() {
		$(".board_select_box .dropdown dd .board_dropdown_location").toggle();
	});

	$(".board_select_box .dropdown dd ul li a").click(function() {
		var text = $(this).html();
		$(".board_select_box .dropdown dt a .board_select_location").html(text);
		$(".board_select_box .dropdown dd .board_dropdown_location").hide();
		onChange();
	});

	function getSelectedValue(id) {
		return $("#" + id).find("dt a #board_select_location.value").html();
	}
	
	$(document).bind('click', function(e) {
		var $clicked = $(e.target);
		if (!$clicked.parents().hasClass("board_select_box"))
			$(".board_select_box .dropdown dd .board_dropdown_location").hide();
	});

	$(".board_select_box .dropdown img.flag").toggleClass("flagvisibility");

	$('#board_select_location').on('change', function() {
		onChange();
	});
});
