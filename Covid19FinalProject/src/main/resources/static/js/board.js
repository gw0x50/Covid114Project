$(document).ready(function(){
	function onChange() {
		console.log($("#board_location").val());
		$.ajax({
			url: "./board/getLiveValue",
			data: {
				"location": $("#board_location").val()
			},
			dataType: "json",
			success: function(data) {
				$("#board_live_count").html(data.live_count);
			}
		});
		$.ajax({
			url: "./board/getResultValue",
			data: {
				"location": $("#board_location").val()
			},
			dataType: "json",
			success: function(data) {
				$("#board_total_count").html(data.total_count);
				$("#board_increment_count").html(data.increment_count);
				$("#board_clear_count").html(data.clear_count);
				$("#board_compare_clear_count").html(data.compare_clear_count);
				$("#board_death_count").html(data.death_count);
				$("#board_compare_death_count").html(data.compare_death_count);
			}
		});
	}
	onChange();
	
	$("#board_category").on("change", function(){
		onChange();
	});
});