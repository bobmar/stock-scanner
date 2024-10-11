package org.rhm.stock.dto;

import org.rhm.stock.controller.dto.TickerInfo;

import java.util.List;

public class IbdStockList {

	private String listName;
	private String exportDate;
	private List<TickerInfo> tickerList;
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getExportDate() {
		return exportDate;
	}
	public void setExportDate(String exportDate) {
		this.exportDate = exportDate;
	}
	public List<TickerInfo> getTickerList() {
		return tickerList;
	}
	public void setTickerList(List<TickerInfo> tickerList) {
		this.tickerList = tickerList;
	}
}
