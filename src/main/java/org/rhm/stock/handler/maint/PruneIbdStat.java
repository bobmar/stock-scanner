package org.rhm.stock.handler.maint;

import java.util.Date;

import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
@Qualifier("pruneIbdStat")
public class PruneIbdStat implements MaintHandler {
	@Autowired
	private TickerService tickerSvc = null;
	private Logger logger = LoggerFactory.getLogger(PruneIbdStat.class);
	@Override
	public void prune(Date deleteBefore) {
		logger.info("prune - delete prices before " + deleteBefore.toString());
		long deleteCnt = tickerSvc.deleteIbdStatsOlderThan(deleteBefore);
		logger.info("prune - delete " + deleteCnt + " price records");
	}

}
