package org.rhm.stock.handler.aggregate;

import org.rhm.stock.domain.KeyStatAggregate;
import org.rhm.stock.domain.YahooKeyStatistic;
import org.rhm.stock.service.KeyStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
@Qualifier("yahooStatAggr")
@Deprecated
public class YahooKeyStatAggr implements AggregatorHandler {
	private Logger logger = LoggerFactory.getLogger(YahooKeyStatAggr.class);
	@Autowired
	private KeyStatService ksSvc = null;
    private String[] statAttr = { 
    	"revenueQuarterlyGrowth"
    	,"pegRatio"
    	,"enterpriseToRevenue"
    	,"quickRatio"
    	,"currentRatio"
    	,"debtToEquity"
    	,"returnOnAssets"
    	,"returnOnEquity"
    	,"earningsGrowth"
    	,"revenueGrowth"
    	,"grossMargins"
    	,"ebitdaMargins"
    	,"operatingMargins"
    };
	
	@Override
	public void process() {
		Map<String,KeyStatAggregate> ksAggMap = new HashMap<String,KeyStatAggregate>();
		List<YahooKeyStatistic> ksList = ksSvc.retrieveStats();
		KeyStatAggregate ksAgg = null;
		String statKey = null;
		for (YahooKeyStatistic ks: ksList) {
			for (String stat: statAttr) {
				statKey = ks.getPriceDate().format(DateTimeFormatter.ISO_DATE) + ":" + stat;
				ksAgg = ksAggMap.get(statKey);
				if (ksAgg == null) {
					ksAgg = new KeyStatAggregate();
					ksAgg.setStatKey(statKey);
					ksAggMap.put(statKey, ksAgg);
				}
				this.accumStats(ksAgg, ks, stat);
			}
		}
		List<String> ksMapKeys = ksAggMap.keySet().stream().sorted().collect(Collectors.toList());
		ksMapKeys.forEach(k->{
			KeyStatAggregate ks = ksAggMap.get(k);
			if (ks.getValueList().size() > 0) {
				this.calcAggrStats(ks);
				logger.debug(ks.getStatKey() + ": AbvMean=" + ks.getAboveMeanCnt()
					+ " BlwMean=" + ks.getBelowMeanCnt()
					+ " Mean=" + ks.getMeanValue()
					+ " Med=" + ks.getMedianValue()
					+ " Min=" + ks.getLowValue()
					+ " Max=" + ks.getHighValue()
					);
			}
			else {
				logger.warn("process - " + ks.getStatKey() + " has no values");
			}
		});
	}

	private KeyStatAggregate accumStats(KeyStatAggregate ksAggr, YahooKeyStatistic ks, String stat) {
		KeyStatAggregate agg = ksAggr;
		ksAggr.incSampleCnt();
		switch (stat) {
			case "revenueQuarterlyGrowth":
				if (ks.getRevenueGrowth() != null) {
					ksAggr.addValue(ks.getRevenueGrowth());
				}
				break;
			case "pegRatio":
				if (ks.getPegRatio() != null) {
					ksAggr.addValue(ks.getPegRatio());
				}
				break;
			case "enterpriseToRevenue":
				if (ks.getEnterpriseToRevenue() != null) {
					ksAggr.addValue(ks.getEnterpriseToRevenue());
				}
				break;
			case "quickRatio":
				if (ks.getQuickRatio() != null) {
					ksAggr.addValue(ks.getQuickRatio());
				}
				break;
			case "currentRatio":
				if (ks.getCurrentRatio() != null) {
					ksAggr.addValue(ks.getCurrentRatio());
				}
				break;
			case "debtToEquity":
				if (ks.getDebtToEquity() != null) {
					ksAggr.addValue(ks.getDebtToEquity());
				}
				break;
			case "returnOnAssets":
				if (ks.getReturnOnAssets() != null) {
					ksAggr.addValue(ks.getReturnOnAssets());
				}
				break;
			case "returnOnEquity":
				if (ks.getReturnOnEquity() != null) {
					ksAggr.addValue(ks.getReturnOnEquity());
				}
				break;
			case "earningsGrowth":
				if (ks.getEarningsGrowth() != null) {
					ksAggr.addValue(ks.getEarningsGrowth());
				}
				break;
			case "revenueGrowth":
				if (ks.getRevenueGrowth() != null) {
					ksAggr.addValue(ks.getRevenueGrowth());
				}
				break;
			case "grossMargins":
				if (ks.getGrossMargins() != null) {
					ksAggr.addValue(ks.getGrossMargins());
				}
				break;
			case "ebitdaMargins":
				if (ks.getEbitdaMargins() != null) {
					ksAggr.addValue(ks.getEbitdaMargins());
				}
				break;
			case "operatingMargins":
				if (ks.getOperatingMargins() != null) {
					ksAggr.addValue(ks.getOperatingMargins());
				}
				break;
		}
		return agg;
	}
	
	private KeyStatAggregate calcAggrStats(KeyStatAggregate ksAggr) {
		KeyStatAggregate agg = ksAggr;
		List<Double> statValueList = ksAggr.getValueList().stream().sorted().collect(Collectors.toList());
		ksAggr.setLowValue(statValueList.get(0).doubleValue());
		ksAggr.setHighValue(statValueList.get(statValueList.size()-1).doubleValue());
		int medianIndex = statValueList.size() / 2;
		ksAggr.setMedianValue(statValueList.get(medianIndex).doubleValue());
		statValueList.forEach(s->{
			ksAggr.incSampleCnt();
			ksAggr.incSumValue(s.doubleValue());
		});
		ksAggr.setMeanValue(ksAggr.getSumValue() / ksAggr.getSampleCnt());
		statValueList.forEach(s->{
			if (s.doubleValue() < ksAggr.getMeanValue()) {
				ksAggr.incBelowMeanCnt();
			}
			else {
				if (s.doubleValue() >= ksAggr.getMeanValue()) {
					ksAggr.incAboveMeanCnt();
				}
			}
			if (s.doubleValue() < ksAggr.getMedianValue()) {
				ksAggr.incBelowMedianCnt();
			}
			else {
				if (s.doubleValue() >= ksAggr.getMedianValue()) {
					ksAggr.incAboveMedianCnt();
				}
			}
		});
		return agg;
	}
}
