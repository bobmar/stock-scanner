package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

public class SignalTypeCount {
	@Id
	private String signalCountKey;
	private String signalDate;
	private String signalCode;
	private String signalDesc;
	private int signalCount = 0;
	
	public String getSignalDate() {
		return signalDate;
	}
	public void setSignalDate(String signalDate) {
		this.signalDate = signalDate;
	}
	public String getSignalCode() {
		return signalCode;
	}
	public void setSignalCode(String signalCode) {
		this.signalCode = signalCode;
	}
	public int getSignalCount() {
		return signalCount;
	}
	public void setSignalCount(int signalCount) {
		this.signalCount = signalCount;
	}
	public void incrementSignal() {
		this.signalCount++;
	}
	public String getSignalDesc() {
		return signalDesc;
	}
	public void setSignalDesc(String signalDesc) {
		this.signalDesc = signalDesc;
	}
	public String getSignalCountKey() {
		return signalCountKey;
	}
	public void setSignalCountKey(String signalCountKey) {
		this.signalCountKey = signalCountKey;
	}
}
