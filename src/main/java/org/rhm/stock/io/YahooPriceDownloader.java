/**
 * 
 */
package org.rhm.stock.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.rhm.stock.dto.PriceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * http://ichart.finance.yahoo.com/table.csv?s=INTC&d=3&e=25&f=2013&g=d&a=6&b=9&c=1986&ignore=.csv
 * http://chart.finance.yahoo.com/table.csv?s=TGT&a=2&b=22&c=2017&d=3&e=22&f=2017&g=d&ignore=.csv
 * https://query1.finance.yahoo.com/v7/finance/download/TGT?period1=1492664246&period2=1495256246&interval=1d&events=history&crumb=VbcWYe.acmh
 * https://query1.finance.yahoo.com/v7/finance/download/TGT?period1=1502309369&period2=1504987769&interval=1d&events=history&crumb=tJPmx.z.yIn
 * http://www.google.com/finance/historical?q=WFC&output=csv
 * https://query1.finance.yahoo.com/v7/finance/download/WFC?period1=1502334450&period2=1505012850&interval=1d&events=history&crumb=tJPmx.z.yIn
 * cookie:AO=u=1; ywandp=1000911397279%3A1847613481; ywadp115488662=4169304830; HP=0; YP=v=AwAAY&d=AEgAMEUCIG2Lub0Gz0X.IuFKnkdB2eMcxbWqxdcsjPdU_eotVWfBAiEA9IHc2SLkkFFwQZl24b3CwKIebHlefAo1NS_rC7oqJVIA; ucs=fs=1&lnct=1457327776; GED_PLAYLIST_ACTIVITY=W3sidSI6IjhUdE8iLCJ0c2wiOjE0ODg1MTExNDEsIm52IjowLCJ1cHQiOjE0ODg1MDk1MTMsImx0IjoxNDg4NTA5NjQ4fV0.; yvapF=%7B%22vl%22%3A55646.45897001347%2C%22rvl%22%3A134.34009599999996%2C%22cc%22%3A327%2C%22rcc%22%3A3%2C%22ac%22%3A229%2C%22al%22%3A1391.8863285215448%7D; B=6b5igrdaho2o2&b=4&d=_09VHXZpYEJmYiUgG46_2HOCqgEawdWqgjrGyg--&s=j5&i=F5H4S1XlplLIhHPo3DwT; PRF=%3Dundefined%26t%3DUSB%252BWFC%252BTGT%252BSHO%252BNEE%252BFPRX%252BHDS%252BBAK%252BNAK%252BESNT%252BACTPUX%252BREN%252BCIR%252BPAYC%252BDRII
 * @author bob
 *
 */
@Component
public class YahooPriceDownloader {
	private final static String BASE_URL = "https://query1.finance.yahoo.com/v7/finance/download/";
	private final static String PER1_PARAM = "?period1=";
	private final static String PER2_PARAM = "&period2=";
	private final static String YAHOO_SUFFIX = "&interval=1d&events=history&crumb=tJPmx.z.yIn";
	private final static String YAHOO_QUOTE_PAGE = "https://finance.yahoo.com/quote/ticker_symbol/history?p=ticker_symbol";
	private final static String COOKIE = "B=6b5igrdaho2o2&b=4&d=_09VHXZpYEJmYiUgG46_2HOCqgEawdWqgjrGyg--&s=j5&i=F5H4S1XlplLIhHPo3DwT";
	
	private String tickerSymbol = null;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YahooPriceDownloader.class);
	
	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public void retrQuotePage() {
		String quotePageUrl = YAHOO_QUOTE_PAGE.replaceAll("ticker_symbol", this.tickerSymbol);
		LOGGER.debug("Quote Page URL: " + quotePageUrl);
		try {
			URL url = new URL(quotePageUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			Map<String,List<String>> headerFields = conn.getHeaderFields();
			System.out.println(headerFields.toString());
			InputStream is = conn.getInputStream();
			byte[] chunk = new byte[8192];
			int bytesRead = -1;
			while ((bytesRead = is.read(chunk, 0, chunk.length)) > -1) {
				System.out.println(new String(chunk));
			}
		} 
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<PriceBean> retrievePriceData(String tickerSymbol, Integer histDays) {
		Calendar toCal = Calendar.getInstance();
		Calendar fromCal = Calendar.getInstance();
		fromCal.add(Calendar.DAY_OF_MONTH, histDays * -1);
		List<PriceBean> priceDataList = new ArrayList<PriceBean>();
		String urlStr = formatUrl(tickerSymbol, fromCal, toCal);
		PriceBean priceData = null;
		LOGGER.debug("URL string: " + urlStr);
		try {
			URI uri = URI.create(urlStr);
			LOGGER.debug("Query:" + uri.getQuery());
			HttpRequest request = HttpRequest.newBuilder()
			.uri(uri)
			.setHeader("Cookie", COOKIE)
			.GET()
			.build()
			;
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
			InputStream is = response.body();
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(reader);
			String s = null;
			while ((s = in.readLine()) != null) {
				priceData = new PriceBean(s);
				if (priceData.isDataLoaded()) {
					priceDataList.add(priceData);
				}
				else {
					if (!s.startsWith("Date,Open,High,Low,Close")) {
						LOGGER.warn("downloadPrices - [" + tickerSymbol + "] unable to parse: " + s);
					}
				}
			}
			in.close();
            is.close();
		} catch (InterruptedException | IOException e) {
			throw new RuntimeException(e);
		}
		return priceDataList;

	}

	private String millisecondStr(Calendar cal) {
		String str = String.valueOf(cal.getTimeInMillis());
		return str.substring(0, str.length() - 3);
	}

	private String formatUrl(String tickerSymbol, Calendar fromCal, Calendar toCal) {
		StringBuilder sb = new StringBuilder(BASE_URL);
		sb.append(tickerSymbol);
		sb.append(PER1_PARAM);
		sb.append(this.millisecondStr(fromCal));
		sb.append(PER2_PARAM);
		sb.append(this.millisecondStr(toCal));
		sb.append(YAHOO_SUFFIX);
		return sb.toString();
	}
	
}
