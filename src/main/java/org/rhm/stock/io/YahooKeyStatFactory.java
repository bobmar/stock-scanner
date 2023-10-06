package org.rhm.stock.io;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.rhm.stock.domain.YahooKeyStatistic;

public class YahooKeyStatFactory {

	private static Double extractRaw(String key, Map<String,Object> value ) {
		Double rawValue = null;
		if (value != null) {
			Map<String,Object> valueMap = (Map<String,Object>) value.get(key);
			if (valueMap != null) {
				if (valueMap.get("raw") != null) {
					if (valueMap.get("raw") instanceof Long) {
						rawValue = Double.valueOf(((Long)valueMap.get("raw")).doubleValue());
					}
					else if (valueMap.get("raw") instanceof Integer) {
						rawValue = Double.valueOf(((Integer)valueMap.get("raw")).doubleValue());
					}
					else if (valueMap.get("raw") instanceof Double) {
						rawValue = Double.valueOf(((Double)valueMap.get("raw")).doubleValue());
					}
				}
			}
		}
		return rawValue;
	}

	private static LocalDate extractDate(String key, Map<String,Object> value) {
		LocalDate date = null;
		if (value != null) {
			Map<String,Object> valueMap = (Map<String,Object>)value.get(key);
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("GMT-7"));
			if (valueMap != null) {
				String dateStr = (String)valueMap.get("fmt");
				if (dateStr != null) {
					date =  LocalDate.parse(dateStr, fmt);
				}
				
			}
		}
		return date;
	}
	
	private static Double extractPct(String key, Map<String,Object> value) {
		Double pctValue = null;
		if (value != null) {
			Double rawValue = extractRaw(key, value);
			if (rawValue != null) {
				pctValue = rawValue * 100;
			}
		}
		return pctValue;
	}
	
	private static YahooKeyStatistic extractDefault(YahooKeyStatistic keyStat, Map<String,Object> defaultKeyStat) {
		if (defaultKeyStat != null) {
			keyStat.setEnterpriseValue(extractRaw("enterpriseValue", defaultKeyStat));
			keyStat.setForwardPE(extractRaw("forwardPE", defaultKeyStat));
			keyStat.setProfitMargins(extractRaw("profitMargins", defaultKeyStat));
			keyStat.setFloatShares(extractRaw("floatShares", defaultKeyStat));
			keyStat.setSharesOutstanding(extractRaw("sharesOutstanding", defaultKeyStat));
			keyStat.setSharesShort(extractRaw("sharesShort", defaultKeyStat));
			keyStat.setSharesShortPriorMonth(extractRaw("sharesShortPriorMonth", defaultKeyStat));
			keyStat.setSharesShortPreviousMonthDate(extractDate("sharesShortPreviousMonthDate", defaultKeyStat));
			keyStat.setDateShortInterest(extractDate("dateShortInterest", defaultKeyStat));
			keyStat.setSharesPercentSharesOut(extractPct("sharesPercentSharesOut", defaultKeyStat));
			keyStat.setHeldPercentInsiders(extractPct("heldPercentInsiders", defaultKeyStat));
			keyStat.setHeldPercentInstitutions(extractPct("heldPercentInstitutions", defaultKeyStat));
			keyStat.setShortRatio(extractRaw("shortRatio", defaultKeyStat));
			keyStat.setShortPercentOfFloat(extractPct("shortPercentOfFloat", defaultKeyStat));
			keyStat.setBeta(extractRaw("beta", defaultKeyStat));
			keyStat.setMorningStarOverallRating(extractRaw("morningStarOverallRating", defaultKeyStat));
			keyStat.setMorningStarRiskRating(extractRaw("morningStarRiskRating", defaultKeyStat));
			keyStat.setCategory((String)defaultKeyStat.get("category"));
			keyStat.setBookValue(extractRaw("bookValue", defaultKeyStat));
			keyStat.setPriceToBook(extractRaw("priceToBook", defaultKeyStat));
			keyStat.setAnnualReportExpenseRatio(extractRaw("annualReportExpenseRatio", defaultKeyStat));
			keyStat.setYtdReturn(extractRaw("ytdReturn", defaultKeyStat));
			keyStat.setBeta3Year(extractRaw("beta3Year", defaultKeyStat));
			keyStat.setTotalAssets(extractRaw("totalAssets", defaultKeyStat));
			keyStat.setYield(extractRaw("yield", defaultKeyStat));
			keyStat.setFundFamily((String)defaultKeyStat.get("fundFamily"));
			keyStat.setFundInceptionDate(extractDate("fundInceptionDate", defaultKeyStat));
			keyStat.setLegalType((String)defaultKeyStat.get("legalType"));
			keyStat.setThreeYearAverageReturn(extractRaw("threeYearAverageReturn", defaultKeyStat));
			keyStat.setFiveYearAverageReturn(extractRaw("fiveYearAverageReturn", defaultKeyStat));
			keyStat.setPriceToSalesTrailing12Months(extractRaw("priceToSalesTrailing12Months", defaultKeyStat));
			keyStat.setLastFiscalYearEnd(extractDate("lastFiscalYearEnd", defaultKeyStat));
			keyStat.setNextFiscalYearEnd(extractDate("nextFiscalYearEnd", defaultKeyStat));
			keyStat.setMostRecentQuarter(extractDate("mostRecentQuarter", defaultKeyStat));
			keyStat.setEarningsQuarterlyGrowth(extractRaw("earningsQuarterlyGrowth", defaultKeyStat));
			keyStat.setRevenueQuarterlyGrowth(extractRaw("revenueQuarterlyGrowth", defaultKeyStat));
			keyStat.setNetIncomeToCommon(extractRaw("netIncomeToCommon", defaultKeyStat));
			keyStat.setTrailingEps(extractRaw("trailingEps", defaultKeyStat));
			keyStat.setForwardEps(extractRaw("forwardEps", defaultKeyStat));
			keyStat.setPegRatio(extractRaw("pegRatio", defaultKeyStat));
			keyStat.setLastSplitFactor((String)defaultKeyStat.get("lastSplitFactor"));
			keyStat.setLastSplitDate(extractDate("lastSplitDate", defaultKeyStat));
			keyStat.setEnterpriseToRevenue(extractRaw("enterpriseToRevenue", defaultKeyStat));
			keyStat.setEnterpriseToEbitda(extractRaw("enterpriseToEbitda", defaultKeyStat));
			keyStat.setS52WeekChange(extractPct("52WeekChange", defaultKeyStat));
			keyStat.setSandP52WeekChange(extractPct("SandP52WeekChange", defaultKeyStat));
			keyStat.setLastDividendValue(extractRaw("lastDividendValue", defaultKeyStat));
			keyStat.setLastCapGain(extractRaw("lastCapGain", defaultKeyStat));
			keyStat.setAnnualHoldingsTurnover(extractRaw("annualHoldingsTurnover", defaultKeyStat));
		}
		return keyStat;
	}
	
	private static YahooKeyStatistic extractFinancialData(YahooKeyStatistic keyStat, Map<String,Object> financialData) {
		if (financialData != null) {
	        keyStat.setCurrentPrice(extractRaw("currentPrice", financialData));
	        keyStat.setTargetHighPrice(extractRaw("targetHighPrice", financialData));
	        keyStat.setTargetLowPrice(extractRaw("targetLowPrice", financialData));
	        keyStat.setTargetMeanPrice(extractRaw("targetMeanPrice", financialData));
	        keyStat.setTargetMedianPrice(extractRaw("targetMedianPrice", financialData));
	        keyStat.setRecommendationMean(extractRaw("recommendationMean", financialData));
	        keyStat.setRecommendationKey((String)financialData.get("recommendationKey"));
	        keyStat.setNumberOfAnalystOpinions(extractRaw("numberOfAnalystOpinions", financialData));
	        keyStat.setTotalCash(extractRaw("totalCash", financialData));
	        keyStat.setTotalCashPerShare(extractRaw("totalCashPerShare", financialData));
	        keyStat.setEbitda(extractRaw("ebitda", financialData));
	        keyStat.setTotalDebt(extractRaw("totalDebt", financialData));
	        keyStat.setQuickRatio(extractRaw("quickRatio", financialData));
	        keyStat.setCurrentRatio(extractRaw("currentRatio", financialData));
	        keyStat.setTotalRevenue(extractRaw("totalRevenue", financialData));
	        keyStat.setDebtToEquity(extractRaw("debtToEquity", financialData));
	        keyStat.setRevenuePerShare(extractRaw("revenuePerShare", financialData));
	        keyStat.setReturnOnAssets(extractPct("returnOnAssets", financialData));
	        keyStat.setReturnOnEquity(extractPct("returnOnEquity", financialData));
	        keyStat.setGrossProfits(extractRaw("grossProfits", financialData));
	        keyStat.setFreeCashflow(extractRaw("freeCashflow", financialData));
	        keyStat.setOperatingCashflow(extractRaw("operatingCashflow", financialData));
	        keyStat.setEarningsGrowth(extractPct("earningsGrowth", financialData));
	        keyStat.setRevenueGrowth(extractPct("revenueGrowth", financialData));
	        keyStat.setGrossMargins(extractPct("grossMargins", financialData));
	        keyStat.setEbitdaMargins(extractPct("ebitdaMargins", financialData));
	        keyStat.setOperatingMargins(extractPct("operatingMargins", financialData));
	        keyStat.setProfitMargins(extractPct("profitMargins", financialData));
	        keyStat.setFinancialCurrency((String)financialData.get("financialCurrency"));
		}
		return keyStat;
	}
	
	public static YahooKeyStatistic createKeyStat(String tickerSymbol, Map<String,Object> keyStatData) {
		YahooKeyStatistic yks = new YahooKeyStatistic();
		yks.setTickerSymbol(tickerSymbol);
		yks.setCreateDate(LocalDate.now());
		yks.setKeyStatId(tickerSymbol + "|" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(yks.getCreateDate()));
		if (keyStatData != null) {
			Map<String,Object> quoteSummary = (Map<String,Object>)keyStatData.get("quoteSummary");
			List<Map<String,Object>> result = null;
			Map<String,Object> statMap = null;
			if (quoteSummary != null) {
				result = (List<Map<String,Object>>)quoteSummary.get("result");
				statMap = result.get(0);
				if (statMap != null) {
					extractDefault(yks, ((Map<String,Object>)statMap.get("defaultKeyStatistics")));
					extractFinancialData(yks, ((Map<String,Object>)statMap.get("financialData")));
				}
			}
		}
		return yks;
	}
}
