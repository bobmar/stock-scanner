package org.rhm.stock.dto;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinanceProfile {
	private String address1 = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String country = null;
	private String phone = null;
	private String industry = null;
	private String sector = null;
	private String longBusinessSummary = null;
	private Integer fullTimeEmployees = null;
	private String longName = null;
	private String shortName = null;
	public FinanceProfile() {}
	private Logger logger = LoggerFactory.getLogger(FinanceProfile.class);
	public FinanceProfile(Map<String,Object> profile) {
		Map<String,Object> profileMap = this.findStatDetail(profile);
		if (profileMap != null) {
			logger.debug(profileMap.toString());
			this.address1 = (String)profileMap.get("address1");
			this.city = (String)profileMap.get("city");
			this.state = (String) profileMap.get("state");
			this.zip = (String) profileMap.get("zip");
			this.country = (String) profileMap.get("country");
			this.phone = (String) profileMap.get("phone");
			this.industry = (String) profileMap.get("industry");
			this.sector = (String) profileMap.get("sector");
			this.longBusinessSummary = (String) profileMap.get("longBusinessSummary");
			this.fullTimeEmployees = (Integer) profileMap.get("fullTimeEmployees");
			Map<String,Object> priceMap = this.findPriceDetail(profile);
			this.longName = (String)priceMap.get("longName");
			if (this.longName != null) {
				this.longName = this.stripAmp(this.longName);
			}
			this.shortName = (String) priceMap.get("shortName");
		}
	}

	private String stripAmp(String str) {
		StringBuilder sb = new StringBuilder(str);
		int pos = sb.indexOf("&amp;");
		if (pos >= 0) {
			sb.replace(pos, pos + 5, "&");
		}
		return sb.toString();
	}
	
	private Map<String,Object> findStatDetail(Map<String,Object> profileStat) {
		Map<String,Object> quoteSummary = (Map<String,Object> )profileStat.get("quoteSummary");
		List<Map<String,Object>> result = (List<Map<String,Object>>)quoteSummary.get("result");
		Map<String,Object> profileMap = result.get(0);
		Map<String,Object> assetProfile = (Map<String,Object>)profileMap.get("assetProfile");
		return assetProfile;
	}
	
	private Map<String,Object> findPriceDetail(Map<String,Object> profileStat) {
		Map<String,Object> quoteSummary = (Map<String,Object> )profileStat.get("quoteSummary");
		List<Map<String,Object>> result = (List<Map<String,Object>>)quoteSummary.get("result");
		Map<String,Object> profileMap = result.get(0);
		Map<String,Object> price = (Map<String,Object>)profileMap.get("price");
		return price;
	}
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getLongBusinessSummary() {
		return longBusinessSummary;
	}
	public void setLongBusinessSummary(String longBusinessSummary) {
		this.longBusinessSummary = longBusinessSummary;
	}
	public Integer getFullTimeEmployees() {
		return fullTimeEmployees;
	}
	public void setFullTimeEmployees(Integer fullTimeEmployees) {
		this.fullTimeEmployees = fullTimeEmployees;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
