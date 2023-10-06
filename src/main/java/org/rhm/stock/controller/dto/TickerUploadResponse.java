package org.rhm.stock.controller.dto;

import java.util.List;

public class TickerUploadResponse extends GeneralResponse{
	private String fileName = null;
	private List<TickerInfo> keep = null;
	private List<TickerInfo> discard = null;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<TickerInfo> getKeep() {
		return keep;
	}
	public void setKeep(List<TickerInfo> keep) {
		this.keep = keep;
	}
	public List<TickerInfo> getDiscard() {
		return discard;
	}
	public void setDiscard(List<TickerInfo> discard) {
		this.discard = discard;
	}

}
