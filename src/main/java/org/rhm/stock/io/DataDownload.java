package org.rhm.stock.io;

import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.dto.PriceBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataDownload {
  public List<PriceBean> downloadPrices(String tickerSymbol, int days);
  public Map<String,Object> retrieveCompanyInfo(String tickerSymbol);
  public List<FinancialRatio> retrieveFinancialRatios(String tickerSymbol);
}
