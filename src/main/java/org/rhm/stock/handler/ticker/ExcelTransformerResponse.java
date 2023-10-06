package org.rhm.stock.handler.ticker;

import java.util.ArrayList;
import java.util.List;

import org.rhm.stock.domain.IbdStatistic;

public class ExcelTransformerResponse {
	List<String> tickerSymbols = new ArrayList<String>();
	List<IbdStatistic> ibdStatList = new ArrayList<IbdStatistic>();
	String listName = null;
	public List<String> getTickerSymbols() {
		return tickerSymbols;
	}
	public void setTickerSymbols(List<String> tickerSymbols) {
		this.tickerSymbols = tickerSymbols;
	}
	public List<IbdStatistic> getIbdStatList() {
		return ibdStatList;
	}
	public void setIbdStatList(List<IbdStatistic> ibdStatList) {
		this.ibdStatList = ibdStatList;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}

}
