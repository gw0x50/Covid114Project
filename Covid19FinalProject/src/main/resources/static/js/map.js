$(window).on('load', function() {
	var markers = [];
	var infoWindows = [];
	var seqnum;
	var closenum = 0;

	var infoWindowlocal = new naver.maps.InfoWindow({
		borderWidth: 0,
		disableAnchor: true,
		backgroundColor: 'transparent',
		pixelOffset: new naver.maps.Point(0, -35)
	}); //백신센터 정보를 보여줄 정보창 초기값 설정

	var map = new naver.maps.Map('map_area', {
		center: new naver.maps.LatLng(37.5012624, 127.0397024), //지도 기본 위치 설정(역삼멀티캠퍼스)
		zoom: 14,
		mapTypeId: naver.maps.MapTypeId.NORMAL,
		mapTypeControl: true,
		size: new naver.maps.Size($('.map_title').width(), $('.map_title').width())
	}); //네이버 지도 초기값 설정

	function onSuccessGeolocation(position) {//현재 위치 위도경도를 받아와서 맵 설정
		var location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
		getLocalCenter(position.coords.latitude, position.coords.longitude);
		map.setCenter(location);
		map.setZoom(14);
		infoWindowlocal.setContent('<div class="map_local_window"><center><strong>현재 위치 주변 <br>백신센터를 표시하였습니다.</strong></center></div>');
		var marker = new naver.maps.Marker({
			position: new naver.maps.LatLng(position.coords.latitude, position.coords.longitude),
			map: map,
			icon: {
				content: '<img src="resources/images/localpin.jpg" alt="" class="map_local_img">',
				size: new naver.maps.Size(22, 35),
				anchor: new naver.maps.Point(11, 35)
			}
		});
		infoWindowlocal.open(map, location);
	} //onSuccessGeolocation end

	function getClickHandler(seq) {//마커를 클릭했을 때 정보창 출력하는 함수
		return function(e) {
			var marker = markers[seq], infoWindow = infoWindows[seq];
			if (infoWindow.getMap()) {
				infoWindow.close();
				closenum = 0;
			} else {
				infoWindow.open(map, marker);
				closenum = 1;
				seqnum = seq;
			}
		}
	} //getClickHandler end

	function getLocalCenter(lat, lng) { //현재 위도경도 기준으로 DB에 있는 백신센터 정보 받아와서 지도에 표시하는 함수
		$.ajax({
			url: "./map/getLocalCenter",
			data: { 'lat': lat, 'lng': lng },
			dataType: "json",
			type: "post",
			success: function(localCenter) {
				$.each(localCenter, function(key, value) {

					var marker = new naver.maps.Marker({
						position: new naver.maps.LatLng(value.lat, value.lng),
						map: map
					});

					var contentStrings = [
						'<div class="map_center_window">',
						'<table class="map_center_table"><center>',
						'<div class="map_facility_name"><strong>' + value.facility_name + '</strong></div>',
						'<hr></center>',
						'<tr><td><strong>주소  </strong></td><td>' + value.address + '</td></tr><br>',
						' <tr><td><strong>전화번호</strong></td>  <td>' + value.phone_number + '</td></tr></table>',
						'</div>'
					].join('');
					var infoWindow = new naver.maps.InfoWindow({
						content: contentStrings,
						borderWidth: 0,
						disableAnchor: true,
						backgroundColor: 'transparent',
						pixelOffset: new naver.maps.Point(0, 0)
					});
					markers.push(marker);
					infoWindows.push(infoWindow);
				});
				for (var i = 0, ii = markers.length; i < ii; i++) {
					naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
				}
			}
		}); //ajax end
	} //getLocalCenter end

	function searchCoordinateToAddress(latlng) { //클릭한 지점 위도 경도로 주변 백신센터 정보 받아와서 지도에 표시하는 함수
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
				'<div class="map_local_window">',
				'<center><strong>선택한 장소 주변 <br>백신센터를 표시하였습니다.</strong></center>',
				'</div>'
			].join('\n'));
			var marker = new naver.maps.Marker({
				position: new naver.maps.LatLng(latlng.y, latlng.x),
				map: map,
				icon: {
					content: '<img src="resources/images/pin_default.png" alt="" class="map_local_img">',
					size: new naver.maps.Size(22, 35),
					anchor: new naver.maps.Point(11, 35)
				}
			});
			getLocalCenter(latlng.y, latlng.x);
			infoWindowlocal.open(map, latlng);
		});
	}//searchCoordinateToAddress end

	function searchAddressToCoordinate(address) { //입력한 주소 주변 백신센터 정보 받아와서 지도에 표시하는 함수
		naver.maps.Service.geocode({
			query: address
		}, function(status, response) {
			if (status === naver.maps.Service.Status.ERROR) {//서버 문제시 오류처리
				return alert('Something Wrong!');
			}
			if (response.v2.meta.totalCount === 0) {//잘못된 주소 입력시 오류처리
				return alert('지번주소 혹은 도로명 주소를 정확히 입력하세요');
			}
			var htmlAddresses = [],
				item = response.v2.addresses[0],
				point = new naver.maps.Point(item.x, item.y);
			var marker = new naver.maps.Marker({
				position: new naver.maps.LatLng(item.y, item.x),
				map: map,
				icon: {
					content: '<img src="resources/images/pin_default.png" alt="" class="map_local_img">',
					size: new naver.maps.Size(22, 35),
					anchor: new naver.maps.Point(11, 35)
				}
			});
			getLocalCenter(item.y, item.x);
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
				'<div class="map_local_window">',
				'<center><strong>검색하신 주소 주변 <br>백신센터를 표시하였습니다.</strong></center>',
				'</div>'
			].join('\n'));
			map.setCenter(point);
			infoWindowlocal.open(map, point);
		});
	}//searchAddressToCoordinate end

	$('#map_address').on('keydown', function(e) { //주소 입력후 Enter입력시 입력주소 주변 백신센터 정보 지도에 표시하게 하는 함수 호출
		var keyCode = e.which;
		if (keyCode === 13) { // Enter Key
			if ($('#map_address').val() != '') {
				searchAddressToCoordinate($('#map_address').val());
				$('#map_address').val('');
			} else { return alert("지번주소 혹은 도로명 주소를 입력하세요"); }
		}
	});//$('#map_address').on('keydown', function(e)) end

	$('#map_submit').on('click', function(e) { //주소 입력후 주소검색버튼 누를시 입력주소 주변 백신센터 정보 지도에 표시하게 하는 함수 호출
		e.preventDefault();
		if ($('#map_address').val() != '') {
			searchAddressToCoordinate($('#map_address').val());
			$('#map_address').val('');
		} else { return alert("지번주소 혹은 도로명 주소를 입력하세요"); }
	});//$('#map_submit').on('click', function(e)) end

	function defaultLocation() {
		var center = map.getCenter();
		getLocalCenter(37.5012624, 127.0397024); //지도 기본 위치 설정(역삼멀티캠퍼스)
		var marker = new naver.maps.Marker({
			position: new naver.maps.LatLng(37.5012624, 127.0397024), //지도 기본 위치 설정(역삼멀티캠퍼스)
			map: map,
			icon: {
				content: '<img src="resources/images/localpin.jpg" alt="" class="map_local_img">',
				size: new naver.maps.Size(22, 35),
				anchor: new naver.maps.Point(11, 35)
			}
		});
		infoWindowlocal.setContent('<div class="map_local_window">   기본 설정위치<br>(역삼멀티캠퍼스) </div>');
		infoWindowlocal.open(map, center);
	}//defaultLocation end

	function onErrorGeolocation() {//현재 위치 못 찾을시 기본 설정값 (멀티캠퍼스) 주변 백신센터 정보 지도에 표시해주는 함수
		defaultLocation();
	}//onErrorGeolocation end

	map.addListener('click', function(e) { //지도 클릭시 좌표알려주는 함수
		if (closenum == 1) { //정보창이 떠있을 경우 정보창 종료
			infoWindows[seqnum].close();
			closenum = 0;
		} else {
			searchCoordinateToAddress(e.coord);
		}
	});//map.addListener('click', function(e)) end

	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
	}
	else {
		defaultLocation();
	}
	$(window).resize(function() {
		map.setSize(new naver.maps.Size($('.map_title').width(), $('.map_title').width()));
	});

}); //window on load end