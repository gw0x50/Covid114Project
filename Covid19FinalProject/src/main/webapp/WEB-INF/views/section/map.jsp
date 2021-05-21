<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <title>간단한 지도 표시하기</title>
    <script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=ugvsraegy7"></script>
</head>
<div class="map_section">
	<div class="map_title">
		<h1>지도</h1>
	</div>
	<div class="map" id="map" style="width:500px;height:400px;"></div>
</div>

<script>

var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5666805, 126.9784147),
    zoom: 10,
    mapTypeId: naver.maps.MapTypeId.NORMAL
});

var infowindow = new naver.maps.InfoWindow();
var marker = new naver.maps.Marker();
function onSuccessGeolocation(position) {
    var location = new naver.maps.LatLng(position.coords.latitude,
                                         position.coords.longitude);

    map.setCenter(location); // 얻은 좌표를 지도의 중심으로 설정합니다.
    map.setZoom(15); // 지도의 줌 레벨을 변경합니다.

    infowindow.setContent('<div style="padding:20px;">' + '현재 내 위치' +position.coords.latitude +':'+
            position.coords.longitude+ '</div>');
    marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(position.coords.latitude,position.coords.longitude),
        map: map
    });
    infowindow.open(map, location);
    console.log('Coordinates: ' + location.toString());
}

function onErrorGeolocation() {
    var center = map.getCenter();

    infowindow.setContent('<div style="padding:20px;">' +
        '<h5 style="margin-bottom:5px;color:#f00;">Geolocation failed!</h5>'+ "latitude: "+ center.lat() +"<br />longitude: "+ center.lng() +'</div>');

    infowindow.open(map, center);
}

$(window).on("load", function() {
    if (navigator.geolocation) {
        /**
         * navigator.geolocation 은 Chrome 50 버젼 이후로 HTTP 환경에서 사용이 Deprecate 되어 HTTPS 환경에서만 사용 가능 합니다.
         * http://localhost 에서는 사용이 가능하며, 테스트 목적으로, Chrome 의 바로가기를 만들어서 아래와 같이 설정하면 접속은 가능합니다.
         * chrome.exe --unsafely-treat-insecure-origin-as-secure="http://example.com"
         */
        navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
    } else {
        var center = map.getCenter();
        infowindow.setContent('<div style="padding:20px;"><h5 style="margin-bottom:5px;color:#f00;">Geolocation not supported</h5></div>');
        infowindow.open(map, center);
    }
});

var marker1 = new naver.maps.Marker({
    position: new naver.maps.LatLng(37.465,127.129),
    map: map
});
    
var contentString = [
    '<div class="iw_inner">',
    '   <h3>우리집명칭</h3>',
    '   <p>도시쓸거야 | 여기는 주소 쓸거고<br />',
    /* '       <img src="'+ HOME_PATH +'/img/example/hi-seoul.jpg" width="55" height="55" alt="서울시청" class="thumb" /><br />' */,
    '       02-120 | 전화번호쓸자리<br />',
    '       <a href="www.seoul.go.kr" target="_blank">도메인주소쓸자리</a>',
    '   </p>',
    '</div>'
].join('');

var infowindow1 = new naver.maps.InfoWindow({
content: contentString
});

naver.maps.Event.addListener(marker1, "click", function(e) {
if (infowindow1.getMap()) {
    infowindow1.close();
} else {
    infowindow1.open(map, marker1);
}
});

infowindow1.open(map, marker1);

var marker2 = new naver.maps.Marker({
    position: new naver.maps.LatLng(37.47,127.133),
    map: map
});
    
var contentString1 = [
    '<div class="iw_inner">',
    '   <h3>두번째마커</h3>',
    '   <p>도시쓸거야 | 여기는 주소 쓸거고<br />',
    /* '       <img src="'+ HOME_PATH +'/img/example/hi-seoul.jpg" width="55" height="55" alt="서울시청" class="thumb" /><br />' */,
    '       02-120 | 전화번호쓸자리<br />',
    '       <a href="www.seoul.go.kr" target="_blank">도메인주소쓸자리</a>',
    '   </p>',
    '</div>'
].join('');

var infowindow2 = new naver.maps.InfoWindow({
content: contentString1
});

naver.maps.Event.addListener(marker2, "click", function(e) {
if (infowindow2.getMap()) {
    infowindow2.close();
} else {
    infowindow2.open(map, marker2);
}
});

infowindow2.open(map, marker2);
</script>
</body>
</html>
