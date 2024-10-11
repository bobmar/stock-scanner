package org.rhm.stock.dto;

import org.rhm.stock.domain.*;

import java.util.List;
import java.util.Map;

public class CompositePrice {
	private String priceId;
	private String tickerSymbol;
	private StockPrice price;
	private List<StockStatistic> statisticList;
	private List<StockSignal> signalList;
	private List<StockAveragePrice> avgPrices;
	private List<IbdStatistic> ibdStatList;
	private Map<String,List<StockSignal>> histSignals;
	
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
	public StockPrice getPrice() {
		return price;
	}
	public void setPrice(StockPrice price) {
		this.price = price;
	}
	public List<StockStatistic> getStatisticList() {
		return statisticList;
	}
	public void setStatisticList(List<StockStatistic> statisticList) {
		this.statisticList = statisticList;
	}
	public List<StockSignal> getSignalList() {
		return signalList;
	}
	public void setSignalList(List<StockSignal> signalList) {
		this.signalList = signalList;
	}
	public List<StockAveragePrice> getAvgPrices() {
		return avgPrices;
	}
	public void setAvgPrices(List<StockAveragePrice> avgPrices) {
		this.avgPrices = avgPrices;
	}
	public Map<String, List<StockSignal>> getHistSignals() {
		return histSignals;
	}
	public void setHistSignals(Map<String, List<StockSignal>> histSignals) {
		this.histSignals = histSignals;
	}
	public List<IbdStatistic> getIbdStatList() {
		return ibdStatList;
	}
	public void setIbdStatList(List<IbdStatistic> ibdStatList) {
		this.ibdStatList = ibdStatList;
	}
}
