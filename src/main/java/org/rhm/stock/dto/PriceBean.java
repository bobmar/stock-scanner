package org.rhm.stock.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PriceBean {
	private Date date;
	private Double openPrice;
	private Double highPrice;
	private Double lowPrice;
	private Double closePrice;
	private Long volume;
	private Double adjClose;
	private static final int DATE_INDX = 0;
	private static final int OPEN_INDX = 1;
	private static final int HIGH_INDX = 2;
	private static final int LOW_INDX = 3;
	private static final int CLOSE_INDX = 4;
	private static final int VOLUME_INDX = 5;
	private static final String DATE_TEXT = "Date";
	private final DateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd");
	private boolean dataLoaded = false;
	private static final Logger LOGGER = LoggerFactory.getLogger(PriceBean.class);
	public PriceBean(String priceCsv) {
		dataLoaded = false;
		this.parsePrice(priceCsv);
	}
	
	private void parsePrice(String priceData) {
		String[] price = priceData.split(",");
		if (!price[0].substring(1).equals(DATE_TEXT)) {
			try {
				openPrice = Double.valueOf(price[OPEN_INDX]);
				highPrice = Double.valueOf(price[HIGH_INDX]);
				lowPrice = Double.valueOf(price[LOW_INDX]);
				closePrice = Double.valueOf(price[CLOSE_INDX]);
				volume = Long.valueOf(price[VOLUME_INDX]);
				date = dtFmt.parse(price[DATE_INDX]);
				dataLoaded = true;
			}
			catch (ParseException | NumberFormatException e) {
				if (!priceData.startsWith("Date,Open,High,Low,Close")) {
					LOGGER.error(e.getMessage());
				}
			}
    }
		else {
			LOGGER.debug("parsePrice - skip header");
		}
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}

	public Double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}

	public Double getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}

	public Double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(Double adjClose) {
		this.adjClose = adjClose;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public boolean isDataLoaded() {
		return dataLoaded;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(date.toString());
		sb.append("|");
		sb.append(String.valueOf(this.openPrice));
		sb.append("|");
		sb.append(String.valueOf(this.highPrice));
		sb.append("|");
		sb.append(String.valueOf(this.lowPrice));
		sb.append("|");
		sb.append(String.valueOf(this.closePrice));
		sb.append("|");
		sb.append(String.valueOf(this.volume));
		return sb.toString();
	}
}
