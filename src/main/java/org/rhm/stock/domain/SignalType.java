package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

public class SignalType {
	@Id
	private String signalCode;
	private String signalDesc;
	
	public String getSignalCode() {
		return signalCode;
	}
	public void setSignalCode(String signalCode) {
		this.signalCode = signalCode;
	}
	public String getSignalDesc() {
		return signalDesc;
	}
	public void setSignalDesc(String signalDesc) {
		this.signalDesc = signalDesc;
	}
}
