package org.rhm.stock.domain;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

public class YahooKeyStatistic {
	@Id
	private String keyStatId;
	private String tickerSymbol;
	private LocalDate priceDate;
    private Double enterpriseValue;
    private Double forwardPE;
    private Double profitMargins;
    private Double floatShares;
    private Double sharesOutstanding;
    private Double sharesShort;
    private Double sharesShortPriorMonth;
    private LocalDate sharesShortPreviousMonthDate;
    private LocalDate dateShortInterest;
    private Double sharesPercentSharesOut;
    private Double heldPercentInsiders;
    private Double heldPercentInstitutions;
    private Double shortRatio;
    private Double shortPercentOfFloat;
    private Double beta;
    private Double morningStarOverallRating;
    private Double morningStarRiskRating;
    private String category;
    private Double bookValue;
    private Double priceToBook;
    private Double annualReportExpenseRatio;
    private Double ytdReturn;
    private Double beta3Year;
    private Double totalAssets;
    private Double yield;
    private String fundFamily;
    private LocalDate fundInceptionDate;
    private String legalType;
    private Double threeYearAverageReturn;
    private Double fiveYearAverageReturn;
    private Double priceToSalesTrailing12Months;
    private LocalDate lastFiscalYearEnd;
    private LocalDate nextFiscalYearEnd;
    private LocalDate mostRecentQuarter;
    private Double earningsQuarterlyGrowth;
    private Double revenueQuarterlyGrowth;
    private Double netIncomeToCommon;
    private Double trailingEps;
    private Double forwardEps;
    private Double pegRatio;
    private String lastSplitFactor;
    private LocalDate lastSplitDate;
    private Double enterpriseToRevenue;
    private Double enterpriseToEbitda;
    private Double s52WeekChange;
    private Double SandP52WeekChange;
    private Double lastDividendValue;
    private Double lastCapGain;
    private Double annualHoldingsTurnover;
    private String maxAge;
    private Double currentPrice;
    private Double targetHighPrice;
    private Double targetLowPrice;
    private Double targetMeanPrice;
    private Double targetMedianPrice;
    private Double recommendationMean;
    private String recommendationKey;
    private Double numberOfAnalystOpinions;
    private Double totalCash;
    private Double totalCashPerShare;
    private Double ebitda;
    private Double totalDebt;
    private Double quickRatio;
    private Double currentRatio;
    private Double totalRevenue;
    private Double debtToEquity;
    private Double revenuePerShare;
    private Double returnOnAssets;
    private Double returnOnEquity;
    private Double grossProfits;
    private Double freeCashflow;
    private Double operatingCashflow;
    private Double earningsGrowth;
    private Double revenueGrowth;
    private Double grossMargins;
    private Double ebitdaMargins;
    private Double operatingMargins;
    private Double profitMargins2;
    private String financialCurrency;
    private LocalDate createDate;
	public Double getEnterpriseValue() {
		return enterpriseValue;
	}
	public void setEnterpriseValue(Double enterpriseValue) {
		this.enterpriseValue = enterpriseValue;
	}
	public Double getForwardPE() {
		return forwardPE;
	}
	public void setForwardPE(Double forwardPE) {
		this.forwardPE = forwardPE;
	}
	public Double getProfitMargins() {
		return profitMargins;
	}
	public void setProfitMargins(Double profitMargins) {
		this.profitMargins = profitMargins;
	}
	public Double getFloatShares() {
		return floatShares;
	}
	public void setFloatShares(Double floatShares) {
		this.floatShares = floatShares;
	}
	public Double getSharesOutstanding() {
		return sharesOutstanding;
	}
	public void setSharesOutstanding(Double sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}
	public Double getSharesShort() {
		return sharesShort;
	}
	public void setSharesShort(Double sharesShort) {
		this.sharesShort = sharesShort;
	}
	public Double getSharesShortPriorMonth() {
		return sharesShortPriorMonth;
	}
	public void setSharesShortPriorMonth(Double sharesShortPriorMonth) {
		this.sharesShortPriorMonth = sharesShortPriorMonth;
	}
	public LocalDate getSharesShortPreviousMonthDate() {
		return sharesShortPreviousMonthDate;
	}
	public void setSharesShortPreviousMonthDate(LocalDate sharesShortPreviousMonthDate) {
		this.sharesShortPreviousMonthDate = sharesShortPreviousMonthDate;
	}
	public LocalDate getDateShortInterest() {
		return dateShortInterest;
	}
	public void setDateShortInterest(LocalDate dateShortInterest) {
		this.dateShortInterest = dateShortInterest;
	}
	public Double getSharesPercentSharesOut() {
		return sharesPercentSharesOut;
	}
	public void setSharesPercentSharesOut(Double sharesPercentSharesOut) {
		this.sharesPercentSharesOut = sharesPercentSharesOut;
	}
	public Double getHeldPercentInsiders() {
		return heldPercentInsiders;
	}
	public void setHeldPercentInsiders(Double heldPercentInsiders) {
		this.heldPercentInsiders = heldPercentInsiders;
	}
	public Double getHeldPercentInstitutions() {
		return heldPercentInstitutions;
	}
	public void setHeldPercentInstitutions(Double heldPercentInstitutions) {
		this.heldPercentInstitutions = heldPercentInstitutions;
	}
	public Double getShortRatio() {
		return shortRatio;
	}
	public void setShortRatio(Double shortRatio) {
		this.shortRatio = shortRatio;
	}
	public Double getShortPercentOfFloat() {
		return shortPercentOfFloat;
	}
	public void setShortPercentOfFloat(Double shortPercentOfFloat) {
		this.shortPercentOfFloat = shortPercentOfFloat;
	}
	public Double getBeta() {
		return beta;
	}
	public void setBeta(Double beta) {
		this.beta = beta;
	}
	public Double getMorningStarOverallRating() {
		return morningStarOverallRating;
	}
	public void setMorningStarOverallRating(Double morningStarOverallRating) {
		this.morningStarOverallRating = morningStarOverallRating;
	}
	public Double getMorningStarRiskRating() {
		return morningStarRiskRating;
	}
	public void setMorningStarRiskRating(Double morningStarRiskRating) {
		this.morningStarRiskRating = morningStarRiskRating;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Double getBookValue() {
		return bookValue;
	}
	public void setBookValue(Double bookValue) {
		this.bookValue = bookValue;
	}
	public Double getPriceToBook() {
		return priceToBook;
	}
	public void setPriceToBook(Double priceToBook) {
		this.priceToBook = priceToBook;
	}
	public Double getAnnualReportExpenseRatio() {
		return annualReportExpenseRatio;
	}
	public void setAnnualReportExpenseRatio(Double annualReportExpenseRatio) {
		this.annualReportExpenseRatio = annualReportExpenseRatio;
	}
	public Double getYtdReturn() {
		return ytdReturn;
	}
	public void setYtdReturn(Double ytdReturn) {
		this.ytdReturn = ytdReturn;
	}
	public Double getBeta3Year() {
		return beta3Year;
	}
	public void setBeta3Year(Double beta3Year) {
		this.beta3Year = beta3Year;
	}
	public Double getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(Double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public Double getYield() {
		return yield;
	}
	public void setYield(Double yield) {
		this.yield = yield;
	}
	public String getFundFamily() {
		return fundFamily;
	}
	public void setFundFamily(String fundFamily) {
		this.fundFamily = fundFamily;
	}
	public LocalDate getFundInceptionDate() {
		return fundInceptionDate;
	}
	public void setFundInceptionDate(LocalDate fundInceptionDate) {
		this.fundInceptionDate = fundInceptionDate;
	}
	public String getLegalType() {
		return legalType;
	}
	public void setLegalType(String legalType) {
		this.legalType = legalType;
	}
	public Double getThreeYearAverageReturn() {
		return threeYearAverageReturn;
	}
	public void setThreeYearAverageReturn(Double threeYearAverageReturn) {
		this.threeYearAverageReturn = threeYearAverageReturn;
	}
	public Double getFiveYearAverageReturn() {
		return fiveYearAverageReturn;
	}
	public void setFiveYearAverageReturn(Double fiveYearAverageReturn) {
		this.fiveYearAverageReturn = fiveYearAverageReturn;
	}
	public Double getPriceToSalesTrailing12Months() {
		return priceToSalesTrailing12Months;
	}
	public void setPriceToSalesTrailing12Months(Double priceToSalesTrailing12Months) {
		this.priceToSalesTrailing12Months = priceToSalesTrailing12Months;
	}
	public LocalDate getLastFiscalYearEnd() {
		return lastFiscalYearEnd;
	}
	public void setLastFiscalYearEnd(LocalDate lastFiscalYearEnd) {
		this.lastFiscalYearEnd = lastFiscalYearEnd;
	}
	public LocalDate getNextFiscalYearEnd() {
		return nextFiscalYearEnd;
	}
	public void setNextFiscalYearEnd(LocalDate nextFiscalYearEnd) {
		this.nextFiscalYearEnd = nextFiscalYearEnd;
	}
	public LocalDate getMostRecentQuarter() {
		return mostRecentQuarter;
	}
	public void setMostRecentQuarter(LocalDate mostRecentQuarter) {
		this.mostRecentQuarter = mostRecentQuarter;
	}
	public Double getEarningsQuarterlyGrowth() {
		return earningsQuarterlyGrowth;
	}
	public void setEarningsQuarterlyGrowth(Double earningsQuarterlyGrowth) {
		this.earningsQuarterlyGrowth = earningsQuarterlyGrowth;
	}
	public Double getRevenueQuarterlyGrowth() {
		return revenueQuarterlyGrowth;
	}
	public void setRevenueQuarterlyGrowth(Double revenueQuarterlyGrowth) {
		this.revenueQuarterlyGrowth = revenueQuarterlyGrowth;
	}
	public Double getTrailingEps() {
		return trailingEps;
	}
	public void setTrailingEps(Double trailingEps) {
		this.trailingEps = trailingEps;
	}
	public Double getForwardEps() {
		return forwardEps;
	}
	public void setForwardEps(Double forwardEps) {
		this.forwardEps = forwardEps;
	}
	public Double getPegRatio() {
		return pegRatio;
	}
	public void setPegRatio(Double pegRatio) {
		this.pegRatio = pegRatio;
	}
	public String getLastSplitFactor() {
		return lastSplitFactor;
	}
	public void setLastSplitFactor(String lastSplitFactor) {
		this.lastSplitFactor = lastSplitFactor;
	}
	public LocalDate getLastSplitDate() {
		return lastSplitDate;
	}
	public void setLastSplitDate(LocalDate lastSplitDate) {
		this.lastSplitDate = lastSplitDate;
	}
	public Double getEnterpriseToRevenue() {
		return enterpriseToRevenue;
	}
	public void setEnterpriseToRevenue(Double enterpriseToRevenue) {
		this.enterpriseToRevenue = enterpriseToRevenue;
	}
	public Double getEnterpriseToEbitda() {
		return enterpriseToEbitda;
	}
	public void setEnterpriseToEbitda(Double enterpriseToEbitda) {
		this.enterpriseToEbitda = enterpriseToEbitda;
	}
	public Double getSandP52WeekChange() {
		return SandP52WeekChange;
	}
	public void setSandP52WeekChange(Double sandP52WeekChange) {
		SandP52WeekChange = sandP52WeekChange;
	}
	public Double getLastDividendValue() {
		return lastDividendValue;
	}
	public void setLastDividendValue(Double lastDividendValue) {
		this.lastDividendValue = lastDividendValue;
	}
	public Double getLastCapGain() {
		return lastCapGain;
	}
	public void setLastCapGain(Double lastCapGain) {
		this.lastCapGain = lastCapGain;
	}
	public Double getAnnualHoldingsTurnover() {
		return annualHoldingsTurnover;
	}
	public void setAnnualHoldingsTurnover(Double annualHoldingsTurnover) {
		this.annualHoldingsTurnover = annualHoldingsTurnover;
	}
	public String getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	public Double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
	public Double getTargetHighPrice() {
		return targetHighPrice;
	}
	public void setTargetHighPrice(Double targetHighPrice) {
		this.targetHighPrice = targetHighPrice;
	}
	public Double getTargetLowPrice() {
		return targetLowPrice;
	}
	public void setTargetLowPrice(Double targetLowPrice) {
		this.targetLowPrice = targetLowPrice;
	}
	public Double getTargetMeanPrice() {
		return targetMeanPrice;
	}
	public void setTargetMeanPrice(Double targetMeanPrice) {
		this.targetMeanPrice = targetMeanPrice;
	}
	public Double getTargetMedianPrice() {
		return targetMedianPrice;
	}
	public void setTargetMedianPrice(Double targetMedianPrice) {
		this.targetMedianPrice = targetMedianPrice;
	}
	public Double getRecommendationMean() {
		return recommendationMean;
	}
	public void setRecommendationMean(Double recommendationMean) {
		this.recommendationMean = recommendationMean;
	}
	public String getRecommendationKey() {
		return recommendationKey;
	}
	public void setRecommendationKey(String recommendationKey) {
		this.recommendationKey = recommendationKey;
	}
	public Double getNumberOfAnalystOpinions() {
		return numberOfAnalystOpinions;
	}
	public void setNumberOfAnalystOpinions(Double numberOfAnalystOpinions) {
		this.numberOfAnalystOpinions = numberOfAnalystOpinions;
	}
	public Double getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}
	public Double getTotalCashPerShare() {
		return totalCashPerShare;
	}
	public void setTotalCashPerShare(Double totalCashPerShare) {
		this.totalCashPerShare = totalCashPerShare;
	}
	public Double getEbitda() {
		return ebitda;
	}
	public void setEbitda(Double ebitda) {
		this.ebitda = ebitda;
	}
	public Double getTotalDebt() {
		return totalDebt;
	}
	public void setTotalDebt(Double totalDebt) {
		this.totalDebt = totalDebt;
	}
	public Double getQuickRatio() {
		return quickRatio;
	}
	public void setQuickRatio(Double quickRatio) {
		this.quickRatio = quickRatio;
	}
	public Double getCurrentRatio() {
		return currentRatio;
	}
	public void setCurrentRatio(Double currentRatio) {
		this.currentRatio = currentRatio;
	}
	public Double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public Double getDebtToEquity() {
		return debtToEquity;
	}
	public void setDebtToEquity(Double debtToEquity) {
		this.debtToEquity = debtToEquity;
	}
	public Double getRevenuePerShare() {
		return revenuePerShare;
	}
	public void setRevenuePerShare(Double revenuePerShare) {
		this.revenuePerShare = revenuePerShare;
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
	public Double getGrossProfits() {
		return grossProfits;
	}
	public void setGrossProfits(Double grossProfits) {
		this.grossProfits = grossProfits;
	}
	public Double getFreeCashflow() {
		return freeCashflow;
	}
	public void setFreeCashflow(Double freeCashflow) {
		this.freeCashflow = freeCashflow;
	}
	public Double getEarningsGrowth() {
		return earningsGrowth;
	}
	public void setEarningsGrowth(Double earningsGrowth) {
		this.earningsGrowth = earningsGrowth;
	}
	public Double getRevenueGrowth() {
		return revenueGrowth;
	}
	public void setRevenueGrowth(Double revenueGrowth) {
		this.revenueGrowth = revenueGrowth;
	}
	public Double getGrossMargins() {
		return grossMargins;
	}
	public void setGrossMargins(Double grossMargins) {
		this.grossMargins = grossMargins;
	}
	public Double getEbitdaMargins() {
		return ebitdaMargins;
	}
	public void setEbitdaMargins(Double ebitdaMargins) {
		this.ebitdaMargins = ebitdaMargins;
	}
	public Double getOperatingMargins() {
		return operatingMargins;
	}
	public void setOperatingMargins(Double operatingMargins) {
		this.operatingMargins = operatingMargins;
	}
	public Double getProfitMargins2() {
		return profitMargins2;
	}
	public void setProfitMargins2(Double profitMargins2) {
		this.profitMargins2 = profitMargins2;
	}
	public String getFinancialCurrency() {
		return financialCurrency;
	}
	public void setFinancialCurrency(String financialCurrency) {
		this.financialCurrency = financialCurrency;
	}
	public String getKeyStatId() {
		return keyStatId;
	}
	public void setKeyStatId(String keyStatId) {
		this.keyStatId = keyStatId;
	}
	public LocalDate getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	public String getTickerSymbol() {
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}
	public Double getNetIncomeToCommon() {
		return netIncomeToCommon;
	}
	public void setNetIncomeToCommon(Double netIncomeToCommon) {
		this.netIncomeToCommon = netIncomeToCommon;
	}
	public Double getS52WeekChange() {
		return s52WeekChange;
	}
	public void setS52WeekChange(Double s52WeekChange) {
		this.s52WeekChange = s52WeekChange;
	}
	public Double getOperatingCashflow() {
		return operatingCashflow;
	}
	public void setOperatingCashflow(Double operatingCashflow) {
		this.operatingCashflow = operatingCashflow;
	}
	public LocalDate getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(LocalDate priceDate) {
		this.priceDate = priceDate;
	}

}
