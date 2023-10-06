package org.rhm.stock.handler.maint;

import java.util.Date;

import org.rhm.stock.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("prunePrice")
public class PrunePrice implements MaintHandler {
	@Autowired
	private PriceService priceSvc = null;
	private Logger logger = LoggerFactory.getLogger(PruneAvgPrice.class);
	@Override
	public void prune(Date deleteBefore) {
		logger.info("prune - delete prices before " + deleteBefore.toString());
		long deleteCnt = priceSvc.deleteOlderThan(deleteBefore);
		logger.info("prune - delete " + deleteCnt + " price records");
	}

}
