package org.rhm.stock.handler.maint;

import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Qualifier("pruneStat")
public class PruneStatistic implements MaintHandler {
	@Autowired
	private StatisticService statSvc;
	private final static Logger LOGGER = LoggerFactory.getLogger(PruneStatistic.class);
	@Override
	public void prune(Date deleteBefore) {
		LOGGER.info("prune - delete prices before {}", deleteBefore.toString());
		long deleteCnt = statSvc.deleteOlderThan(deleteBefore);
		LOGGER.info("prune - delete {} statistic records", deleteCnt);
	}
}
