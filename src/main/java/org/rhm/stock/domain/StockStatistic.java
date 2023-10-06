package org.rhm.stock.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class StockStatistic {
	@Id
	private String statId = null;
	private String priceId = null;
	private String statisticType = null;
	private Double statisticValue = null;
	private String tickerSymbol = null;
	private Date priceDate = null;
	
	public StockStatistic() {
		
	}
	
	public StockStatistic(String priceId, String statisticType, Double statisticValue, String tickerSymbol, Date priceDate) {
		this.priceId = priceId;
		this.statisticType = statisticType;
		this.statId = priceId + ":" + statisticType;
		this.statisticValue = statisticValue;
		this.tickerSymbol = tickerSymbol;
		this.priceDate = priceDate;
	}

	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public String getStatisticType() {
		return statisticType;
	}
	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}
	public Double getStatisticValue() {
		return statisticValue;
	}
	public void setStatisticValue(Double statisticValue) {
		this.statisticValue = statisticValue;
	}
	public String getTickerSymbol() {
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}
	public Date getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	public String getStatId() {
		return statId;
	}

	public void setStatId(String statId) {
		this.statId = statId;
	}
}
