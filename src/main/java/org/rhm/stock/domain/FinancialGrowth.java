package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class FinancialGrowth {
  @Id
  private String finGrowthId;
  private String symbol;
  private String date;
  private String calendarYear;
  private String period;
  private Double revenueGrowth;
  private Double grossProfitGrowth;
  private Double ebitgrowth;
  private Double operatingIncomeGrowth;
  private Double netIncomeGrowth;
  private Double epsgrowth;
  private Double epsdilutedGrowth;
  private Double weightedAverageSharesGrowth;
  private Double weightedAverageSharesDilutedGrowth;
  private Double dividendsperShareGrowth;
  private Double operatingCashFlowGrowth;
  private Double freeCashFlowGrowth;
  private Double tenYRevenueGrowthPerShare;
  private Double fiveYRevenueGrowthPerShare;
  private Double threeYRevenueGrowthPerShare;
  private Double tenYOperatingCFGrowthPerShare;
  private Double fiveYOperatingCFGrowthPerShare;
  private Double threeYOperatingCFGrowthPerShare;
  private Double tenYNetIncomeGrowthPerShare;
  private Double fiveYNetIncomeGrowthPerShare;
  private Double threeYNetIncomeGrowthPerShare;
  private Double tenYShareholdersEquityGrowthPerShare;
  private Double fiveYShareholdersEquityGrowthPerShare;
  private Double threeYShareholdersEquityGrowthPerShare;
  private Double tenYDividendperShareGrowthPerShare;
  private Double fiveYDividendperShareGrowthPerShare;
  private Double threeYDividendperShareGrowthPerShare;
  private Double receivablesGrowth;
  private Double inventoryGrowth;
  private Double assetGrowth;
  private Double bookValueperShareGrowth;
  private Double debtGrowth;
  private Double rdexpenseGrowth;
  private Double sgaexpensesGrowth;
  private LocalDateTime createDate;
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

  public Double getRevenueGrowth() {
    return revenueGrowth;
  }

  public void setRevenueGrowth(Double revenueGrowth) {
    this.revenueGrowth = revenueGrowth;
  }

  public Double getGrossProfitGrowth() {
    return grossProfitGrowth;
  }

  public void setGrossProfitGrowth(Double grossProfitGrowth) {
    this.grossProfitGrowth = grossProfitGrowth;
  }

  public Double getEbitgrowth() {
    return ebitgrowth;
  }

  public void setEbitgrowth(Double ebitgrowth) {
    this.ebitgrowth = ebitgrowth;
  }

  public Double getOperatingIncomeGrowth() {
    return operatingIncomeGrowth;
  }

  public void setOperatingIncomeGrowth(Double operatingIncomeGrowth) {
    this.operatingIncomeGrowth = operatingIncomeGrowth;
  }

  public Double getNetIncomeGrowth() {
    return netIncomeGrowth;
  }

  public void setNetIncomeGrowth(Double netIncomeGrowth) {
    this.netIncomeGrowth = netIncomeGrowth;
  }

  public Double getEpsgrowth() {
    return epsgrowth;
  }

  public void setEpsgrowth(Double epsgrowth) {
    this.epsgrowth = epsgrowth;
  }

  public Double getEpsdilutedGrowth() {
    return epsdilutedGrowth;
  }

  public void setEpsdilutedGrowth(Double epsdilutedGrowth) {
    this.epsdilutedGrowth = epsdilutedGrowth;
  }

  public Double getWeightedAverageSharesGrowth() {
    return weightedAverageSharesGrowth;
  }

  public void setWeightedAverageSharesGrowth(Double weightedAverageSharesGrowth) {
    this.weightedAverageSharesGrowth = weightedAverageSharesGrowth;
  }

  public Double getWeightedAverageSharesDilutedGrowth() {
    return weightedAverageSharesDilutedGrowth;
  }

  public void setWeightedAverageSharesDilutedGrowth(Double weightedAverageSharesDilutedGrowth) {
    this.weightedAverageSharesDilutedGrowth = weightedAverageSharesDilutedGrowth;
  }

  public Double getDividendsperShareGrowth() {
    return dividendsperShareGrowth;
  }

  public void setDividendsperShareGrowth(Double dividendsperShareGrowth) {
    this.dividendsperShareGrowth = dividendsperShareGrowth;
  }

  public Double getOperatingCashFlowGrowth() {
    return operatingCashFlowGrowth;
  }

  public void setOperatingCashFlowGrowth(Double operatingCashFlowGrowth) {
    this.operatingCashFlowGrowth = operatingCashFlowGrowth;
  }

  public Double getFreeCashFlowGrowth() {
    return freeCashFlowGrowth;
  }

  public void setFreeCashFlowGrowth(Double freeCashFlowGrowth) {
    this.freeCashFlowGrowth = freeCashFlowGrowth;
  }

  public Double getTenYRevenueGrowthPerShare() {
    return tenYRevenueGrowthPerShare;
  }

  public void setTenYRevenueGrowthPerShare(Double tenYRevenueGrowthPerShare) {
    this.tenYRevenueGrowthPerShare = tenYRevenueGrowthPerShare;
  }

  public Double getFiveYRevenueGrowthPerShare() {
    return fiveYRevenueGrowthPerShare;
  }

  public void setFiveYRevenueGrowthPerShare(Double fiveYRevenueGrowthPerShare) {
    this.fiveYRevenueGrowthPerShare = fiveYRevenueGrowthPerShare;
  }

  public Double getThreeYRevenueGrowthPerShare() {
    return threeYRevenueGrowthPerShare;
  }

  public void setThreeYRevenueGrowthPerShare(Double threeYRevenueGrowthPerShare) {
    this.threeYRevenueGrowthPerShare = threeYRevenueGrowthPerShare;
  }

  public Double getTenYOperatingCFGrowthPerShare() {
    return tenYOperatingCFGrowthPerShare;
  }

  public void setTenYOperatingCFGrowthPerShare(Double tenYOperatingCFGrowthPerShare) {
    this.tenYOperatingCFGrowthPerShare = tenYOperatingCFGrowthPerShare;
  }

  public Double getFiveYOperatingCFGrowthPerShare() {
    return fiveYOperatingCFGrowthPerShare;
  }

  public void setFiveYOperatingCFGrowthPerShare(Double fiveYOperatingCFGrowthPerShare) {
    this.fiveYOperatingCFGrowthPerShare = fiveYOperatingCFGrowthPerShare;
  }

  public Double getThreeYOperatingCFGrowthPerShare() {
    return threeYOperatingCFGrowthPerShare;
  }

  public void setThreeYOperatingCFGrowthPerShare(Double threeYOperatingCFGrowthPerShare) {
    this.threeYOperatingCFGrowthPerShare = threeYOperatingCFGrowthPerShare;
  }

  public Double getTenYNetIncomeGrowthPerShare() {
    return tenYNetIncomeGrowthPerShare;
  }

  public void setTenYNetIncomeGrowthPerShare(Double tenYNetIncomeGrowthPerShare) {
    this.tenYNetIncomeGrowthPerShare = tenYNetIncomeGrowthPerShare;
  }

  public Double getFiveYNetIncomeGrowthPerShare() {
    return fiveYNetIncomeGrowthPerShare;
  }

  public void setFiveYNetIncomeGrowthPerShare(Double fiveYNetIncomeGrowthPerShare) {
    this.fiveYNetIncomeGrowthPerShare = fiveYNetIncomeGrowthPerShare;
  }

  public Double getThreeYNetIncomeGrowthPerShare() {
    return threeYNetIncomeGrowthPerShare;
  }

  public void setThreeYNetIncomeGrowthPerShare(Double threeYNetIncomeGrowthPerShare) {
    this.threeYNetIncomeGrowthPerShare = threeYNetIncomeGrowthPerShare;
  }

  public Double getTenYShareholdersEquityGrowthPerShare() {
    return tenYShareholdersEquityGrowthPerShare;
  }

  public void setTenYShareholdersEquityGrowthPerShare(Double tenYShareholdersEquityGrowthPerShare) {
    this.tenYShareholdersEquityGrowthPerShare = tenYShareholdersEquityGrowthPerShare;
  }

  public Double getFiveYShareholdersEquityGrowthPerShare() {
    return fiveYShareholdersEquityGrowthPerShare;
  }

  public void setFiveYShareholdersEquityGrowthPerShare(Double fiveYShareholdersEquityGrowthPerShare) {
    this.fiveYShareholdersEquityGrowthPerShare = fiveYShareholdersEquityGrowthPerShare;
  }

  public Double getThreeYShareholdersEquityGrowthPerShare() {
    return threeYShareholdersEquityGrowthPerShare;
  }

  public void setThreeYShareholdersEquityGrowthPerShare(Double threeYShareholdersEquityGrowthPerShare) {
    this.threeYShareholdersEquityGrowthPerShare = threeYShareholdersEquityGrowthPerShare;
  }

  public Double getTenYDividendperShareGrowthPerShare() {
    return tenYDividendperShareGrowthPerShare;
  }

  public void setTenYDividendperShareGrowthPerShare(Double tenYDividendperShareGrowthPerShare) {
    this.tenYDividendperShareGrowthPerShare = tenYDividendperShareGrowthPerShare;
  }

  public Double getFiveYDividendperShareGrowthPerShare() {
    return fiveYDividendperShareGrowthPerShare;
  }

  public void setFiveYDividendperShareGrowthPerShare(Double fiveYDividendperShareGrowthPerShare) {
    this.fiveYDividendperShareGrowthPerShare = fiveYDividendperShareGrowthPerShare;
  }

  public Double getThreeYDividendperShareGrowthPerShare() {
    return threeYDividendperShareGrowthPerShare;
  }

  public void setThreeYDividendperShareGrowthPerShare(Double threeYDividendperShareGrowthPerShare) {
    this.threeYDividendperShareGrowthPerShare = threeYDividendperShareGrowthPerShare;
  }

  public Double getReceivablesGrowth() {
    return receivablesGrowth;
  }

  public void setReceivablesGrowth(Double receivablesGrowth) {
    this.receivablesGrowth = receivablesGrowth;
  }

  public Double getInventoryGrowth() {
    return inventoryGrowth;
  }

  public void setInventoryGrowth(Double inventoryGrowth) {
    this.inventoryGrowth = inventoryGrowth;
  }

  public Double getAssetGrowth() {
    return assetGrowth;
  }

  public void setAssetGrowth(Double assetGrowth) {
    this.assetGrowth = assetGrowth;
  }

  public Double getBookValueperShareGrowth() {
    return bookValueperShareGrowth;
  }

  public void setBookValueperShareGrowth(Double bookValueperShareGrowth) {
    this.bookValueperShareGrowth = bookValueperShareGrowth;
  }

  public Double getDebtGrowth() {
    return debtGrowth;
  }

  public void setDebtGrowth(Double debtGrowth) {
    this.debtGrowth = debtGrowth;
  }

  public Double getRdexpenseGrowth() {
    return rdexpenseGrowth;
  }

  public void setRdexpenseGrowth(Double rdexpenseGrowth) {
    this.rdexpenseGrowth = rdexpenseGrowth;
  }

  public Double getSgaexpensesGrowth() {
    return sgaexpensesGrowth;
  }

  public void setSgaexpensesGrowth(Double sgaexpensesGrowth) {
    this.sgaexpensesGrowth = sgaexpensesGrowth;
  }

  public String getFinGrowthId() {
    return finGrowthId;
  }

  public void setFinGrowthId(String finGrowthId) {
    this.finGrowthId = finGrowthId;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }
}
