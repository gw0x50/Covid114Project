Kakao.init('acf10d854f2b80adc53f9cde98c6da22');

$(document).ready(function() {
	$('.chatbot_a').on('click', function() {
		Kakao.Channel.chat({
			channelPublicId: '_pEfMs'
		});
	});
}); // ready end