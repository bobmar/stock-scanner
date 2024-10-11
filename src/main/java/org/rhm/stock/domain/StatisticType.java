package org.rhm.stock.domain;

import org.springframework.data.annotation.Id;

public class StatisticType {
	@Id
	private String statisticCode;
	private String statisticDesc;
	private String className;
	private Boolean showInDashboard = false;
	public String getStatisticCode() {
		return statisticCode;
	}
	public void setStatisticCode(String statisticCode) {
		this.statisticCode = statisticCode;
	}
	public String getStatisticDesc() {
		return statisticDesc;
	}
	public void setStatisticDesc(String statisticDesc) {
		this.statisticDesc = statisticDesc;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Boolean getShowInDashboard() {
		return showInDashboard;
	}
	public void setShowInDashboard(Boolean showInDashboard) {
		this.showInDashboard = showInDashboard;
	}
}
