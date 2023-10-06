package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockSignal;

public interface SignalCustomRepo {
	public List<StockSignal> findSignalsByType(List<String> signalTypeList, Date priceDate);
	public long deleteOlderThan(Date deleteBefore);
	public List<String> findUniqueTickerSymbols(Date deleteBefore);
}
