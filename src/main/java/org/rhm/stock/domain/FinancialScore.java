package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

public class FinancialScore {
    @Id
    private String id;
    private String symbol;
    private String reportedCurrency;
    private Double altmanZScore;
    private Integer piotroskiScore;
    private Long workingCapital;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getReportedCurrency() {
        return reportedCurrency;
    }

    public void setReportedCurrency(String reportedCurrency) {
        this.reportedCurrency = reportedCurrency;
    }

    public Double getAltmanZScore() {
        return altmanZScore;
    }

    public void setAltmanZScore(Double altmanZScore) {
        this.altmanZScore = altmanZScore;
    }

    public Integer getPiotroskiScore() {
        return piotroskiScore;
    }

    public void setPiotroskiScore(Integer piotroskiScore) {
        this.piotroskiScore = piotroskiScore;
    }

    public Long getWorkingCapital() {
        return workingCapital;
    }

    public void setWorkingCapital(Long workingCapital) {
        this.workingCapital = workingCapital;
    }

    public Long getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Long totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Long getRetainedEarnings() {
        return retainedEarnings;
    }

    public void setRetainedEarnings(Long retainedEarnings) {
        this.retainedEarnings = retainedEarnings;
    }

    public Long getEbit() {
        return ebit;
    }

    public void setEbit(Long ebit) {
        this.ebit = ebit;
    }

    public Long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public Long getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(Long totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    private Long totalAssets;
    private Long retainedEarnings;
    private Long ebit;
    private Long marketCap;
    private Long totalLiabilities;
    private Long revenue;

}
