package org.rhm.stock.controller.dto;

import java.util.List;

public class SignalRequest {

	private List<String> signalTypeList = null;
	private Integer lookBackDays = null;
	private String signalType = null;
	private String signalDate = null;
	private String overlaySignalType = null;

	public List<String> getSignalTypeList() {
		return signalTypeList;
	}

	public void setSignalTypeList(List<String> signalTypeList) {
		this.signalTypeList = signalTypeList;
	}

	public Integer getLookBackDays() {
		return lookBackDays;
	}

	public void setLookBackDays(Integer lookBackDays) {
		this.lookBackDays = lookBackDays;
	}

	public String getSignalType() {
		return signalType;
	}

	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}

	public String getSignalDate() {
		return signalDate;
	}

	public void setSignalDate(String priceDate) {
		this.signalDate = priceDate;
	}

	public String getOverlaySignalType() {
		return overlaySignalType;
	}

	public void setOverlaySignalType(String overlaySignalType) {
		this.overlaySignalType = overlaySignalType;
	}
	

}
