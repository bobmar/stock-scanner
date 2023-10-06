package org.rhm.stock.handler.maint;

import java.util.Date;
import java.util.List;

import org.rhm.stock.service.AveragePriceService;
import org.rhm.stock.service.KeyStatService;
import org.rhm.stock.service.PriceService;
import org.rhm.stock.service.SignalService;
import org.rhm.stock.service.StatisticService;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("pruneOrphan")
public class PruneOrphan implements MaintHandler {
	@Autowired
	private PriceService priceSvc = null;
	@Autowired
	private TickerService tickerSvc = null;
	@Autowired
	private AveragePriceService avgPriceSvc = null;
	@Autowired
	private SignalService signalSvc = null;
	@Autowired
	private StatisticService statSvc = null;
	@Autowired
	private KeyStatService keyStatSvc = null;
	private Logger logger = LoggerFactory.getLogger(PruneOrphan.class);

	private void removeOrphans(String tickerSymbol) {
		long delPrices = priceSvc.deleteByTickerSymbol(tickerSymbol);
		logger.info("removeOrphans - deleted " + delPrices + " price records for ticker " + tickerSymbol);
		long avgPrices = avgPriceSvc.deleteByTickerSymbol(tickerSymbol);
		logger.info("removeOrphans - deleted " + avgPrices + " average price records for ticker " + tickerSymbol);
		long signals = signalSvc.deleteByTickerSymbol(tickerSymbol);
		logger.info("removeOrphans - deleted " + signals + " signal records for ticker " + tickerSymbol);
		long stats = statSvc.deleteByTickerSymbol(tickerSymbol);
		logger.info("removeOrphans - deleted " + stats + " statistic records for ticker " + tickerSymbol);
		long ibdStats = tickerSvc.deleteIbdStatsByTicker(tickerSymbol);
		logger.info("removeOrphans - deleted " + ibdStats + " IBD stat records for ticker " + tickerSymbol);
		long keyStats = keyStatSvc.deleteByTickerSymbol(tickerSymbol);
		logger.info("removeOrphans - deleted " + keyStats + " Yahoo key stat records for ticker " + tickerSymbol);
	}
	
	private void processTickers(List<String> tickerSymbolList) {
		for (String tickerSymbol: tickerSymbolList) {
			if (!tickerSvc.tickerExists(tickerSymbol)) {
				this.removeOrphans(tickerSymbol);
			}
		}
	}
	
	@Override
	public void prune(Date deleteBefore) {
		List<String> tickerSymbolList = priceSvc.findPriceTickers(deleteBefore);
		logger.info("prune - found " + tickerSymbolList.size() + " price tickers");
		this.processTickers(tickerSymbolList);
		tickerSymbolList = avgPriceSvc.findAvgPriceTickers(deleteBefore);
		logger.info("prune - found " + tickerSymbolList.size() + " average price tickers");
		this.processTickers(tickerSymbolList);
		tickerSymbolList = signalSvc.findSignalTickers(deleteBefore);
		logger.info("prune - found " + tickerSymbolList.size() + " signal tickers");
		this.processTickers(tickerSymbolList);
		tickerSymbolList = statSvc.findStatTickers(deleteBefore);
		logger.info("prune - found " + tickerSymbolList.size() + " statistic tickers");
		this.processTickers(tickerSymbolList);
	}

}
