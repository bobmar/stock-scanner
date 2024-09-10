package org.rhm.stock.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class StockAveragePrice {
	@Id
	private String priceId = null;
	private List<AveragePrice> avgList = null;
	private String tickerSymbol = null;
	private Date priceDate = null;
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public List<AveragePrice> getAvgList() {
		return avgList;
	}
	public void setAvgList(List<AveragePrice> avgList) {
		this.avgList = avgList;
	}
	
	public AveragePrice findAvgPrice(Integer days) {
		AveragePrice avgPrice = null;
		if (avgList != null) {
			for (AveragePrice avg: avgList) {
				if (avg.getDaysCnt().intValue() == days.intValue()) {
					avgPrice = avg;
				}
			}
		}
		return avgPrice;
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
}
