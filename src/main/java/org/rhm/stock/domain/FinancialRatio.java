package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class FinancialRatio {
  @Id
  private String finRatioId;
  private String symbol;
  private String date;
  private String calendarYear;
  private String period;
  private Double currentRatio;
  private Double quickRatio;
  private Double cashRatio;
  private Double daysOfSalesOutstanding;
  private Double daysOfInventoryOutstanding;
  private Double operatingCycle;
  private Double daysOfPayablesOutstanding;
  private Double cashConversionCycle;
  private Double grossProfitMargin;
  private Double operatingProfitMargin;
  private Double pretaxProfitMargin;
  private Double netProfitMargin;
  private Double effectiveTaxRate;
  private Double returnOnAssets;
  private Double returnOnEquity;
  private Double returnOnCapitalEmployed;
  private Double netIncomePerEBT;
  private Double ebtPerEbit;
  private Double ebitPerRevenue;
  private Double debtRatio;
  private Double debtEquityRatio;
  private Double longTermDebtToCapitalization;
  private Double totalDebtToCapitalization;
  private Double interestCoverage;
  private Double cashFlowToDebtRatio;
  private Double companyEquityMultiplier;
  private Double receivablesTurnover;
  private Double payablesTurnover;
  private Double inventoryTurnover;
  private Double fixedAssetTurnover;
  private Double assetTurnover;
  private Double operatingCashFlowPerShare;
  private Double freeCashFlowPerShare;
  private Double cashPerShare;
  private Double payoutRatio;
  private Double operatingCashFlowSalesRatio;
  private Double freeCashFlowOperatingCashFlowRatio;
  private Double cashFlowCoverageRatios;
  private Double shortTermCoverageRatios;
  private Double capitalExpenditureCoverageRatio;
  private Double dividendPaidAndCapexCoverageRatio;
  private Double dividendPayoutRatio;
  private Double priceBookValueRatio;
  private Double priceToBookRatio;
  private Double priceToSalesRatio;
  private Double priceEarningsRatio;
  private Double priceToFreeCashFlowsRatio;
  private Double priceToOperatingCashFlowsRatio;
  private Double priceCashFlowRatio;
  private Double priceEarningsToGrowthRatio;
  private Double priceSalesRatio;
  private Double dividendYield;
  private Double enterpriseValueMultiple;
  private Double priceFairValue;
  private LocalDateTime createDate;

  public String getFinRatioId() {
    return finRatioId;
  }

  public void setFinRatioId(String finRatioId) {
    this.finRatioId = finRatioId;
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

  public Double getCurrentRatio() {
    return currentRatio;
  }

  public void setCurrentRatio(Double currentRatio) {
    this.currentRatio = currentRatio;
  }

  public Double getQuickRatio() {
    return quickRatio;
  }

  public void setQuickRatio(Double quickRatio) {
    this.quickRatio = quickRatio;
  }

  public Double getCashRatio() {
    return cashRatio;
  }

  public void setCashRatio(Double cashRatio) {
    this.cashRatio = cashRatio;
  }

  public Double getDaysOfSalesOutstanding() {
    return daysOfSalesOutstanding;
  }

  public void setDaysOfSalesOutstanding(Double daysOfSalesOutstanding) {
    this.daysOfSalesOutstanding = daysOfSalesOutstanding;
  }

  public Double getDaysOfInventoryOutstanding() {
    return daysOfInventoryOutstanding;
  }

  public void setDaysOfInventoryOutstanding(Double daysOfInventoryOutstanding) {
    this.daysOfInventoryOutstanding = daysOfInventoryOutstanding;
  }

  public Double getOperatingCycle() {
    return operatingCycle;
  }

  public void setOperatingCycle(Double operatingCycle) {
    this.operatingCycle = operatingCycle;
  }

  public Double getDaysOfPayablesOutstanding() {
    return daysOfPayablesOutstanding;
  }

  public void setDaysOfPayablesOutstanding(Double daysOfPayablesOutstanding) {
    this.daysOfPayablesOutstanding = daysOfPayablesOutstanding;
  }

  public Double getCashConversionCycle() {
    return cashConversionCycle;
  }

  public void setCashConversionCycle(Double cashConversionCycle) {
    this.cashConversionCycle = cashConversionCycle;
  }

  public Double getGrossProfitMargin() {
    return grossProfitMargin;
  }

  public void setGrossProfitMargin(Double grossProfitMargin) {
    this.grossProfitMargin = grossProfitMargin;
  }

  public Double getOperatingProfitMargin() {
    return operatingProfitMargin;
  }

  public void setOperatingProfitMargin(Double operatingProfitMargin) {
    this.operatingProfitMargin = operatingProfitMargin;
  }

  public Double getPretaxProfitMargin() {
    return pretaxProfitMargin;
  }

  public void setPretaxProfitMargin(Double pretaxProfitMargin) {
    this.pretaxProfitMargin = pretaxProfitMargin;
  }

  public Double getNetProfitMargin() {
    return netProfitMargin;
  }

  public void setNetProfitMargin(Double netProfitMargin) {
    this.netProfitMargin = netProfitMargin;
  }

  public Double getEffectiveTaxRate() {
    return effectiveTaxRate;
  }

  public void setEffectiveTaxRate(Double effectiveTaxRate) {
    this.effectiveTaxRate = effectiveTaxRate;
  }

  public Double getReturnOnAssets() {
    return returnOnAssets;
  }

  public void setReturnOnAssets(Double returnOnAssets) {
    this.returnOnAssets = returnOnAssets;
  }

  public Double getReturnOnEquity() {
    return returnOnEquity;
  }

  public void setReturnOnEquity(Double returnOnEquity) {
    this.returnOnEquity = returnOnEquity;
  }

  public Double getReturnOnCapitalEmployed() {
    return returnOnCapitalEmployed;
  }

  public void setReturnOnCapitalEmployed(Double returnOnCapitalEmployed) {
    this.returnOnCapitalEmployed = returnOnCapitalEmployed;
  }

  public Double getNetIncomePerEBT() {
    return netIncomePerEBT;
  }

  public void setNetIncomePerEBT(Double netIncomePerEBT) {
    this.netIncomePerEBT = netIncomePerEBT;
  }

  public Double getEbtPerEbit() {
    return ebtPerEbit;
  }

  public void setEbtPerEbit(Double ebtPerEbit) {
    this.ebtPerEbit = ebtPerEbit;
  }

  public Double getEbitPerRevenue() {
    return ebitPerRevenue;
  }

  public void setEbitPerRevenue(Double ebitPerRevenue) {
    this.ebitPerRevenue = ebitPerRevenue;
  }

  public Double getDebtRatio() {
    return debtRatio;
  }

  public void setDebtRatio(Double debtRatio) {
    this.debtRatio = debtRatio;
  }

  public Double getDebtEquityRatio() {
    return debtEquityRatio;
  }

  public void setDebtEquityRatio(Double debtEquityRatio) {
    this.debtEquityRatio = debtEquityRatio;
  }

  public Double getLongTermDebtToCapitalization() {
    return longTermDebtToCapitalization;
  }

  public void setLongTermDebtToCapitalization(Double longTermDebtToCapitalization) {
    this.longTermDebtToCapitalization = longTermDebtToCapitalization;
  }

  public Double getTotalDebtToCapitalization() {
    return totalDebtToCapitalization;
  }

  public void setTotalDebtToCapitalization(Double totalDebtToCapitalization) {
    this.totalDebtToCapitalization = totalDebtToCapitalization;
  }

  public Double getInterestCoverage() {
    return interestCoverage;
  }

  public void setInterestCoverage(Double interestCoverage) {
    this.interestCoverage = interestCoverage;
  }

  public Double getCashFlowToDebtRatio() {
    return cashFlowToDebtRatio;
  }

  public void setCashFlowToDebtRatio(Double cashFlowToDebtRatio) {
    this.cashFlowToDebtRatio = cashFlowToDebtRatio;
  }

  public Double getCompanyEquityMultiplier() {
    return companyEquityMultiplier;
  }

  public void setCompanyEquityMultiplier(Double companyEquityMultiplier) {
    this.companyEquityMultiplier = companyEquityMultiplier;
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

  public Double getFixedAssetTurnover() {
    return fixedAssetTurnover;
  }

  public void setFixedAssetTurnover(Double fixedAssetTurnover) {
    this.fixedAssetTurnover = fixedAssetTurnover;
  }

  public Double getAssetTurnover() {
    return assetTurnover;
  }

  public void setAssetTurnover(Double assetTurnover) {
    this.assetTurnover = assetTurnover;
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

  public Double getPayoutRatio() {
    return payoutRatio;
  }

  public void setPayoutRatio(Double payoutRatio) {
    this.payoutRatio = payoutRatio;
  }

  public Double getOperatingCashFlowSalesRatio() {
    return operatingCashFlowSalesRatio;
  }

  public void setOperatingCashFlowSalesRatio(Double operatingCashFlowSalesRatio) {
    this.operatingCashFlowSalesRatio = operatingCashFlowSalesRatio;
  }

  public Double getFreeCashFlowOperatingCashFlowRatio() {
    return freeCashFlowOperatingCashFlowRatio;
  }

  public void setFreeCashFlowOperatingCashFlowRatio(Double freeCashFlowOperatingCashFlowRatio) {
    this.freeCashFlowOperatingCashFlowRatio = freeCashFlowOperatingCashFlowRatio;
  }

  public Double getCashFlowCoverageRatios() {
    return cashFlowCoverageRatios;
  }

  public void setCashFlowCoverageRatios(Double cashFlowCoverageRatios) {
    this.cashFlowCoverageRatios = cashFlowCoverageRatios;
  }

  public Double getShortTermCoverageRatios() {
    return shortTermCoverageRatios;
  }

  public void setShortTermCoverageRatios(Double shortTermCoverageRatios) {
    this.shortTermCoverageRatios = shortTermCoverageRatios;
  }

  public Double getCapitalExpenditureCoverageRatio() {
    return capitalExpenditureCoverageRatio;
  }

  public void setCapitalExpenditureCoverageRatio(Double capitalExpenditureCoverageRatio) {
    this.capitalExpenditureCoverageRatio = capitalExpenditureCoverageRatio;
  }

  public Double getDividendPaidAndCapexCoverageRatio() {
    return dividendPaidAndCapexCoverageRatio;
  }

  public void setDividendPaidAndCapexCoverageRatio(Double dividendPaidAndCapexCoverageRatio) {
    this.dividendPaidAndCapexCoverageRatio = dividendPaidAndCapexCoverageRatio;
  }

  public Double getDividendPayoutRatio() {
    return dividendPayoutRatio;
  }

  public void setDividendPayoutRatio(Double dividendPayoutRatio) {
    this.dividendPayoutRatio = dividendPayoutRatio;
  }

  public Double getPriceBookValueRatio() {
    return priceBookValueRatio;
  }

  public void setPriceBookValueRatio(Double priceBookValueRatio) {
    this.priceBookValueRatio = priceBookValueRatio;
  }

  public Double getPriceToBookRatio() {
    return priceToBookRatio;
  }

  public void setPriceToBookRatio(Double priceToBookRatio) {
    this.priceToBookRatio = priceToBookRatio;
  }

  public Double getPriceToSalesRatio() {
    return priceToSalesRatio;
  }

  public void setPriceToSalesRatio(Double priceToSalesRatio) {
    this.priceToSalesRatio = priceToSalesRatio;
  }

  public Double getPriceEarningsRatio() {
    return priceEarningsRatio;
  }

  public void setPriceEarningsRatio(Double priceEarningsRatio) {
    this.priceEarningsRatio = priceEarningsRatio;
  }

  public Double getPriceToFreeCashFlowsRatio() {
    return priceToFreeCashFlowsRatio;
  }

  public void setPriceToFreeCashFlowsRatio(Double priceToFreeCashFlowsRatio) {
    this.priceToFreeCashFlowsRatio = priceToFreeCashFlowsRatio;
  }

  public Double getPriceToOperatingCashFlowsRatio() {
    return priceToOperatingCashFlowsRatio;
  }

  public void setPriceToOperatingCashFlowsRatio(Double priceToOperatingCashFlowsRatio) {
    this.priceToOperatingCashFlowsRatio = priceToOperatingCashFlowsRatio;
  }

  public Double getPriceCashFlowRatio() {
    return priceCashFlowRatio;
  }

  public void setPriceCashFlowRatio(Double priceCashFlowRatio) {
    this.priceCashFlowRatio = priceCashFlowRatio;
  }

  public Double getPriceEarningsToGrowthRatio() {
    return priceEarningsToGrowthRatio;
  }

  public void setPriceEarningsToGrowthRatio(Double priceEarningsToGrowthRatio) {
    this.priceEarningsToGrowthRatio = priceEarningsToGrowthRatio;
  }

  public Double getPriceSalesRatio() {
    return priceSalesRatio;
  }

  public void setPriceSalesRatio(Double priceSalesRatio) {
    this.priceSalesRatio = priceSalesRatio;
  }

  public Double getDividendYield() {
    return dividendYield;
  }

  public void setDividendYield(Double dividendYield) {
    this.dividendYield = dividendYield;
  }

  public Double getEnterpriseValueMultiple() {
    return enterpriseValueMultiple;
  }

  public void setEnterpriseValueMultiple(Double enterpriseValueMultiple) {
    this.enterpriseValueMultiple = enterpriseValueMultiple;
  }

  public Double getPriceFairValue() {
    return priceFairValue;
  }

  public void setPriceFairValue(Double priceFairValue) {
    this.priceFairValue = priceFairValue;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }
}
