package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

public class KeyMetric {
  @Id
  private String keyMetricId;
  private String symbol;
  private String date;
  private String calendarYear;
  private String period;
  private String revenuePerShare;
  private Double netIncomePerShare;
  private Double operatingCashFlowPerShare;
  private Double freeCashFlowPerShare;
  private Double cashPerShare;
  private Double bookValuePerShare;
  private Double tangibleBookValuePerShare;
  private Double shareholdersEquityPerShare;
  private Double interestDebtPerShare;
  private Double marketCap;
  private Double enterpriseValue;
  private Double peRatio;
  private Double priceToSalesRatio;
  private Double pocfratio;
  private Double pfcfRatio;
  private Double pbRatio;
  private Double ptbRatio;
  private Double evToSales;
  private Double enterpriseValueOverEBITDA;
  private Double evToOperatingCashFlow;
  private Double evToFreeCashFlow;
  private Double earningsYield;
  private Double freeCashFlowYield;
  private Double debtToEquity;
  private Double debtToAssets;
  private Double netDebtToEBITDA;
  private Double currentRatio;
  private Double interestCoverage;
  private Double incomeQuality;
  private Double dividendYield;
  private Double payoutRatio;
  private Double salesGeneralAndAdministrativeToRevenue;
  private Double researchAndDdevelopementToRevenue;
  private Double intangiblesToTotalAssets;
  private Double capexToOperatingCashFlow;
  private Double capexToRevenue;
  private Double capexToDepreciation;
  private Double stockBasedCompensationToRevenue;
  private Double grahamNumber;
  private Double roic;
  private Double returnOnTangibleAssets;
  private Double grahamNetNet;
  private Double workingCapital;
  private Double tangibleAssetValue;
  private Double netCurrentAssetValue;
  private Double investedCapital;
  private Double averageReceivables;
  private Double averagePayables;
  private Double averageInventory;
  private Double daysSalesOutstanding;
  private Double daysPayablesOutstanding;
  private Double daysOfInventoryOnHand;
  private Double receivablesTurnover;
  private Double payablesTurnover;
  private Double inventoryTurnover;
  private Double roe;
  private Double capexPerShare;

  public String getKeyMetricId() {
    return keyMetricId;
  }

