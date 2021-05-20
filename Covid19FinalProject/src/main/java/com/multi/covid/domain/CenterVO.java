package com.multi.covid.domain;

public class CenterVO {
	private String center_name, facility_name, phone_number, location, address, zip_code;
	private double lat, lng;

	public CenterVO() {
	}

	public CenterVO(String center_name, String facility_name, String phone_number, String location, String address, String zip_code, double lat,
			double lng) {
		this.center_name = center_name;
		this.facility_name = facility_name;
		this.phone_number = phone_number;
		this.location = location;
		this.address = address;
		this.zip_code = zip_code;
		this.lat = lat;
		this.lng = lng;
	}

	public String getCenter_name() {
		return center_name;
	}

	public void setCenter_name(String center_name) {
		this.center_name = center_name;
	}

	public String getFacility_name() {
		return facility_name;
	}

	public void setFacility_name(String facility_name) {
		this.facility_name = facility_name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "CenterVO [center_name=" + center_name + ", facility_name=" + facility_name + ", phone_number=" + phone_number + ", location="
				+ location + ", address=" + address + ", zip_code=" + zip_code + ", lat=" + lat + ", lng=" + lng + "]";
	}

}
