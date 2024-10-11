package org.rhm.stock.handler.maint;

import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@Qualifier("pruneIbdStat")
public class PruneIbdStat implements MaintHandler {
	@Autowired
	private TickerService tickerSvc;
	private static final Logger LOGGER = LoggerFactory.getLogger(PruneIbdStat.class);
	@Override
	public void prune(Date deleteBefore) {
		LOGGER.info("prune - delete prices before {}", deleteBefore.toString());
		long deleteCnt = tickerSvc.deleteIbdStatsOlderThan(deleteBefore);
		LOGGER.info("prune - delete {} price records", deleteCnt);
	}

}
