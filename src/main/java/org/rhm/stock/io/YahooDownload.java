package org.rhm.stock.io;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Deprecated
@Component
public class YahooDownload {
	private String protocol = "https://";
	private String domain = "query2.finance.yahoo.com";
	private String baseUri = "/v10/finance";
	private String profilePrefix = "/quoteSummary/";
	private String profileSuffix = "?formatted=true&lang=en-US&region=US&modules=assetProfile%2Cprice&corsDomain=finance.yahoo.com";
	private String keyStatPrefix = "/quoteSummary/";
	private String keyStatSuffix = "?formatted=true&crumb=3nWgOD%2FN%2FCO&lang=en-US&region=US&modules=defaultKeyStatistics%2CfinancialData%2CcalendarEvents&corsDomain=finance.yahoo.com";
	private final static String COOKIE1 = "B=2lico8pd96qcb&b=4&d=_09VHXZpYEJmYiUgG46_2HOCqgEawdWqgjrGyg--&s=t9&i=WKz4Av5hXR7g5v01_V0C";
	private final static String COOKIE2 = "T=S501bBSNc6bBHrupEDVexpTNDU0MQYxNE42NTcyTzQ-&a=QAE&sk=DAAXydChIIgmtG&ks=EAAx_l5dFx_A7R3oh1cA0QYmw--~G&kt=EAAK5LcgfkdrQu7VhGYbsH1AQ--~I&ku=FAAcVTpOTiUpGHXRSoe_81ELFrXFRFHwRcEoMupfuhxmzHFamGa5iHVLabRwFuF1UlWNWuI9J5WuPJ0PQFTg8KFYiSs9bJJvJCrfiGLnwlpmzYSnJ35noby5RWNkFFZTcz0cmGDDEKv2.yFYSbw.tQROEa9RNroh09oKNhsTvLZTh4-~A&d=bnMBeWFob28BZwEyUjVNU1hOU1A1RVFGRFVDNE1OWlVLV0ZGQQFzbAFNekl6TmdFMk16a3hNakExT0RNLQFhAVFBRQFhYwFBQm5sajMxXwFjcwEBc2MBZGVza3RvcF93ZWIBZnMBQTJZWmdEVmIxMDVTAXp6AVM1MDFiQkE3RQ--&af=JnRzPTE1NDA4MzY5NDYmcHM9YnZqekpxSjlDWUUzaWxqcGtTeHdLUS0t";
	private final static String COOKIE = COOKIE1 + ";" + COOKIE2;
	//2lico8pd96qcb&b=4&d=_09VHXZpYEJmYiUgG46_2HOCqgEawdWqgjrGyg--&s=t9&i=WKz4Av5hXR7g5v01_V0C
	//https://query1.finance.yahoo.com/v10/finance/quoteSummary/MSFT?formatted=true&crumb=0kWuoWjxgGH&lang=en-US&region=US&modules=assetProfile,secFilings&corsDomain=finance.yahoo.com
	private Logger logger = LoggerFactory.getLogger(YahooDownload.class);
	//https://query2.finance.yahoo.com/v10/finance/quoteSummary/WFC?formatted=true&crumb=%2FsTY4E5V.U%2F&lang=en-US&region=US&modules=defaultKeyStatistics%2CfinancialData%2CcalendarEvents&corsDomain=finance.yahoo.com

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	public String getProfileSuffix() {
		return profileSuffix;
	}
	public void setProfileSuffix(String profileSuffix) {
		this.profileSuffix = profileSuffix;
	}
	public String getKeyStatPrefix() {
		return keyStatPrefix;
	}
	public void setKeyStatPrefix(String keyStatPrefix) {
		this.keyStatPrefix = keyStatPrefix;
	}
	public String getKeyStatSuffix() {
		return keyStatSuffix;
	}
	public void setKeyStatSuffix(String keyStatSuffix) {
		this.keyStatSuffix = keyStatSuffix;
	}
	public String getProfilePrefix() {
		return profilePrefix;
	}
	public void setProfilePrefix(String profilePrefix) {
		this.profilePrefix = profilePrefix;
	}
	public StringBuilder baseUrl() {
		StringBuilder domainBuilder = new StringBuilder(domain);
		if (domainBuilder.toString().startsWith("query1")) {
			domainBuilder.replace(0, 6, "query2");
		}
		else {
			domainBuilder.replace(0, 6, "query1");
		}
		domain = domainBuilder.toString();
		StringBuilder sb = new StringBuilder();
		sb.append(protocol);
		sb.append(domain);
		sb.append(baseUri);
		return sb;
	}
	
