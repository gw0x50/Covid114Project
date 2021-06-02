package com.multi.covid.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.multi.covid.mapper.AISpeakerMapper;
import com.multi.covid.domain.CenterVO;
import com.multi.covid.domain.LiveVO;
import com.multi.covid.domain.ResultVO;

@Service
public class AISpeakerServiceImpl implements AISpeakerService {	
	
	@Autowired
	private AISpeakerService service;

	@Autowired
	private AISpeakerMapper mapper;
	
	@Override
	public List<ResultVO> getOneResult(String date) {
		return mapper.getOneResult(date);
	}

	@Override
	public List<ResultVO> getBetweenResult(HashMap<String, String> map) {
		return mapper.getBetweenResult(map);
	}

	@Override
	public List<ResultVO> getAllResult() {
		return mapper.getAllResult();
	}

	@Override
	public LiveVO getOneLive(String date) {
		return mapper.getOneLive(date);
	}

	@Override
	public List<CenterVO> getAllCenter() {
		return mapper.getAllCenter();
	}

	@Override
	public String patient(String day, String location) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String date;
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd ");
		LiveVO vo = null;
		List<ResultVO> list = null;
		int sum=0;
		JsonObject obj = new JsonObject();
		//covid_live
		String[] loc = {"seoul", "incheon", "gwangju", "daejeon", "daegu", "busan", "ulsan", "sejong",
						"gyeonggi", "gangwon", "chungbuk", "chungnam", "jeonbuk", "jeonnam", "gyeongbuk", 
						"gyeongnam", "jeju"};
		//covid_result
		String[] loc2 = {"서울", "인천", "광주", "대전", "대구", "부산", "울산", "세종", "경기", "강원", "충북", "충남", 
						"전북", "전남", "경북", "경남", "제주"};
		if(day.equals("today")) {
			try {
			date = format.format(cal.getTime());
			System.out.println("오늘: "+date);			
			vo = service.getOneLive(date); //오늘날짜 확진자수 받아오기
			vo.calSum();
			obj.addProperty("live_date", vo.getLive_date());
			if(location.equals("all")) { //전체지역 확진자								
				obj.addProperty("sum", vo.getSum());
			}
			else if(Arrays.asList(loc).contains(location)) {//location값이 배열에 있는지 확인(있으면 true 반환) 				
				obj.addProperty("sum", vo.getLocation(location));//특정지역 확진자수 조회
			}
			else {
				obj.addProperty("sum", "잘못된접근");
			}
			}catch(Exception e) {
				System.out.println(e.toString());
				return "결과값이 없습니다";
			}
		}
		else if(day.equals("yesterday")) {
			try {
			cal.add(Calendar.DATE, - 1);		
			date = format.format(cal.getTime());
			System.out.println("어제: "+date);
			list = service.getOneResult(date); //어제날짜 확진자수 받아오기
			System.out.println(list.get(0).getTotal_count());//데이터가 없으면 에러발생
			
			if(location.equals("all")) {
				for(ResultVO one : list) {
					if(one.getLocation().equals("합계")) 
						sum = one.getIncrement_count();//모든지역 확진자수				
				}
			}
			else if(Arrays.asList(loc2).contains(location)) {
				for(ResultVO one : list) {
					if(one.getLocation().equals(location)) 
						sum = one.getIncrement_count();//특정지역 확진자수				
				}
			}
			obj.addProperty("result_date", date);
			obj.addProperty("sum", sum);
			}catch(Exception e) {//전날 데이터가 아직 갱신되지 않은경우
				System.out.println(e.toString());
				cal.add(Calendar.DATE, - 2);
				date = format.format(cal.getTime());
				System.out.println("그제: "+date);
				list = mapper.getOneResult(date); //그제날짜 확진자수 받아오기
				for(ResultVO one : list) {		
					if(one.getLocation().equals("합계")) {
						System.out.println(one.getLocation());
						sum += one.getIncrement_count();
					}
				}
				obj.addProperty("result_date", date);
				obj.addProperty("sum", sum);					
			}				
		}
		else {
			return "잘못된접근";
		}
				
