package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class StockAveragePrice {
	@Id
	private String priceId;
	private List<AveragePrice> avgList;
	private String tickerSymbol;
	private Date priceDate;
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
