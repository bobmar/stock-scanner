package org.rhm.stock.handler.maint;

import java.util.Date;

import org.rhm.stock.service.AveragePriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("pruneAvgPrice")
public class PruneAvgPrice implements MaintHandler {
	@Autowired
	private AveragePriceService avgSvc = null;
	private Logger logger = LoggerFactory.getLogger(PruneAvgPrice.class);
	@Override
	public void prune(Date deleteBefore) {
		logger.info("prune - delete average prices before " + deleteBefore.toString());
		long deleteCnt = avgSvc.deleteOlderThan(deleteBefore);
		logger.info("prune - delete " + deleteCnt + " average price records");
	}
}
