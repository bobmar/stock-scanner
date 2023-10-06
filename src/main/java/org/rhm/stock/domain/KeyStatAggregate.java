package org.rhm.stock.domain;

import java.util.ArrayList;
import java.util.List;

public class KeyStatAggregate {

	private String statKey = null;
	private String priceDate = null;
	private String keyStatName = null;
	private double lowValue = 999999999.9;
	private double highValue = -999999999.9;
	private double meanValue = 0.0;
	private double medianValue = 0.0;
	private long aboveMeanCnt = 0l;
	private long belowMeanCnt = 0l;
	private long aboveMedianCnt = 0l;
	private long belowMedianCnt = 0l;
	private long sampleCnt = 0l;
	private double sumValue = 0.0;
	private List<Double> valueList = new ArrayList<Double>();

	public String getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(String priceDate) {
		this.priceDate = priceDate;
	}
	public String getKeyStatName() {
		return keyStatName;
	}
	public void setKeyStatName(String keyStatName) {
		this.keyStatName = keyStatName;
	}
	public double getLowValue() {
		return lowValue;
	}
	public void setLowValue(double lowValue) {
		this.lowValue = lowValue;
	}
	public double getHighValue() {
		return highValue;
	}
	public void setHighValue(double highValue) {
		this.highValue = highValue;
	}
	public double getMeanValue() {
		return meanValue;
	}
	public void setMeanValue(double meanValue) {
		this.meanValue = meanValue;
	}
	public double getMedianValue() {
		return medianValue;
	}
	public void setMedianValue(double medianValue) {
		this.medianValue = medianValue;
	}
	public long getSampleCnt() {
		return sampleCnt;
	}
	public void setSampleCnt(long sampleCnt) {
		this.sampleCnt = sampleCnt;
	}
	public long getAboveMeanCnt() {
		return aboveMeanCnt;
	}
	public void setAboveMeanCnt(long aboveMeanCnt) {
		this.aboveMeanCnt = aboveMeanCnt;
	}
	public long getBelowMeanCnt() {
		return belowMeanCnt;
	}
	public void setBelowMeanCnt(long belowMeanCnt) {
		this.belowMeanCnt = belowMeanCnt;
	}
	public long getAboveMedianCnt() {
		return aboveMedianCnt;
	}
	public void setAboveMedianCnt(long aboveMedianCnt) {
		this.aboveMedianCnt = aboveMedianCnt;
	}
	public long getBelowMedianCnt() {
		return belowMedianCnt;
	}
	public void setBelowMedianCnt(long belowMedianCnt) {
		this.belowMedianCnt = belowMedianCnt;
	}
	public String getStatKey() {
		return statKey;
	}
	public void setStatKey(String statKey) {
		this.statKey = statKey;
	}
	public void incAboveMeanCnt() {
		this.aboveMeanCnt++;
	}
	public void incBelowMeanCnt() {
		this.belowMeanCnt++;
	}
	public void incAboveMedianCnt() {
		this.aboveMedianCnt++;
	}
	public void incBelowMedianCnt() {
		this.belowMedianCnt++;
	}
	public void incSampleCnt() {
		this.sampleCnt++;
	}
	public double getSumValue() {
		return sumValue;
	}
	public void setSumValue(double sumValue) {
		this.sumValue = sumValue;
	}
	public void incSumValue(double value) {
		sumValue += value;
	}
	public List<Double> getValueList() {
		return valueList;
	}
	public void setValueList(List<Double> valueList) {
		this.valueList = valueList;
	}
	public void addValue(Double value) {
		this.valueList.add(value);
	}
}
