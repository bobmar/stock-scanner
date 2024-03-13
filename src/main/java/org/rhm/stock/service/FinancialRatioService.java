package org.rhm.stock.service;

import org.rhm.stock.domain.FinancialRatio;
import org.rhm.stock.io.DataDownload;
import org.rhm.stock.repository.FinancialRatioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FinancialRatioService {

  @Autowired
  private FinancialRatioRepo repo;
  @Autowired
  private DataDownload download;
  public void save(List<FinancialRatio> ratioList) {
    repo.saveAll(ratioList);
  }

  public List<FinancialRatio> retrieve(String tickerSymbol) {
    return repo.findBySymbolOrderByDate(tickerSymbol);
  }
  public List<FinancialRatio> downloadRatios(String tickerSymbol) {
    return download.retrieveFinancialRatios(tickerSymbol);
  }
}
