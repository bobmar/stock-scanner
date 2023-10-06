package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

public interface PriceCustomRepo {
	public long deleteOlderThan(Date deleteBefore);
	public List<String> findUniqueTickerSymbols(Date deleteBefore);
}
