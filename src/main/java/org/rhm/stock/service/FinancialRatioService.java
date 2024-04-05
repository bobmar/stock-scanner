package org.rhm.stock.service;

import org.rhm.stock.domain.FinancialGrowth;
import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.io.DataDownload;
import org.rhm.stock.repository.FinancialGrowthRepo;
import org.rhm.stock.repository.FinancialRatioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FinancialRatioService {

  @Autowired
  private FinancialRatioRepo ratioRepo;
  @Autowired
  private FinancialGrowthRepo growthRepo;
  @Autowired
  private DataDownload download;
  public void saveRatios(List<FinancialRatio> ratioList) {
    ratioRepo.saveAll(ratioList);
  }
  public void saveGrowthList(List<FinancialGrowth> finGrowthList) {
    growthRepo.saveAll(finGrowthList);
  }
  public List<FinancialRatio> retrieve(String tickerSymbol) {
    return ratioRepo.findBySymbolOrderByDate(tickerSymbol);
  }
  public List<FinancialRatio> downloadRatios(String tickerSymbol) {
    return download.retrieveFinancialRatios(tickerSymbol);
  }

  public List<FinancialGrowth> downloadFinancialGrowth(String tickerSymbol) {
    return download.retrieveFinancialGrowth(tickerSymbol);
  }
}
