package org.rhm.stock.dto;

import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.StockAveragePrice;
import org.rhm.stock.domain.StockSignal;

public class StockSignalDisplay extends StockSignal {

	public StockSignalDisplay(StockSignal signal) {
		super(signal);
	}
	
	private Boolean multiList = false;
	public Boolean getMultiList() {
		return multiList;
	}
	public void setMultiList(Boolean multiList) {
		this.multiList = multiList;
	}
	
	private IbdStatistic ibdLatestStat;
	public IbdStatistic getIbdLatestStat() {
		return ibdLatestStat;
	}
	public void setIbdLatestStat(IbdStatistic ibdLatestStat) {
		this.ibdLatestStat = ibdLatestStat;
	}

	private StockAveragePrice avgPrice;
	public StockAveragePrice getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(StockAveragePrice avgPrice) {
		this.avgPrice = avgPrice;
	}
}
