package org.rhm.stock.io;

import org.rhm.stock.dto.PriceBean;

import java.util.List;

public interface PriceDownload {
  public List<PriceBean> downloadPrices(String tickerSymbol);
  public List<PriceBean> downloadPrices(String tickerSymbol, String format);
}
