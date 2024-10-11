package org.rhm.stock.handler.maint;

import org.rhm.stock.service.AveragePriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Qualifier("pruneAvgPrice")
public class PruneAvgPrice implements MaintHandler {
	@Autowired
	private AveragePriceService avgSvc;
	private static final Logger LOGGER = LoggerFactory.getLogger(PruneAvgPrice.class);
	@Override
	public void prune(Date deleteBefore) {
		LOGGER.info("prune - delete average prices before {}", deleteBefore.toString());
		long deleteCnt = avgSvc.deleteOlderThan(deleteBefore);
		LOGGER.info("prune - delete {} average price records", deleteCnt);
	}
}
