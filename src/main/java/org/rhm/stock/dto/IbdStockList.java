package org.rhm.stock.dto;

import java.util.List;

import org.rhm.stock.controller.dto.TickerInfo;

public class IbdStockList {

	private String listName = null;
	private String exportDate = null;
	private List<TickerInfo> tickerList = null;
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
