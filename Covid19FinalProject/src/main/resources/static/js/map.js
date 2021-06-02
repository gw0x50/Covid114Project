$(document).ready(function() {
	var markers = [];
	var infoWindows = [];
	var contentStrings = [];
	var lat = [];
	var lng = [];
	var city_name = [];
	var center_name = [];
	var address = [];
	var zipcode = [];
	var facility_name = [];
	var phone_number = [];
	var locallat = 37.5666805
	var locallng = 126.9784147;

	var infoWindowlocal = new naver.maps.InfoWindow({
		anchorSkew: true
	});


	var map = new naver.maps.Map('map_area', {
		center: new naver.maps.LatLng(37.5666805, 126.9784147),
		zoom: 10,
		mapTypeId: naver.maps.MapTypeId.NORMAL,
		mapTypeControl: true
	})

	var infowindow1 = new naver.maps.InfoWindow();


	map.setCursor('pointer');

	function onSuccessGeolocation(position) {
		var location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);

		locallat = position.coords.latitude;
		locallng = position.coords.longitude;
		getAllCenter();

		map.setCenter(location);
		map.setZoom(14);

		infowindow1.setContent('<div style="padding:20px;">  현재 내 위치 </div>');
		var marker1 = new naver.maps.Marker({
			position: new naver.maps.LatLng(position.coords.latitude, position.coords.longitude),
			map: map
		});
		infowindow1.open(map, location);
		console.log('Coordinates: ' + location.toString());
	} //onSuccessGeolocation end

	function getClickHandler(seq) {
		return function(e) {
			var marker = markers[seq],
				infoWindow = infoWindows[seq];

			if (infoWindow.getMap()) {
				infoWindow.close();
			} else {
				infoWindow.open(map, marker);
			}
		}
	} //getClickHandler end

	function getAllCenter() {
		$.ajax({
			url: "./map/getAllCenter",
			data: { 'lat': locallat, 'lng': locallng },
			dataType: "json",
			success: function(allcenter) {
				$.each(allcenter, function(key, value) {
					lat.push(value.lat);
					lng.push(value.lng);
					console.log(value.lat + ":" + value.lng);
					city_name.push(value.location);
					center_name.push(value.center_name);
					address.push(value.address);
					zipcode.push(value.zipcode);
					facility_name.push(value.facility_name);
					phone_number.push(value.phone_number);

					var marker = new naver.maps.Marker({
						position: new naver.maps.LatLng(value.lat, value.lng),
						map: map
					});

					var contentStrings = [
						'<div class="iw_inner">',
						'   <h3>' + value.center_name + '</h3>',
						'   <h3>' + value.facility_name + '</h3>',
						'   <p>주소 : ' + value.address + '<br />',
						'      전화번호 : ' + value.phone_number + '<br />',
						'   </p>',
						'</div>'
					].join('');

					var infoWindow = new naver.maps.InfoWindow({
						content: contentStrings
					});

					markers.push(marker);
					infoWindows.push(infoWindow);
				});
				for (var i = 0, ii = markers.length; i < ii; i++) {
					naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
				}
			}
		}); //ajax end
	} //getallCenter end

	function searchCoordinateToAddress(latlng) {

		infoWindowlocal.close();

		naver.maps.Service.reverseGeocode({
			coords: latlng,
			orders: [
				naver.maps.Service.OrderType.ADDR,
				naver.maps.Service.OrderType.ROAD_ADDR
			].join(',')
		}, function(status, response) {
			if (status === naver.maps.Service.Status.ERROR) {
				return alert('Something Wrong!');
			}
			
			infoWindowlocal.setContent([
            '<div style="padding:10px;min-width:200px;line-height:150%;">',
            '<h4 style="margin-top:5px;">클릭하신 장소입니다.</h4><br />',
            '</div>'
        	].join('\n'));
        	locallat = latlng.y;
        	locallng = latlng.x;
        	getAllCenter();


			infoWindowlocal.open(map, latlng);
		});
	}

	function searchAddressToCoordinate(address) {
		naver.maps.Service.geocode({
			query: address
		}, function(status, response) {
			if (status === naver.maps.Service.Status.ERROR) {
				return alert('Something Wrong!');
			}

			if (response.v2.meta.totalCount === 0) {
				return alert('지번주소 혹은 도로명 주소를 입력하세요');
			}

			var htmlAddresses = [],
				item = response.v2.addresses[0],
				point = new naver.maps.Point(item.x, item.y);
				locallat = item.y;
				locallng = item.x;
				getAllCenter();
			
			if (item.roadAddress) {
				htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
			}

			if (item.jibunAddress) {
				htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
			}

			if (item.englishAddress) {
				htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
			}

			infoWindowlocal.setContent([
            '<div style="padding:10px;min-width:200px;line-height:150%;">',
            '<h4 style="margin-top:5px;">검색하신 장소입니다.</h4><br />',
            '</div>'
        	].join('\n'));

			map.setCenter(point);
			infoWindowlocal.open(map, point);
		});
	}

	

		map.addListener('click', function(e) {
			searchCoordinateToAddress(e.coord);
			console.log(e.coord);
		});

		$('#map_address').on('keydown', function(e) {
			var keyCode = e.which;

			if (keyCode === 13) { // Enter Key
				searchAddressToCoordinate($('#map_address').val());
				
			}
		});

		$('#map_submit').on('click', function(e) {
			e.preventDefault();

			searchAddressToCoordinate($('#map_address').val());
			
		});

	function onErrorGeolocation() {
		var center = map.getCenter();
		getAllCenter();
		infowindow1.setContent('<div style="padding:20px;">  멀티캠퍼스 </div>');

		infowindow1.open(map, center);
	}

	$(window).on("load", function() {
		if (navigator.geolocation) {
			map.setSize(new naver.maps.Size($('.map_title').width(), $('.map_title').width()));
			navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
		}
		else {
			var center = map.getCenter();
			getAllCenter();
			infowindow1.setContent('<div style="padding:20px;">  멀티캠퍼스 </div>');

			infowindow1.open(map, center);
		}
	});




}); //ready 함수 end