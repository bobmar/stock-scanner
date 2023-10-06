package org.rhm.stock.dto;

import java.util.List;
import java.util.Map;

import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.domain.StockStatistic;

public class CompositePrice {
	private String priceId = null;
	private String tickerSymbol = null;
	private StockPrice price = null;
	private List<StockStatistic> statisticList = null;
	private List<StockSignal> signalList = null;
	private List<StockAveragePrice> avgPrices = null;
	private List<IbdStatistic> ibdStatList = null;
	private Map<String,List<StockSignal>> histSignals = null;
	
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
