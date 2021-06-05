$(document).ready(function() {
	$('.footer_a').on('click', function(e) {
		var $clicked = $(e.target);
		var username = $clicked.text();
		var win = window.open('http://github.com/' + username, '_blank');
		win.focus();
	});
});//ready End