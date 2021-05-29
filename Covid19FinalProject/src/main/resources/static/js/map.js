$(document).ready(function(){
  
    var map = new naver.maps.Map('map_area', {
           center: new naver.maps.LatLng(37.5666805, 126.9784147),
           zoom: 9,
           mapTypeId: naver.maps.MapTypeId.NORMAL,
       }); 
       
       var infowindow1 =new naver.maps.InfoWindow();
   
   function onSuccessGeolocation(position) {
   var location = new naver.maps.LatLng(position.coords.latitude,
                                        position.coords.longitude);
   map.setCenter(location); 
   map.setZoom(13); 

   infowindow1.setContent('<div style="padding:20px;">  현재 내 위치 </div>');
   var marker1 = new naver.maps.Marker({
       position: new naver.maps.LatLng(position.coords.latitude,position.coords.longitude),
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

  

   function getAllCenter(){
       $.ajax({
           url:  "./map/getAllCentertemp",
           dataType: "json",
           success: function(allcenter) {
               map.setSize(new naver.maps.Size($('.map_title').width(),$('.map_title').width()));
               $.each(allcenter, function(key,value){
                   lat.push(value.lat);
                   lng.push(value.lng);
                   city_name.push(value.location);
                   center_name.push(value.center_name);
                   address.push(value.address);
                   zipcode.push(value.zipcode);
                   facility_name.push(value.facility_name);
                   phone_number.push(value.phone_number);
                   
                  
                 
                   var marker = new naver.maps.Marker({
         position: new naver.maps.LatLng(value.lat,value.lng),
         map: map
   });
   
   var contentStrings = [
       '<div class="iw_inner">',
       '   <h3>'+value.center_name+'</h3>',
       '   <h3>'+value.facility_name+'</h3>',
       '   <p>주소 : '  +value.address+'<br />',
       '      전화번호 : ' +value.phone_number+'<br />',
       '   </p>',
       '</div>'
   ].join('');
                   
   var infoWindow = new naver.maps.InfoWindow({
   content: contentStrings
   });
   
   markers.push(marker);
   infoWindows.push(infoWindow);
        
   
               });
                  for (var i=0, ii=markers.length; i<ii; i++) {
      					naver.maps.Event.addListener(markers[i], 'click', getClickHandler(i));
   					}
           }
             
       }); //ajax end
      
   } //getallCenter end
   getAllCenter();


   
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
    
 
  
   
   
   }); //ready 함수 end
   
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
   

