package org.rhm.stock.handler.maint;

import org.rhm.stock.service.SignalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Qualifier("pruneSignal")
public class PruneSignal implements MaintHandler {
	@Autowired
	private SignalService sigSvc = null;
	private Logger logger = LoggerFactory.getLogger(PruneSignal.class);
	@Override
	public void prune(Date deleteBefore) {
		logger.info("prune - delete prices before {}", deleteBefore.toString());
		long deleteCnt = sigSvc.deleteOlderThan(deleteBefore);
		logger.info("prune - delete {} signal records", deleteCnt);
	}
}
