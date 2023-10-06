package org.rhm.stock.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

import org.springframework.data.annotation.Id;

public class StockPrice {
	@Id
	private String priceId = null;
	private String tickerSymbol = null;
	private Date priceDate = null;
	private Double closePrice = null;
	private Double openPrice = null;
	private Double lowPrice = null;
	private Double highPrice = null;
	private Long volume = null;
	public StockPrice() {}
	
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
	public Double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}
	public Double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}
	public Double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}
	public Double getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}
	public Long getVolume() {
		return volume;
	}
	public Double getDollarVolume() {
		return this.volume * this.closePrice.doubleValue();
	}
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	
	public String toString() {
		return this.tickerSymbol + ":" + this.priceDate;
	}

	public Double getHighLowRange() {
		BigDecimal result = BigDecimal.valueOf(highPrice.doubleValue() - lowPrice.doubleValue()).round(new MathContext(3));
		return result.doubleValue();
	}
	
	public Double getOpenCloseRange() {
		BigDecimal result = BigDecimal.valueOf(closePrice.doubleValue() - openPrice.doubleValue()).round(new MathContext(3));
		return result.doubleValue();
	}
	
	public Double getHighLowVsClosePct() {
		BigDecimal result = null;
		try {
			result = BigDecimal.valueOf(((getHighLowRange() / closePrice.doubleValue()) * 100)).round(new MathContext(3));
		}
		catch (NumberFormatException e) {
			result = new BigDecimal(0.0);
		}
		return result.doubleValue();
	}
	
	public Double getOpenCloseVsHighLowPct() {
		BigDecimal result = null;
		try {
			result = BigDecimal.valueOf(((getOpenCloseRange() / getHighLowRange()) * 100)).round(new MathContext(3));
		}
		catch (NumberFormatException e) {
			result = new BigDecimal(0.0);
		}
		return result.doubleValue();
	}

	public Double getCloseVsLowDiffPct() {
		BigDecimal result = null;
		try {
			result = BigDecimal.valueOf((((closePrice.doubleValue() - lowPrice.doubleValue()) / lowPrice.doubleValue()) * 100)).round(new MathContext(3));
		}
		catch (NumberFormatException e) {
			result = new BigDecimal(0.0);
		}
		return result.doubleValue();
	}
	
	public Double getCloseVsHighDiffPct() {
		BigDecimal result = null;
		try {
			result = BigDecimal.valueOf((((highPrice.doubleValue() - closePrice.doubleValue()) / highPrice.doubleValue()) * 100)).round(new MathContext(3));
		}
		catch (NumberFormatException e) {
			result = new BigDecimal(0.0);
		}
		return result.doubleValue();
	}
	
	public boolean equals(Object o) {
		boolean isEqual = false;
		if (this.toString().equals(((StockPrice)o).toString())) {
			isEqual = true;
		}
		return isEqual;
	}

}
