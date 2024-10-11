package org.rhm.stock.domain;

public class AveragePrice {
	private Integer daysCnt;
	private Integer avgVolume;
	private Double avgPrice;
	private Double emaPrice;
	private Double avgHighLowRange;
	private Double avgOpenCloseRange;
	public Integer getDaysCnt() {
		return daysCnt;
	}
	public void setDaysCnt(Integer daysCnt) {
		this.daysCnt = daysCnt;
	}
	public Integer getAvgVolume() {
		return avgVolume;
	}
	public void setAvgVolume(Integer avgVolume) {
		this.avgVolume = avgVolume;
	}
	public Double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}
	public Double getAvgHighLowRange() {
		return avgHighLowRange;
	}
	public void setAvgHighLowRange(Double avgHighLowRange) {
		this.avgHighLowRange = avgHighLowRange;
	}
	public Double getAvgOpenCloseRange() {
		return avgOpenCloseRange;
	}
	public void setAvgOpenCloseRange(Double avgOpenCloseRange) {
		this.avgOpenCloseRange = avgOpenCloseRange;
	}
	public Double getEmaPrice() {
		return emaPrice;
	}
	public void setEmaPrice(Double emaPrice) {
		this.emaPrice = emaPrice;
	}

}