  public void setKeyMetricId(String keyMetricId) {
    this.keyMetricId = keyMetricId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getCalendarYear() {
    return calendarYear;
  }

  public void setCalendarYear(String calendarYear) {
    this.calendarYear = calendarYear;
  }

  public String getPeriod() {
    return period;
  }

  public void setPeriod(String period) {
    this.period = period;
  }

  public String getRevenuePerShare() {
    return revenuePerShare;
  }

  public void setRevenuePerShare(String revenuePerShare) {
    this.revenuePerShare = revenuePerShare;
  }

  public Double getNetIncomePerShare() {
    return netIncomePerShare;
  }

  public void setNetIncomePerShare(Double netIncomePerShare) {
    this.netIncomePerShare = netIncomePerShare;
  }

  public Double getOperatingCashFlowPerShare() {
    return operatingCashFlowPerShare;
  }

  public void setOperatingCashFlowPerShare(Double operatingCashFlowPerShare) {
    this.operatingCashFlowPerShare = operatingCashFlowPerShare;
  }

  public Double getFreeCashFlowPerShare() {
    return freeCashFlowPerShare;
  }

  public void setFreeCashFlowPerShare(Double freeCashFlowPerShare) {
    this.freeCashFlowPerShare = freeCashFlowPerShare;
  }

  public Double getCashPerShare() {
    return cashPerShare;
  }

  public void setCashPerShare(Double cashPerShare) {
    this.cashPerShare = cashPerShare;
  }

  public Double getBookValuePerShare() {
    return bookValuePerShare;
  }

  public void setBookValuePerShare(Double bookValuePerShare) {
    this.bookValuePerShare = bookValuePerShare;
  }

  public Double getTangibleBookValuePerShare() {
    return tangibleBookValuePerShare;
  }

  public void setTangibleBookValuePerShare(Double tangibleBookValuePerShare) {
    this.tangibleBookValuePerShare = tangibleBookValuePerShare;
  }

  public Double getShareholdersEquityPerShare() {
    return shareholdersEquityPerShare;
  }

  public void setShareholdersEquityPerShare(Double shareholdersEquityPerShare) {
    this.shareholdersEquityPerShare = shareholdersEquityPerShare;
  }

  public Double getInterestDebtPerShare() {
    return interestDebtPerShare;
  }

  public void setInterestDebtPerShare(Double interestDebtPerShare) {
    this.interestDebtPerShare = interestDebtPerShare;
  }

  public Double getMarketCap() {
    return marketCap;
  }

  public void setMarketCap(Double marketCap) {
    this.marketCap = marketCap;
  }

  public Double getEnterpriseValue() {
    return enterpriseValue;
  }

  public void setEnterpriseValue(Double enterpriseValue) {
    this.enterpriseValue = enterpriseValue;
  }

  public Double getPeRatio() {
    return peRatio;
  }

  public void setPeRatio(Double peRatio) {
    this.peRatio = peRatio;
  }

  public Double getPriceToSalesRatio() {
    return priceToSalesRatio;
  }

  public void setPriceToSalesRatio(Double priceToSalesRatio) {
    this.priceToSalesRatio = priceToSalesRatio;
  }

  public Double getPocfratio() {
    return pocfratio;
  }

  public void setPocfratio(Double pocfratio) {
    this.pocfratio = pocfratio;
  }

  public Double getPfcfRatio() {
    return pfcfRatio;
  }

  public void setPfcfRatio(Double pfcfRatio) {
    this.pfcfRatio = pfcfRatio;
  }

  public Double getPbRatio() {
    return pbRatio;
  }

  public void setPbRatio(Double pbRatio) {
    this.pbRatio = pbRatio;
  }

  public Double getPtbRatio() {
    return ptbRatio;
  }

  public void setPtbRatio(Double ptbRatio) {
    this.ptbRatio = ptbRatio;
  }

  public Double getEvToSales() {
    return evToSales;
  }

  public void setEvToSales(Double evToSales) {
    this.evToSales = evToSales;
  }

  public Double getEnterpriseValueOverEBITDA() {
    return enterpriseValueOverEBITDA;
  }

  public void setEnterpriseValueOverEBITDA(Double enterpriseValueOverEBITDA) {
    this.enterpriseValueOverEBITDA = enterpriseValueOverEBITDA;
  }

  public Double getEvToOperatingCashFlow() {
    return evToOperatingCashFlow;
  }

  public void setEvToOperatingCashFlow(Double evToOperatingCashFlow) {
    this.evToOperatingCashFlow = evToOperatingCashFlow;
  }

  public Double getEvToFreeCashFlow() {
    return evToFreeCashFlow;
  }

  public void setEvToFreeCashFlow(Double evToFreeCashFlow) {
    this.evToFreeCashFlow = evToFreeCashFlow;
  }

  public Double getEarningsYield() {
    return earningsYield;
  }

  public void setEarningsYield(Double earningsYield) {
    this.earningsYield = earningsYield;
  }

  public Double getFreeCashFlowYield() {
    return freeCashFlowYield;
  }

  public void setFreeCashFlowYield(Double freeCashFlowYield) {
    this.freeCashFlowYield = freeCashFlowYield;
  }

  public Double getDebtToEquity() {
    return debtToEquity;
  }

  public void setDebtToEquity(Double debtToEquity) {
    this.debtToEquity = debtToEquity;
  }

  public Double getDebtToAssets() {
    return debtToAssets;
  }

  public void setDebtToAssets(Double debtToAssets) {
    this.debtToAssets = debtToAssets;
  }

  public Double getNetDebtToEBITDA() {
    return netDebtToEBITDA;
  }

  public void setNetDebtToEBITDA(Double netDebtToEBITDA) {
    this.netDebtToEBITDA = netDebtToEBITDA;
  }

  public Double getCurrentRatio() {
    return currentRatio;
  }

  public void setCurrentRatio(Double currentRatio) {
    this.currentRatio = currentRatio;
  }

  public Double getInterestCoverage() {
    return interestCoverage;
  }

  public void setInterestCoverage(Double interestCoverage) {
    this.interestCoverage = interestCoverage;
  }

  public Double getIncomeQuality() {
    return incomeQuality;
  }

  public void setIncomeQuality(Double incomeQuality) {
    this.incomeQuality = incomeQuality;
  }

  public Double getDividendYield() {
    return dividendYield;
  }

  public void setDividendYield(Double dividendYield) {
    this.dividendYield = dividendYield;
  }

  public Double getPayoutRatio() {
    return payoutRatio;
  }

  public void setPayoutRatio(Double payoutRatio) {
    this.payoutRatio = payoutRatio;
  }

  public Double getSalesGeneralAndAdministrativeToRevenue() {
    return salesGeneralAndAdministrativeToRevenue;
  }

  public void setSalesGeneralAndAdministrativeToRevenue(Double salesGeneralAndAdministrativeToRevenue) {
    this.salesGeneralAndAdministrativeToRevenue = salesGeneralAndAdministrativeToRevenue;
  }

  public Double getResearchAndDdevelopementToRevenue() {
    return researchAndDdevelopementToRevenue;
  }

  public void setResearchAndDdevelopementToRevenue(Double researchAndDdevelopementToRevenue) {
    this.researchAndDdevelopementToRevenue = researchAndDdevelopementToRevenue;
  }

  public Double getIntangiblesToTotalAssets() {
    return intangiblesToTotalAssets;
  }

  public void setIntangiblesToTotalAssets(Double intangiblesToTotalAssets) {
    this.intangiblesToTotalAssets = intangiblesToTotalAssets;
  }

  public Double getCapexToOperatingCashFlow() {
    return capexToOperatingCashFlow;
  }

  public void setCapexToOperatingCashFlow(Double capexToOperatingCashFlow) {
    this.capexToOperatingCashFlow = capexToOperatingCashFlow;
  }

  public Double getCapexToRevenue() {
    return capexToRevenue;
  }

  public void setCapexToRevenue(Double capexToRevenue) {
    this.capexToRevenue = capexToRevenue;
  }

  public Double getCapexToDepreciation() {
    return capexToDepreciation;
  }

  public void setCapexToDepreciation(Double capexToDepreciation) {
    this.capexToDepreciation = capexToDepreciation;
  }

  public Double getStockBasedCompensationToRevenue() {
    return stockBasedCompensationToRevenue;
  }

  public void setStockBasedCompensationToRevenue(Double stockBasedCompensationToRevenue) {
    this.stockBasedCompensationToRevenue = stockBasedCompensationToRevenue;
  }

  public Double getGrahamNumber() {
    return grahamNumber;
  }

  public void setGrahamNumber(Double grahamNumber) {
    this.grahamNumber = grahamNumber;
  }

  public Double getRoic() {
    return roic;
  }

  public void setRoic(Double roic) {
    this.roic = roic;
  }

  public Double getReturnOnTangibleAssets() {
    return returnOnTangibleAssets;
  }

  public void setReturnOnTangibleAssets(Double returnOnTangibleAssets) {
    this.returnOnTangibleAssets = returnOnTangibleAssets;
  }

  public Double getGrahamNetNet() {
    return grahamNetNet;
  }

  public void setGrahamNetNet(Double grahamNetNet) {
    this.grahamNetNet = grahamNetNet;
  }

  public Double getWorkingCapital() {
    return workingCapital;
  }

  public void setWorkingCapital(Double workingCapital) {
    this.workingCapital = workingCapital;
  }

  public Double getTangibleAssetValue() {
    return tangibleAssetValue;
  }

  public void setTangibleAssetValue(Double tangibleAssetValue) {
    this.tangibleAssetValue = tangibleAssetValue;
  }

  public Double getNetCurrentAssetValue() {
    return netCurrentAssetValue;
  }

  public void setNetCurrentAssetValue(Double netCurrentAssetValue) {
    this.netCurrentAssetValue = netCurrentAssetValue;
  }

  public Double getInvestedCapital() {
    return investedCapital;
  }

  public void setInvestedCapital(Double investedCapital) {
    this.investedCapital = investedCapital;
  }

  public Double getAverageReceivables() {
    return averageReceivables;
  }

  public void setAverageReceivables(Double averageReceivables) {
    this.averageReceivables = averageReceivables;
  }

  public Double getAveragePayables() {
    return averagePayables;
  }

  public void setAveragePayables(Double averagePayables) {
    this.averagePayables = averagePayables;
  }

  public Double getAverageInventory() {
    return averageInventory;
  }

  public void setAverageInventory(Double averageInventory) {
    this.averageInventory = averageInventory;
  }

  public Double getDaysSalesOutstanding() {
    return daysSalesOutstanding;
  }

  public void setDaysSalesOutstanding(Double daysSalesOutstanding) {
    this.daysSalesOutstanding = daysSalesOutstanding;
  }

  public Double getDaysPayablesOutstanding() {
    return daysPayablesOutstanding;
  }

  public void setDaysPayablesOutstanding(Double daysPayablesOutstanding) {
    this.daysPayablesOutstanding = daysPayablesOutstanding;
  }

  public Double getDaysOfInventoryOnHand() {
    return daysOfInventoryOnHand;
  }

  public void setDaysOfInventoryOnHand(Double daysOfInventoryOnHand) {
    this.daysOfInventoryOnHand = daysOfInventoryOnHand;
  }

  public Double getReceivablesTurnover() {
    return receivablesTurnover;
  }

  public void setReceivablesTurnover(Double receivablesTurnover) {
    this.receivablesTurnover = receivablesTurnover;
  }

  public Double getPayablesTurnover() {
    return payablesTurnover;
  }

  public void setPayablesTurnover(Double payablesTurnover) {
    this.payablesTurnover = payablesTurnover;
  }

  public Double getInventoryTurnover() {
    return inventoryTurnover;
  }

  public void setInventoryTurnover(Double inventoryTurnover) {
    this.inventoryTurnover = inventoryTurnover;
  }

  public Double getRoe() {
    return roe;
  }

  public void setRoe(Double roe) {
    this.roe = roe;
  }

  public Double getCapexPerShare() {
    return capexPerShare;
  }

  public void setCapexPerShare(Double capexPerShare) {
    this.capexPerShare = capexPerShare;
  }
}
