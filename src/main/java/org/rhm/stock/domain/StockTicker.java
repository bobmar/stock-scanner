package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class StockTicker {
	@Id
	private String tickerSymbol;
	private String companyName;
	private String refreshPrices;
	private String sectorName;
	private String industryName;
	private Boolean weeklyOptions = Boolean.FALSE;
	private LocalDateTime createDate = LocalDateTime.now(ZoneId.of("GMT"));
	
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

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
}
