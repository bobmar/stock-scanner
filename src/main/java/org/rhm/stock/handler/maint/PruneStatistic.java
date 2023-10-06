package org.rhm.stock.handler.maint;

import java.util.Date;

import org.rhm.stock.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("pruneStat")
public class PruneStatistic implements MaintHandler {
	@Autowired
	private StatisticService statSvc = null;
	private Logger logger = LoggerFactory.getLogger(PruneStatistic.class);
	@Override
	public void prune(Date deleteBefore) {
		logger.info("prune - delete prices before " + deleteBefore.toString());
		long deleteCnt = statSvc.deleteOlderThan(deleteBefore);
		logger.info("prune - delete " + deleteCnt + " statistic records");
	}

}