		return obj.toString();
	}		

	@Override
	public List<String> geolocation(String r1, String r2, String r3) {
		
		System.out.println("r1: "+r1+" r2: "+r2+" r3: "+r3);//ai스피커 접속지역
		List<CenterVO> vo = mapper.getAllCenter();				
		String si = (r1); //시
		String gu = (r2); //구
		String dong = (r3); //동
		String temp = "";
		List<String> result = new ArrayList<String>();
		boolean check = false;
		
		//가까운 선별진료소 파악
		for(CenterVO one : vo) {
			temp = one.getAddress();
			if(temp.contains(dong)) { //동 단위로 파악
				check = true;
				result.add(one.getFacility_name());
			}
		}
		if(!check) { //동에 선별진료소가 없는경우
			for(CenterVO one : vo) {
				temp = one.getAddress();
				if(temp.contains(gu)) { //구 단위로 파악
					check = true;
					result.add(one.getFacility_name());
				}
			}
		}
		else if(!check) { //구에 선별진료소가 없는경우
			for(CenterVO one : vo) {
				temp = one.getAddress();
				if(temp.contains(si)) { //시 단위로 파악
					check = true;
					result.add(one.getFacility_name());					
				}
			}
		}
		else { //주변에 선별진료소가 없는경우
			result.add("가까운 선별진료소가 없습니다");
		}
		
		System.out.println(check);
		for(String one : result) {
			System.out.println(one);
		}
		
		
		return result;
	}

	@Override
	public String weather(String lat, String lon) throws IOException, ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String date;
		SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
		date = format.format(cal.getTime());
		
		//위도 경도 -> 기상청 격자
		LatXLngY tmp = convertGRID_GPS(TO_GRID, Double.parseDouble(lat), Double.parseDouble(lon));	    
	    
		String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"; // 홈페이지에서 받은 키
		String serviceKey = "93e7xll6zOwEQ%2Bg77gneIDLoBftDp8%2B%2FmMoLwBhECh4Bc%2F8N0S47nGJT6KsniWqHi3MYxYXFhZ%2FM05uxMKuS%2FA%3D%3D";
		String nx = Integer.toString((int)tmp.x);	//위도
		String ny = Integer.toString((int)tmp.y);	//경도
		
		String pageNo = "1";
		String numOfRows = "10"; //조회할 행개수
		String baseDate = date;	//조회하고싶은 날짜
		System.out.println("date: "+baseDate);
		String baseTime = "1100";	//조회하고싶은 시간
		String type = "JSON";	//타입 xml, json 등등 ..
		
		StringBuilder urlBuilder = new StringBuilder(apiUrl);
        
		urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); //경도
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); //경도
		urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));	/* 타입 */
		urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
		urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
		urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        
        System.out.println(urlBuilder.toString());
        
        
        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());
        //어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        //System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result= sb.toString();
        System.out.println(result);
        
        // Json parser를 만들어 만들어진 문자열 데이터를 객체화 
 		JSONParser parser = new JSONParser(); 
 		JSONObject obj = (JSONObject) parser.parse(result); 
 		// response 키를 가지고 데이터를 파싱 
 		JSONObject parse_response = (JSONObject) obj.get("response"); 
 		// response 로 부터 body 찾기
 		JSONObject parse_body = (JSONObject) parse_response.get("body"); 
 		// body 로 부터 items 찾기 
 		JSONObject parse_items = (JSONObject) parse_body.get("items");

 		// items로 부터 itemlist 를 받기 
 		JSONArray parse_item = (JSONArray) parse_items.get("item");
 		String category;
 		JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용
 		// 카테고리와 값만 받아오기
 		String day="";
 		String time="";
 		String SKY=""; //하늘상태
 		String PTY=""; //강수형태
 		for(int i = 0 ; i < parse_item.size(); i++) {
 			weather = (JSONObject) parse_item.get(i);
 			Object fcstValue = weather.get("fcstValue");
 			Object fcstDate = weather.get("fcstDate");
 			Object fcstTime = weather.get("fcstTime");
 			//double형으로 받고싶으면 아래내용 주석 해제
 			//double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
 			category = (String)weather.get("category"); 
 			// 출력
 			if(!day.equals(fcstDate.toString())) {
 				day=fcstDate.toString();
 			}
 			if(!time.equals(fcstTime.toString())) {
 				time=fcstTime.toString();
 				System.out.println(day+"  "+time);
 			}
 			if(category.equals("SKY")){
 				int temp = Integer.parseInt(fcstValue.toString());
 				if(temp > 0 && temp < 3) {
 					SKY = "맑음";
 				}
 				else if(temp == 3) {
 					SKY = "구름";
 				}
 				else if(temp == 4) {
 					SKY = "흐림";
 				}
	 			System.out.print("\tcategory : "+ category);
	 			System.out.print(", fcst_Value : "+ fcstValue);
	 			System.out.print(", fcstDate : "+ fcstDate);
	 			System.out.println(", fcstTime : "+ fcstTime);
	 			System.out.println("sky: "+SKY);
 			}
 			if(category.equals("PTY")){
 				int temp = Integer.parseInt(fcstValue.toString());
 				switch(temp) {
 					case 0:
 						PTY = "없음";
 						break;
 					case 1:
 						PTY = "비";
 						break;
 					case 2:
 						PTY = "진눈개비";
 						break;
 					case 3:
 						PTY = "눈";
 						break;
 					case 4:
 						PTY = "소나기";
 						break;
 					case 5:
 						PTY = "빗방울";
 						break;
 					case 6:
 						PTY = "진눈개비";
 						break;
 					case 7:
 						PTY = "눈날림";
 						break; 					
 				}	 			
	 			System.out.println("pty: "+PTY);
 			}
 		}
        if(SKY.equals("맑음")) {
        	if(PTY.equals("없음")) {
        		result = "맑음 입니다";
        	}
        	else {
        		result = "맑고 "+PTY+"가 내립니다";
        	}
        }
        else if(SKY.equals("구름많음")) {
        	if(PTY.equals("없음")) {
        		result = "구름이 많이 있습니다";
        	}
        	else {
        		result = "구름이 많고 "+PTY+"가 내립니다";
        	}
        }
        else if(SKY.equals("흐림")) {
        	if(PTY.equals("없음")) {
        		result = "흐립니다";
        	}
        	else {
        		result = "흐리고 "+PTY+"가 내립니다";
        	}
        }
        else {
        	result = "날씨를 모르겠습니다";
        }
        /*
         * POP	강수확률	 %
         * PTY	강수형태	코드값
         * R06	6시간 강수량	범주 (1 mm)
         * REH	습도	 %
         * S06	6시간 신적설	범주(1 cm)
         * SKY	하늘상태	코드값
         * T3H	3시간 기온	 ℃
         * TMN	아침 최저기온	 ℃
         * TMX	낮 최고기온	 ℃
         * UUU	풍속(동서성분)	 m/s
         * VVV	풍속(남북성분)	 m/s
         */
		
		
		return result;
	}
	
	public static int TO_GRID = 0;
	public static int TO_GPS = 1;

	private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
	{
	    double RE = 6371.00877; // 지구 반경(km)
	    double GRID = 5.0; // 격자 간격(km)
	    double SLAT1 = 30.0; // 투영 위도1(degree)
	    double SLAT2 = 60.0; // 투영 위도2(degree)
	    double OLON = 126.0; // 기준점 경도(degree)
	    double OLAT = 38.0; // 기준점 위도(degree)
	    double XO = 43; // 기준점 X좌표(GRID)
	    double YO = 136; // 기1준점 Y좌표(GRID)

	    //
	    // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
	    //


	    double DEGRAD = Math.PI / 180.0;
	    double RADDEG = 180.0 / Math.PI;

	    double re = RE / GRID;
	    double slat1 = SLAT1 * DEGRAD;
	    double slat2 = SLAT2 * DEGRAD;
	    double olon = OLON * DEGRAD;
	    double olat = OLAT * DEGRAD;

	    double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	    sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
	    double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	    sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
	    double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
	    ro = re * sf / Math.pow(ro, sn);
	    LatXLngY rs = new LatXLngY();

	    if (mode == TO_GRID) {
	        rs.lat = lat_X;
	        rs.lng = lng_Y;
	        double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
	        ra = re * sf / Math.pow(ra, sn);
	        double theta = lng_Y * DEGRAD - olon;
	        if (theta > Math.PI) theta -= 2.0 * Math.PI;
	        if (theta < -Math.PI) theta += 2.0 * Math.PI;
	        theta *= sn;
	        rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
	        rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
	    }
	    else {
	        rs.x = lat_X;
	        rs.y = lng_Y;
	        double xn = lat_X - XO;
	        double yn = ro - lng_Y + YO;
	        double ra = Math.sqrt(xn * xn + yn * yn);
	        if (sn < 0.0) {
	            ra = -ra;
	        }
	        double alat = Math.pow((re * sf / ra), (1.0 / sn));
	        alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

	        double theta = 0.0;
	        if (Math.abs(xn) <= 0.0) {
	            theta = 0.0;
	        }
	        else {
	            if (Math.abs(yn) <= 0.0) {
	                theta = Math.PI * 0.5;
	                if (xn < 0.0) {
	                    theta = -theta;
	                }
	            }
	            else theta = Math.atan2(xn, yn);
	        }
	        double alon = theta / sn + olon;
	        rs.lat = alat * RADDEG;
	        rs.lng = alon * RADDEG;
	    }
	    return rs;
	}

	class LatXLngY
	{
	    public double lat;
	    public double lng;

	    public double x;
	    public double y;

	}
	
}
