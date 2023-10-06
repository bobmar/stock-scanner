package org.rhm.stock.controller.dto;

public class StatListRequest {
	private String statDate = null;
	private String statCode = null;
	private Integer maxResults = null;
	private Double lowValue = null;
	private Double highValue = null;
	public String getStatDate() {
		return statDate;
	}
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
	public String getStatCode() {
		return statCode;
	}
	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}
	public Integer getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	public Double getLowValue() {
		return lowValue;
	}
	public void setLowValue(Double lowValue) {
		this.lowValue = lowValue;
	}
	public Double getHighValue() {
		return highValue;
	}
	public void setHighValue(Double highValue) {
		this.highValue = highValue;
	}
	
}
