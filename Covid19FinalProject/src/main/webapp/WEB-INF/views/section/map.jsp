<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.multi.covid.domain.CenterVO" %>



   <div class="map_title">
      <h1>지도</h1>
   </div>
   <div class="map" id="map" ></div>

<script>
var markers = []
var infoWindows = []

var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5666805, 126.9784147),
    zoom: 10,
    mapTypeId: naver.maps.MapTypeId.NORMAL
});
 // 위치정보 없을시 기본 위치 지정

<%
for(int i=0;i<((ArrayList)request.getAttribute("allcenter")).size();i++){
CenterVO vo = (CenterVO)((ArrayList)request.getAttribute("allcenter")).get(i); 
%> //백신접종센터 정보 받아오기
var lat = <%=vo.getLat()%>
var lng = <%=vo.getLng()%>
var city_name = "<%=vo.getLocation()%>"
var center_name = "<%=vo.getCenter_name()%>"
var address = "<%=vo.getAddress()%>"
var zipcode = "<%=vo.getZip_code()%>"
var facility_name = "<%=vo.getFacility_name()%>"
var phone_number = "<%=vo.getPhone_number()%>"
//자바스크립트용 변수로 저장

var marker = new naver.maps.Marker({
	  position: new naver.maps.LatLng(lat,lng),
	  map: map
});
//백신정보센터 위도 경도 좌표로 마커 표시하기	

var contentStrings = [
    '<div class="iw_inner">',
    '   <h3>'+center_name+'</h3>',
    '   <h3>'+facility_name+'</h3>',
    '   <p>주소 : '  +address+'<br />',
    '      전화번호 : ' +phone_number+'<br />',
    '   </p>',
    '</div>'
].join('');
//클릭했을 때 뜨게 할 내용 입력

var infoWindow = new naver.maps.InfoWindow({
content: contentStrings
});
//클릭시 뜨는 창 설정 

markers.push(marker);
infoWindows.push(infoWindow);



<%}
%>
//다수의 이벤트 핸들러 설정
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
	}

	for (var i=0, ii=markers.length; i<ii; i++) {
	    naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
	}
	


var infowindow1 =new naver.maps.InfoWindow()
function onSuccessGeolocation(position) {
    var location = new naver.maps.LatLng(position.coords.latitude,
                                         position.coords.longitude);

    map.setCenter(location); // 얻은 좌표를 지도의 중심으로 설정합니다.
    map.setZoom(13); // 지도의 줌 레벨을 변경합니다.

    infowindow1.setContent('<div style="padding:20px;">  현재 내 위치 </div>');
    var marker1 = new naver.maps.Marker({
        position: new naver.maps.LatLng(position.coords.latitude,position.coords.longitude),
        map: map
    });
    infowindow1.open(map, location);
    console.log('Coordinates: ' + location.toString());
}

function onErrorGeolocation() {
    var center = map.getCenter();

    infowindow1.setContent('<div style="padding:20px;">' +
        '<h5 style="margin-bottom:5px;color:#f00;">Geolocation failed!</h5>'+ "latitude: "+ center.lat() +"<br />longitude: "+ center.lng() +'</div>');

    infowindow1.open(map, center);
}

$(window).on("load", function() {
    if (navigator.geolocation) {
       
        navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
    } else {
        var center = map.getCenter();
        infowindow1.setContent('<div style="padding:20px;"><h5 style="margin-bottom:5px;color:#f00;">Geolocation not supported</h5></div>');
        infowindow1.open(map, center);
    }
});
</script>

