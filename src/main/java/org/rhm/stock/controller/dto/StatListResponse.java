package org.rhm.stock.controller.dto;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockStatistic;

public class StatListResponse {
	private Integer statsFoundCnt = null;
	private Double lowValue = null;
	private Double highValue = null;
	private Date statDate = null;
	private List<StockStatistic> statList = null;
	public Integer getStatsFoundCnt() {
		return statsFoundCnt;
	}
	public void setStatsFoundCnt(Integer statsFoundCnt) {
		this.statsFoundCnt = statsFoundCnt;
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
	public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	public List<StockStatistic> getStatList() {
		return statList;
	}
	public void setStatList(List<StockStatistic> statList) {
		this.statList = statList;
	} 
}
