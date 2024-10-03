package org.rhm.stock.handler.signal;

import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.StockPrice;
import org.rhm.stock.domain.StockSignal;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Qualifier("ibdScanner")
public class IBDScanner implements SignalScanner {
	// IBD Composite score increased
	private static final String SIGNAL_COMPOSITE_INC = "IBDCINC";
	// IBD Relative Strength rating increased
	private static final String SIGNAL_RS_INC = "IBDRSINC";
	// IBD Relative Strength crossed above 90
	private static final String SIGNAL_RS_X90 = "IBDRSX90";
	// IBD Accum/Dist is A or B
	private static final String SIGNAL_AD_AB = "IBDADAB";
	// Stock on 3 or more IBD lists
	private static final String SIGNAL_3PLUS = "IBD3PLUS";
	// IBD Management Ownership pct 5 or higher
	private static final String SIGNAL_MGMT_GE5 = "IBDMGMT5";

	@Autowired
	private TickerService tickerSvc;
	@Autowired
	private PriceService priceSvc;
	@Autowired
	private SignalService signalSvc;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger LOGGER = LoggerFactory.getLogger(IBDScanner.class);
	
	private void createSignal(IbdStatistic stat, String signal) {
		StockPrice price = priceSvc.findStockPrice(stat.getTickerSymbol() + ":" + df.format(stat.getPriceDate()));
		signalSvc.createSignal(new StockSignal(price, signal));
		LOGGER.info("createSignal - {},{}", price.getPriceId(), signal);
	}
	
	private void detectSignals(List<IbdStatistic> ibdStatList) {
		IbdStatistic currStat = ibdStatList.get(0), prevStat = ibdStatList.get(1);
		try {
			if (Integer.valueOf(currStat.getCompositeRating()).compareTo(Integer.valueOf(prevStat.getCompositeRating())) > 0) {
				this.createSignal(currStat, SIGNAL_COMPOSITE_INC);
			}
		}
		catch (NumberFormatException e) {
			LOGGER.error("detectSignals - {}:{}", SIGNAL_COMPOSITE_INC, e.getMessage());
		}
		try {
			if (Integer.valueOf(currStat.getRelativeStrength()).compareTo(Integer.valueOf(prevStat.getRelativeStrength())) > 0) {
				this.createSignal(currStat, SIGNAL_RS_INC);
			}
		}
		catch (NumberFormatException e) {
			LOGGER.error("detectSignals - {}:{}", SIGNAL_RS_INC, e.getMessage());
		}
		try {
			if (Integer.parseInt(currStat.getRelativeStrength()) >= 90) {
				if (Integer.parseInt(prevStat.getRelativeStrength()) < 90) {
					this.createSignal(currStat, SIGNAL_RS_X90);
				}
			}
		}
		catch (NumberFormatException e) {
			LOGGER.error("detectSignals - {}:{}", SIGNAL_RS_X90, e.getMessage());
		}
	}
	
	private void detectCurrSignal(IbdStatistic currStat) {
		String accumDist = currStat.getAccumDist()==null?"":currStat.getAccumDist();
		if (accumDist.startsWith("A") || accumDist.startsWith("B")) {
			this.createSignal(currStat, SIGNAL_AD_AB);
		}
		if (currStat.getListName().size() >= 3) {
			this.createSignal(currStat, SIGNAL_3PLUS);
		}
		if (currStat.getMgmtOwnPct().intValue() >= 5) {
			this.createSignal(currStat, SIGNAL_MGMT_GE5);
		}
	}
	
	@Override
	public void scan(String tickerSymbol) {
		List<IbdStatistic> ibdStatList = tickerSvc.findIbdStats(tickerSymbol);
		LOGGER.info("scan - found {} IBD stats for {}", ibdStatList.size(), tickerSymbol);
		while (ibdStatList.size() >= 2) {
			this.detectCurrSignal(ibdStatList.get(0));
			this.detectSignals(ibdStatList.subList(0, 2));
			ibdStatList.remove(0);
		}
	}
}
