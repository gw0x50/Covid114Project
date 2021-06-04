$(document).ready(function() {
	var markers = [];
	var infoWindows = [];
	var infoWindow;
	var seqnum;
	var closenum = 0;
	var locallat = 37.5666805
	var locallng = 126.9784147;
	var marker;
	var marker1;
	var marker_address;

	var infoWindowlocal = new naver.maps.InfoWindow({
		borderWidth: 0,
		disableAnchor: true,
		backgroundColor: 'transparent',
		pixelOffset: new naver.maps.Point(0, -35)
	}); //백신센터 정보를 보여줄 정보창 초기값 설정


	var map = new naver.maps.Map('map_area', {
		center: new naver.maps.LatLng(37.5012712, 127.0392959),
		zoom: 10,
		mapTypeId: naver.maps.MapTypeId.NORMAL,
		mapTypeControl: true
	}); //네이버 지도 초기값 설정

	var infowindow1 = new naver.maps.InfoWindow({
		borderWidth: 0,
		disableAnchor: true,
		backgroundColor: 'transparent',
		pixelOffset: new naver.maps.Point(0, -40)
	}); //현재위치를 표시할 정보창 초기값 설정

	function onSuccessGeolocation(position) {//현재 위치 위도경도를 받아와서 맵 설정
		var location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
		locallat = position.coords.latitude;
		locallng = position.coords.longitude;
		getAllCenter();
		map.setCenter(location);
		map.setZoom(14);
		infowindow1.setContent('<div class="map_local_window"><center><strong>현재 위치 주변 <br>백신센터를 표시하였습니다.</strong></center></div>');
		marker1 = new naver.maps.Marker({
			position: new naver.maps.LatLng(position.coords.latitude, position.coords.longitude),
			map: map,
			icon: {
				content: '<img src="resources/images/localpin.jpg" alt="" ' +
					'style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; ' +
					'-webkit-user-select: none; position: absolute; width: 22px; height: 35px; left: 0px; top: 0px;">',
				size: new naver.maps.Size(22, 35),
				anchor: new naver.maps.Point(11, 35)
			}
		});
		infowindow1.open(map, location);
	} //onSuccessGeolocation end

	function getClickHandler(seq) {//마커를 클릭했을 때 정보창 출력하는 함수
		return function(e) {
			var marker = markers[seq],
				infoWindow = infoWindows[seq];
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

	function getAllCenter() { //현재 위도경도 기준으로 DB에 있는 백신센터 정보 받아와서 지도에 표시하는 함수
		$.ajax({
			url: "./map/getAllCenter",
			data: { 'lat': locallat, 'lng': locallng },
			dataType: "json",
			type: "post",
			success: function(allcenter) {
				$.each(allcenter, function(key, value) {

					marker = new naver.maps.Marker({
						position: new naver.maps.LatLng(value.lat, value.lng),
						map: map
					});

					var contentStrings = [
						'<div class="map_center_window">',
						'<table class="map_center_table"><center>',
						'   <h3>' + value.facility_name + '</h3>',
						'<hr></center>',
						'<tr><td><strong>주소  </strong></td><td>' + value.address + '</td></tr><br>',
						' <tr><td><strong>전화번호</strong></td>  <td>' + value.phone_number + '</td></tr></table>',
						'</div>'
					].join('');
					infoWindow = new naver.maps.InfoWindow({
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
	} //getallCenter end

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
			marker_address = new naver.maps.Marker({
				position: new naver.maps.LatLng(latlng.y, latlng.x),
				map: map,
				icon: {
					content: '<img src="resources/images/pin_default.png" alt="" ' +
						'style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; ' +
						'-webkit-user-select: none; position: absolute; width: 22px; height: 35px; left: 0px; top: 0px;">',
					size: new naver.maps.Size(22, 35),
					anchor: new naver.maps.Point(11, 35)
				}
			});
			locallat = latlng.y;
			locallng = latlng.x;
			getAllCenter();
			infoWindowlocal.open(map, latlng);
		});
	}

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
			marker_address = new naver.maps.Marker({
				position: new naver.maps.LatLng(item.y, item.x),
				map: map,
				icon: {
					content: '<img src="resources/images/pin_default.png" alt="" ' +
						'style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; ' +
						'-webkit-user-select: none; position: absolute; width: 22px; height: 35px; left: 0px; top: 0px;">',
					size: new naver.maps.Size(22, 35),
					anchor: new naver.maps.Point(11, 35)
				}
			});
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
				'<div class="map_local_window">',
				'<center><strong>검색하신 주소 주변 <br>백신센터를 표시하였습니다.</strong></center>',
				'</div>'
			].join('\n'));
			map.setCenter(point);
			infoWindowlocal.open(map, point);
		});
	}

	map.addListener('click', function(e) { //지도 클릭시 좌표알려주는 함수
		console.log(closenum);
		if (closenum == 1) { //정보창이 떠있을 경우 정보창 종료
			infoWindows[seqnum].close();
			closenum = 0;
		} else {
			searchCoordinateToAddress(e.coord);
		}
	});

	$('#map_address').on('keydown', function(e) { //주소 입력후 Enter입력시 입력주소 주변 백신센터 정보 지도에 표시하게 하는 함수 호출
		var keyCode = e.which;
		if (keyCode === 13) { // Enter Key
			if ($('#map_address').val() != '') {
				searchAddressToCoordinate($('#map_address').val());
				$('#map_address').val('');
			} else { return alert("지번주소 혹은 도로명 주소를 입력하세요"); }
		}
	});

	$('#map_submit').on('click', function(e) { //주소 입력후 주소검색버튼 누를시 입력주소 주변 백신센터 정보 지도에 표시하게 하는 함수 호출
		e.preventDefault();
		if ($('#map_address').val() != '') {
			searchAddressToCoordinate($('#map_address').val());
			$('#map_address').val('');
		} else { return alert("지번주소 혹은 도로명 주소를 입력하세요"); }
	});

	function onErrorGeolocation() {//현재 위치 못 찾을시 기본 설정값 (멀티캠퍼스) 주변 백신센터 정보 지도에 표시해주는 함수
		var center = map.getCenter();
		getAllCenter();
		marker1 = new naver.maps.Marker({
			position: new naver.maps.LatLng(37.5666805, 126.9784147),
			map: map,
			icon: {
				content: '<img src="resources/images/localpin.jpg" alt="" ' +
					'style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; ' +
					'-webkit-user-select: none; position: absolute; width: 22px; height: 35px; left: 0px; top: 0px;">',
				size: new naver.maps.Size(22, 35),
				anchor: new naver.maps.Point(11, 35)
			}
		});
		infowindow1.setContent('<div class="map_local_window">   기본 설정위치(역삼멀티캠퍼스) </div>');
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
			marker1 = new naver.maps.Marker({
				position: new naver.maps.LatLng(37.5666805, 126.9784147),
				map: map,
				icon: {
					content: '<img src="resources/images/localpin.jpg" alt="" ' +
						'style="margin: 0px; padding: 0px; border: 0px solid transparent; display: block; max-width: none; max-height: none; ' +
						'-webkit-user-select: none; position: absolute; width: 22px; height: 35px; left: 0px; top: 0px;">',
					size: new naver.maps.Size(22, 35),
					anchor: new naver.maps.Point(11, 35)
				}
			});
			infowindow1.setContent('<div class="map_local_window">  기본 설정위치(역삼멀티캠퍼스) </div>');
			infowindow1.open(map, center);
		}
	});
}); //ready 함수 end