	public String profileUrl(String tickerSymbol) {
		StringBuilder sb = this.baseUrl();
		sb.append(profilePrefix.trim());
		sb.append(tickerSymbol);
		sb.append(profileSuffix.trim());
		return sb.toString();
	}
	
	public String keyStatUrl(String tickerSymbol) {
		StringBuilder sb = this.baseUrl();
		sb.append(keyStatPrefix.trim());
		sb.append(tickerSymbol);
		sb.append(keyStatSuffix.trim());
		return sb.toString();
	}
	
	public String optionsUrl(String tickerSymbol) {
		StringBuilder sb = new StringBuilder();
		sb.append(protocol);
		sb.append(domain);
		sb.append("/v7/finance/options/");
		sb.append(tickerSymbol);
		sb.append("?formatted=true&lang=en-US&region=US&corsDomain=finance.yahoo.com");
		return sb.toString();
	}
	
	public HttpURLConnection createConnection(String urlStr) {
		URL url = null;
		HttpURLConnection conn = null;
		logger.debug("URL:" + urlStr);
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public String retrieve(HttpURLConnection urlConn) {
		InputStream is = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] chunk = new byte[1024*256];
		try {
			urlConn.addRequestProperty("Cookie", COOKIE1);
			urlConn.addRequestProperty("Cookie", COOKIE2);
			is = urlConn.getInputStream();
			int bytesRead = -1;
			while ((bytesRead = is.read(chunk)) != -1) {
				os.write(chunk,0, bytesRead);
			}
			os.flush();
			is.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(os.toByteArray());
	}
	
	public InputStream retrieveStream(HttpURLConnection urlConn) {
		InputStream is = null;
		try {
			is = urlConn.getInputStream();
		} 
		catch (IOException e) {
			logger.error(e.getMessage());
		}
		return is;
	}
	
	private Map<String,Object> jsonToMap(String jsonStr) throws JsonParseException {
		logger.debug("jsonToMap - \n" + jsonStr);
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map = null;
		try {
			map = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>(){});
		} catch (JsonParseException e) {
			throw e;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	private String streamToJsonStr(InputStream is) {
		byte[] chunk = new byte[512];
		int bytesRead = 0;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			while ((bytesRead = is.read(chunk)) > -1) {
				os.write(chunk, 0, bytesRead);
			}
			os.flush();
			os.close();
			is.close();
			logger.debug(os.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toString();
	}
	
	public Map<String,Object> retrieveProfile(String tickerSymbol) {
		Map<String,Object> profile = null;
		String urlStr = this.profileUrl(tickerSymbol);
		InputStream is = this.retrieveStream(this.createConnection(urlStr));
		logger.info("retrieveProfile - URL: " + urlStr);
		if (is != null) {
			try {
				profile = this.jsonToMap(this.streamToJsonStr(is));
			}
			catch (JsonParseException e) {
				logger.error("retrieveProfile - " + e.getMessage());
				profile = null;
			}
		}
		return profile;
	}
	
	public Map<String,Object> retrieveKeyStat(String tickerSymbol) {
		Map<String,Object> keyStat = null;
		String urlStr = this.keyStatUrl(tickerSymbol);
		InputStream is = this.retrieveStream(this.createConnection(urlStr));
		if (is != null) {
			try {
				keyStat = jsonToMap(this.streamToJsonStr(is));
			}
			catch (JsonParseException e) {
				logger.error("retrieveKeyStat - " + e.getMessage());
			}
		}
		return keyStat;
	}
	
	public Map<String,Object> retrieveOptionChain(String tickerSymbol, String expirationDate) {
		Map<String,Object> optionChain = null;
		String urlStr = this.optionsUrl(tickerSymbol);
		if (expirationDate != null) {
			urlStr = urlStr + "&date=" + expirationDate;
		}
		InputStream is = this.retrieveStream(this.createConnection(urlStr));
		if (is != null) {
			try {
				optionChain = jsonToMap(this.streamToJsonStr(is));
			}
			catch (JsonParseException e) {
				logger.error("retrieveOptionChain - " + e.getMessage());
			}
		}
		return optionChain;
	}
}
