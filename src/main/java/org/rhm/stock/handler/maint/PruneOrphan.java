package org.rhm.stock.handler.maint;

import org.rhm.stock.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Qualifier("pruneOrphan")
public class PruneOrphan implements MaintHandler {
	@Autowired
	private PriceService priceSvc;
	@Autowired
	private TickerService tickerSvc;
	@Autowired
	private AveragePriceService avgPriceSvc;
	@Autowired
	private SignalService signalSvc;
	@Autowired
	private StatisticService statSvc;
	private final static Logger LOGGER = LoggerFactory.getLogger(PruneOrphan.class);

	private void removeOrphans(String tickerSymbol) {
		long delPrices = priceSvc.deleteByTickerSymbol(tickerSymbol);
		LOGGER.info("removeOrphans - deleted {} price records for ticker {}", delPrices, tickerSymbol);
		long avgPrices = avgPriceSvc.deleteByTickerSymbol(tickerSymbol);
		LOGGER.info("removeOrphans - deleted {} average price records for ticker {}", avgPrices, tickerSymbol);
		long signals = signalSvc.deleteByTickerSymbol(tickerSymbol);
		LOGGER.info("removeOrphans - deleted {} signal records for ticker {}", signals, tickerSymbol);
		long stats = statSvc.deleteByTickerSymbol(tickerSymbol);
		LOGGER.info("removeOrphans - deleted {} statistic records for ticker {}", stats, tickerSymbol);
		long ibdStats = tickerSvc.deleteIbdStatsByTicker(tickerSymbol);
		LOGGER.info("removeOrphans - deleted {} IBD stat records for ticker {}", ibdStats, tickerSymbol);
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
		LOGGER.info("prune - found {} price tickers", tickerSymbolList.size());
		this.processTickers(tickerSymbolList);
		tickerSymbolList = avgPriceSvc.findAvgPriceTickers(deleteBefore);
		LOGGER.info("prune - found {} average price tickers", tickerSymbolList.size());
		this.processTickers(tickerSymbolList);
		tickerSymbolList = signalSvc.findSignalTickers(deleteBefore);
		LOGGER.info("prune - found {} signal tickers", tickerSymbolList.size());
		this.processTickers(tickerSymbolList);
		tickerSymbolList = statSvc.findStatTickers(deleteBefore);
		LOGGER.info("prune - found {} statistic tickers", tickerSymbolList.size());
		this.processTickers(tickerSymbolList);
	}
}
