package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

public class StockTicker {
	@Id
	private String tickerSymbol = null;
	private String companyName = null;
	private String refreshPrices = null;
	private String sectorName = null;
	private String industryName = null;
	private Boolean weeklyOptions = false;
	
	public String getTickerSymbol() {
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRefreshPrices() {
		return refreshPrices;
	}
	public void setRefreshPrices(String refreshPrices) {
		this.refreshPrices = refreshPrices;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Boolean getWeeklyOptions() {
		return weeklyOptions;
	}

	public void setWeeklyOptions(Boolean weeklyOptions) {
		this.weeklyOptions = weeklyOptions;
	}
}
