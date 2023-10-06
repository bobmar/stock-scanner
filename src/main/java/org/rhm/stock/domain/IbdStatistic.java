package org.rhm.stock.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class IbdStatistic {

	@Id
	private String statId = null;
	private String tickerSymbol = null;
	private String companyName = null;
	private Date priceDate = null;
	private String compositeRating = null;
	private String epsRating = null;
	private String relativeStrength = null;
	private String groupStrength = null;
	private String accumDist = null;
	private String salesMarginRoe = null;
	private String industryRank = null;
	private Double mgmtOwnPct = null;
	private Date createDate = Calendar.getInstance().getTime();
	private List<String> listName = null;
	
	public String getStatId() {
		return statId;
	}
	public void setStatId(String statId) {
		this.statId = statId;
	}
	public String getTickerSymbol() {
		return tickerSymbol;
	}
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}
	public Date getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}
	public String getCompositeRating() {
		return compositeRating;
	}
	public void setCompositeRating(String compositeRating) {
		this.compositeRating = compositeRating;
	}
	public String getEpsRating() {
		return epsRating;
	}
	public void setEpsRating(String epsRating) {
		this.epsRating = epsRating;
	}
	public String getRelativeStrength() {
		return relativeStrength;
	}
	public void setRelativeStrength(String relativeStrength) {
		this.relativeStrength = relativeStrength;
	}
	public String getGroupStrength() {
		return groupStrength;
	}
	public void setGroupStrength(String groupStrength) {
		this.groupStrength = groupStrength;
	}
	public String getAccumDist() {
		return accumDist;
	}
	public void setAccumDist(String accumDist) {
		this.accumDist = accumDist;
	}
	public String getSalesMarginRoe() {
		return salesMarginRoe;
	}
	public void setSalesMarginRoe(String salesMarginRoe) {
		this.salesMarginRoe = salesMarginRoe;
	}
	public String getIndustryRank() {
		return industryRank;
	}
	public void setIndustryRank(String industryRank) {
		this.industryRank = industryRank;
	}
	
	public String toString() {
		return this.tickerSymbol
				+ "|" + this.companyName
				+ "|C-" + this.compositeRating
				+ "|E-" + this.epsRating
				+ "|R-" + this.relativeStrength
				+ "|G-" + this.groupStrength
				+ "|AD-" + this.getAccumDist()
				+ "|S-" + this.getSalesMarginRoe();
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public List<String> getListName() {
		if (listName == null) {
			listName = new ArrayList<String>();
		}
		return listName;
	}
	public void setListName(List<String> listName) {
		this.listName = listName;
	}
	public Double getMgmtOwnPct() {
		return mgmtOwnPct;
	}
	public void setMgmtOwnPct(Double mgmtOwnPct) {
		this.mgmtOwnPct = mgmtOwnPct;
	}
	
}
