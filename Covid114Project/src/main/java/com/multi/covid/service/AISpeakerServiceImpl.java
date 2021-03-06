package com.multi.covid.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
	private AISpeakerMapper mapper;

	// 실시간 또는 누적 확진자 현황 조회
	@Override
	public String getPatient(String day, String location) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String date;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
		LiveVO vo = null;
		List<ResultVO> list = null;
		int sum = 0;
		JsonObject obj = new JsonObject();

		// covid_live
		String[] liveLoc = { "seoul", "incheon", "gwangju", "daejeon", "daegu", "busan", "ulsan", "sejong", "gyeonggi", "gangwon", "chungbuk",
				"chungnam", "jeonbuk", "jeonnam", "gyeongbuk", "gyeongnam", "jeju" };
		// covid_result
		String[] resultLoc = { "서울", "인천", "광주", "대전", "대구", "부산", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주" };

		// 오늘 확진자 수 조회
		if (day.equals("today")) {
			try {
				date = format.format(cal.getTime());				
				vo = mapper.getOneLive(date); // 오늘 날짜 확진자 수 받아오기
				vo.calSum();
				obj.addProperty("live_date", vo.getLive_date());
				if (location.equals("all")) { // 전체 지역 확진자								
					obj.addProperty("sum", vo.getSum());
				}
				else if (Arrays.asList(liveLoc).contains(location)) {// location 값이 배열에 있는지 확인(있으면 true 반환) 				
					obj.addProperty("sum", vo.getLocation(location));// 특정 지역 확진자 수 조회
				}
				else {
					obj.addProperty("sum", "잘못된 접근");
				}
			}
			catch (Exception e) {				
				return "결과값이 없습니다";
			}
		}
		// 어제 확진자 수 조회
		else if (day.equals("yesterday")) {
			try {
				cal.add(Calendar.DATE, -1);
				date = format.format(cal.getTime());				
				list = mapper.getOneResult(date); // 어제 날짜 확진자 수 받아오기
				list.get(0).getTotal_count();// 데이터가 없으면 에러 발생

				// 모든 지역 확진자 수 합계 조회
				if (location.equals("all")) {
					for (ResultVO one : list) {
						if (one.getLocation().equals("합계")) sum = one.getIncrement_count();
					}
				}
				// 특정 지역 확진자 수 합계 조회
				else if (Arrays.asList(resultLoc).contains(location)) {
					for (ResultVO one : list) {
						if (one.getLocation().equals(location)) sum = one.getIncrement_count();// 특정 지역 확진자 수 조회			
					}
				}
				
				obj.addProperty("result_date", date);
				obj.addProperty("sum", sum);
			}
			catch (Exception e) {// 전날 데이터가 아직 갱신되지 않은 경우				
				cal.add(Calendar.DATE, -2);
				date = format.format(cal.getTime());
				list = mapper.getOneResult(date); // 그제 날짜 확진자 수 받아오기
				for (ResultVO one : list) {
					if (one.getLocation().equals("합계")) {
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

	// 현재 접속지역에서 가까운 백신센터를 반환
		@Override
		public String getVaccineCenter(String r1, String r2, String r3) {			
			List<CenterVO> vo = mapper.getAllCenter();
			String temp = "";
			List<String> result = new ArrayList<String>();
			boolean check = false;
			JsonObject obj = new JsonObject();
			// 가까운 백신센터 파악
			for (CenterVO one : vo) {
				temp = one.getAddress();						
				if (temp.contains(r3)) { // 동 단위로 파악
					check = true;					
					obj.addProperty("Facility", one.getFacility_name());
					obj.addProperty("Address", one.getAddress());
					break;
				}
			}
			if (!check) { // 동에 선별진료소가 없는 경우
				for (CenterVO one : vo) {
					temp = one.getAddress();
					if (temp.contains(r2)) { // 구 단위로 파악
						check = true;						
						obj.addProperty("Facility", one.getFacility_name());
						obj.addProperty("Address", one.getAddress());
						break;
					}
				}
			}
			else if (!check) { // 구에 선별진료소가 없는 경우
				for (CenterVO one : vo) {
					temp = one.getAddress();				
					if (temp.contains(r1)) { // 시 단위로 파악
						check = true;						
						obj.addProperty("Facility", one.getFacility_name());
						obj.addProperty("Address", one.getAddress());
						break;
					}
				}
			}
			else { // 주변에 선별진료소가 없는경우
				result.add("가까운 선별진료소가 없습니다");
			}			

			return obj.toString();
		}


	// 현재 접속지역의 위도, 경도를 기준으로 기상청 날씨 정보 반환
	@Override
	public String getWeather(String lat, String lon) throws IOException, ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String date;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		date = format.format(cal.getTime());

		// 위도, 경도 -> 기상청 격자 데이터 변환
		LatXLngY tmp = convertGRID_GPS(Double.parseDouble(lat), Double.parseDouble(lon));

		String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"; // 홈페이지에서 받은 키
		String serviceKey = "93e7xll6zOwEQ%2Bg77gneIDLoBftDp8%2B%2FmMoLwBhECh4Bc%2F8N0S47nGJT6KsniWqHi3MYxYXFhZ%2FM05uxMKuS%2FA%3D%3D";
		String nx = Integer.toString((int) tmp.x); // 위도
		String ny = Integer.toString((int) tmp.y); // 경도

		String pageNo = "1"; // 조회할 페이지수
		String numOfRows = "10"; // 조회할 행 개수
		String baseDate = date; // 조회하고 싶은 날짜
		String baseTime = "1100"; // 조회하고 싶은 시간
		String type = "JSON"; // 응답 데이터 타입 XML, JSON

		// 기상청에 get요청을 위한 urlBuilder
		StringBuilder urlBuilder = new StringBuilder(apiUrl);

		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); // 경도
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); // 경도
		urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); // 타입
		urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // 조회하고 싶은 날짜
		urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // 조회하고 싶은 시간 AM 02시부터 3시간 단위
		urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // 경도
		urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // 위도


		// GET방식으로 전송해서 파라미터 받아오기
		URL url = new URL(urlBuilder.toString());
		// 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}
		else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		
		rd.close();
		conn.disconnect();
		
		String result = sb.toString();

		// Json parser를 만들어 문자열 데이터를 객체화 
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
		JSONObject weather; // parse_item은 배열 형태이기 때문에 하나씩 데이터를 하나씩 가져올 때 사용
		// 카테고리와 값 받아오기
		String day = "";
		String time = "";
		String sky = ""; // 하늘 상태
		String pty = ""; // 강수 형태
		
		for (int i = 0; i < parse_item.size(); i++) {
			weather = (JSONObject) parse_item.get(i);
			Object fcstValue = weather.get("fcstValue");
			Object fcstDate = weather.get("fcstDate");
			Object fcstTime = weather.get("fcstTime");
			// double형으로 받고 싶으면 아래 내용 주석 해제
			// double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
			category = (String) weather.get("category");

			// 출력
			if (!day.equals(fcstDate.toString())) {
				day = fcstDate.toString();
			}
			if (!time.equals(fcstTime.toString())) {
				time = fcstTime.toString();
			}
			
			// 하늘 상태 파악
			if (category.equals("SKY")) {
				int temp = Integer.parseInt(fcstValue.toString());
				if (temp > 0 && temp < 3) {
					sky = "맑음";
				}
				else if (temp == 3) {
					sky = "구름";
				}
				else if (temp == 4) {
					sky = "흐림";
				}
				
			}
		
			// 강수 상태 파악
			if (category.equals("PTY")) {
				int temp = Integer.parseInt(fcstValue.toString());
				switch (temp) {
					case 0:
						pty = "없음";
						break;
					case 1:
						pty = "비";
						break;
					case 2:
						pty = "진눈개비";
						break;
					case 3:
						pty = "눈";
						break;
					case 4:
						pty = "소나기";
						break;
					case 5:
						pty = "빗방울";
						break;
					case 6:
						pty = "진눈개비";
						break;
					case 7:
						pty = "눈날림";
						break;
				}				
			}
		}
		if (sky.equals("맑음")) {
			if (pty.equals("없음")) {
				result = "맑음 입니다";
			}
			else {
				result = "맑고 " + pty + "가 내립니다";
			}
		}
		else if (sky.equals("구름많음")) {
			if (pty.equals("없음")) {
				result = "구름이 많이 있습니다";
			}
			else {
				result = "구름이 많고 " + pty + "가 내립니다";
			}
		}
		else if (sky.equals("흐림")) {
			if (pty.equals("없음")) {
				result = "흐립니다";
			}
			else {
				result = "흐리고 " + pty + "가 내립니다";
			}
		}
		else {
			result = "날씨를 모르겠습니다";
		}
		/*
		 * POP 강수확률 %
		 * PTY 강수형태	코드값
		 * R06 6시간 강수량	범주 (1 mm)
		 * REH 습도	%
		 * S06 6시간 신적설	범주 (1 cm)
		 * SKY 하늘상태	코드값
		 * T3H 3시간 기온 ℃
		 * TMN 아침 최저기온 ℃
		 * TMX 낮 최고기온 ℃
		 * UUU 풍속(동서성분) m/s
		 * VVV 풍속(남북성분) m/s
		 */

		return result;
	}

	// 위도, 경도 -> 기상청 격자 단위로 변환
	LatXLngY convertGRID_GPS(double lat_X, double lng_Y) {
		final double RE = 6371.00877; // 지구 반경(km)
		final double GRID = 5.0; // 격자 간격(km)
		final double SLAT1 = 30.0; // 투영 위도1(degree)
		final double SLAT2 = 60.0; // 투영 위도2(degree)
		final double OLON = 126.0; // 기준점 경도(degree)
		final double OLAT = 38.0; // 기준점 위도(degree)
		final double XO = 43; // 기준점 X좌표(GRID)
		final double YO = 136; // 기준점 Y좌표(GRID)
		final double PI = Math.PI;

		// LCC DFS 좌표 변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )

		double DEGRAD = PI / 180.0;

		double re = RE / GRID;
		double slat1 = SLAT1 * DEGRAD;
		double slat2 = SLAT2 * DEGRAD;
		double olon = OLON * DEGRAD;
		double olat = OLAT * DEGRAD;

		double sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
		sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
		double sf = Math.tan(PI * 0.25 + slat1 * 0.5);
		sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
		double ro = Math.tan(PI * 0.25 + olat * 0.5);
		ro = re * sf / Math.pow(ro, sn);
		LatXLngY rs = new LatXLngY();

		rs.lat = lat_X;
		rs.lng = lng_Y;
		double ra = Math.tan(PI * 0.25 + (lat_X) * DEGRAD * 0.5);
		ra = re * sf / Math.pow(ra, sn);
		double theta = lng_Y * DEGRAD - olon;
		if (theta > PI) theta -= 2.0 * PI;
		if (theta < -PI) theta += 2.0 * PI;
		theta *= sn;
		rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
		rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

		return rs;
	}

	//위도, 경도 저장용 클래스
	class LatXLngY {
		private double lat;
		private double lng;

		private double x;
		private double y;
	}
}