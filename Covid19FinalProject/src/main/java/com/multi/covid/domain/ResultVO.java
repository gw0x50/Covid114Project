package com.multi.covid.domain;

public class ResultVO {
	private int sequence;
	private String result_date, location;
	private int increment_count, total_count, clear_count, death_count;

	public ResultVO() {
		this.increment_count = 0;
		this.total_count = 0;
		this.clear_count = 0;
		this.death_count = 0;
	}

	public ResultVO(int sequence, String result_date, String location, int increment_count, int total_count, int clear_count, int death_count) {
		this.sequence = sequence;
		this.result_date = result_date;
		this.location = location;
		this.increment_count = increment_count;
		this.total_count = total_count;
		this.clear_count = clear_count;
		this.death_count = death_count;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getResult_date() {
		return result_date;
	}

	public void setResult_date(String result_date) {
		this.result_date = result_date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getIncrement_count() {
		return increment_count;
	}

	public void setIncrement_count(int increment_count) {
		this.increment_count = increment_count;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getClear_count() {
		return clear_count;
	}

	public void setClear_count(int clear_count) {
		this.clear_count = clear_count;
	}

	public int getDeath_count() {
		return death_count;
	}

	public void setDeath_count(int death_count) {
		this.death_count = death_count;
	}

	@Override
	public String toString() {
		return "ResultVO [sequence=" + sequence + ", result_date=" + result_date + ", location=" + location + ", increment_count=" + increment_count
				+ ", total_count=" + total_count + ", clear_count=" + clear_count + ", death_count=" + death_count + "]";
	}

}
