package org.rhm.stock.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class StockSignal {
	@Id
	private String signalId = null;
	private String signalType = null;
	private String priceId = null;
	private String tickerSymbol = null;
	private Date priceDate = null;
	private Double closePrice = null;
	private Double openPrice = null;
	private Double lowPrice = null;
	private Double highPrice = null;
	private Long volume = null;
	
	public StockSignal() {
		
	}
	
	public StockSignal(StockSignal signal) {
		this.signalId = signal.getSignalId();
		this.signalType = signal.getSignalType();
		this.priceId = signal.getPriceId();
		this.tickerSymbol = signal.getTickerSymbol();
		this.priceDate = signal.getPriceDate();
		this.closePrice = signal.getClosePrice();
		this.openPrice = signal.getOpenPrice();
		this.lowPrice = signal.getLowPrice();
		this.highPrice = signal.getHighPrice();
		this.volume = signal.getVolume();
	}
	
	public StockSignal(StockPrice price, String signalType) {
		this.priceId = price.getPriceId();
		this.tickerSymbol = price.getTickerSymbol();
		this.priceDate = price.getPriceDate();
		this.closePrice = price.getClosePrice();
		this.openPrice = price.getOpenPrice();
		this.lowPrice = price.getLowPrice();
		this.highPrice = price.getHighPrice();
		this.volume = price.getVolume();
		this.signalType = signalType;
		this.signalId = price.getPriceId() + ":" + signalType;
	}
	public String getSignalType() {
		return signalType;
	}
	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}
	public String getSignalId() {
		return signalId;
	}
	public void setSignalId(String signalId) {
		this.signalId = signalId;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
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
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	
}
