package com.multi.covid.domain;

import org.springframework.stereotype.Component;

@Component
public class LiveVO {
	private String live_date;
	private int seoul, incheon, gwangju, daejeon, daegu, busan, ulsan, sejong, gyeonggi, gangwon, chungbuk, chungnam, jeonbuk, jeonnam, gyeongbuk,
			gyeongnam, jeju, sum;

	public LiveVO() {
	}

	public LiveVO(String live_date, int seoul, int incheon, int gwangju, int daejeon, int daegu, int busan, int ulsan, int sejong, int gyeonggi,
			int gangwon, int chungbuk, int chungnam, int jeonbuk, int jeonnam, int gyeongbuk, int gyeongnam, int jeju) {
		this.live_date = live_date;
		this.seoul = seoul;
		this.incheon = incheon;
		this.gwangju = gwangju;
		this.daejeon = daejeon;
		this.daegu = daegu;
		this.busan = busan;
		this.ulsan = ulsan;
		this.sejong = sejong;
		this.gyeonggi = gyeonggi;
		this.gangwon = gangwon;
		this.chungbuk = chungbuk;
		this.chungnam = chungnam;
		this.jeonbuk = jeonbuk;
		this.jeonnam = jeonnam;
		this.gyeongbuk = gyeongbuk;
		this.gyeongnam = gyeongnam;
		this.jeju = jeju;
		this.sum = seoul + incheon + gwangju + daejeon + daegu + busan + ulsan + sejong + gyeonggi + gangwon + chungbuk + chungnam + jeonbuk + jeonnam
				+ gyeongbuk + gyeongnam + jeju;
	}

	public String getLive_date() {
		return live_date;
	}

	public void setLive_date(String live_date) {
		this.live_date = live_date;
	}

	public int getSeoul() {
		return seoul;
	}

	public void setSeoul(int seoul) {
		this.seoul = seoul;
	}

	public int getIncheon() {
		return incheon;
	}

	public void setIncheon(int incheon) {
		this.incheon = incheon;
	}

	public int getGwangju() {
		return gwangju;
	}

	public void setGwangju(int gwangju) {
		this.gwangju = gwangju;
	}

	public int getDaejeon() {
		return daejeon;
	}

	public void setDaejeon(int daejeon) {
		this.daejeon = daejeon;
	}

	public int getDaegu() {
		return daegu;
	}

	public void setDaegu(int daegu) {
		this.daegu = daegu;
	}

	public int getBusan() {
		return busan;
	}

	public void setBusan(int busan) {
		this.busan = busan;
	}

	public int getUlsan() {
		return ulsan;
	}

	public void setUlsan(int ulsan) {
		this.ulsan = ulsan;
	}

	public int getSejong() {
		return sejong;
	}

	public void setSejong(int sejong) {
		this.sejong = sejong;
	}

	public int getGyeonggi() {
		return gyeonggi;
	}

	public void setGyeonggi(int gyeonggi) {
		this.gyeonggi = gyeonggi;
	}

	public int getGangwon() {
		return gangwon;
	}

	public void setGangwon(int gangwon) {
		this.gangwon = gangwon;
	}

	public int getChungbuk() {
		return chungbuk;
	}

	public void setChungbuk(int chungbuk) {
		this.chungbuk = chungbuk;
	}

	public int getChungnam() {
		return chungnam;
	}

	public void setChungnam(int chungnam) {
		this.chungnam = chungnam;
	}

	public int getJeonbuk() {
		return jeonbuk;
	}

	public void setJeonbuk(int jeonbuk) {
		this.jeonbuk = jeonbuk;
	}

	public int getJeonnam() {
		return jeonnam;
	}

	public void setJeonnam(int jeonnam) {
		this.jeonnam = jeonnam;
	}

	public int getGyeongbuk() {
		return gyeongbuk;
	}

	public void setGyeongbuk(int gyeongbuk) {
		this.gyeongbuk = gyeongbuk;
	}

	public int getGyeongnam() {
		return gyeongnam;
	}

	public void setGyeongnam(int gyeongnam) {
		this.gyeongnam = gyeongnam;
	}

	public int getJeju() {
		return jeju;
	}

	public void setJeju(int jeju) {
		this.jeju = jeju;
	}

	public void calSum() {
		this.sum = seoul + incheon + gwangju + daejeon + daegu + busan + ulsan + sejong + gyeonggi + gangwon + chungbuk + chungnam + jeonbuk + jeonnam
				+ gyeongbuk + gyeongnam + jeju;
	}

	public int getLocation(String location) {
		switch (location) {
			case "seoul":
				return this.seoul;
			case "incheon":
				return this.incheon;
			case "gwangju":
				return this.gwangju;
			case "daejeon":
				return this.daejeon;
			case "daegu":
				return this.daegu;
			case "busan":
				return this.busan;
			case "ulsan":
				return this.ulsan;
			case "sejong":
				return this.sejong;
			case "gyeonggi":
				return this.gyeonggi;
			case "gangwon":
				return this.gangwon;
			case "chungbuk":
				return this.chungbuk;
			case "chungnam":
				return this.chungnam;
			case "jeonbuk":
				return this.jeonbuk;
			case "jeonnam":
				return this.jeonnam;
			case "gyeongbuk":
				return this.gyeongbuk;
			case "gyeongnam":
				return this.gyeongnam;
			case "jeju":
				return this.jeju;

			default:
				return 0;
		}
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	@Override
	public String toString() {
		return "LiveVO [live_date=" + live_date + ", seoul=" + seoul + ", incheon=" + incheon + ", gwangju=" + gwangju + ", daejeon=" + daejeon
				+ ", daegu=" + daegu + ", busan=" + busan + ", ulsan=" + ulsan + ", sejong=" + sejong + ", gyeonggi=" + gyeonggi + ", gangwon="
				+ gangwon + ", chungbuk=" + chungbuk + ", chungnam=" + chungnam + ", jeonbuk=" + jeonbuk + ", jeonnam=" + jeonnam + ", gyeongbuk="
				+ gyeongbuk + ", gyeongnam=" + gyeongnam + ", jeju=" + jeju + ", sum=" + sum + "]";
	}
}