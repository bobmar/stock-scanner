package org.rhm.stock.handler.maint;

import org.rhm.stock.service.KeyStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Deprecated
@Component
@Qualifier("pruneKeyStat")
public class PruneKeyStat implements MaintHandler {
	private Logger logger = LoggerFactory.getLogger(PruneKeyStat.class);
	@Autowired
	private KeyStatService keyStatSvc = null;
	@Override
	public void prune(Date deleteBefore) {
		logger.info("prune - delete records older than " + deleteBefore.toString());
		int count = keyStatSvc.deleteOlderThan(deleteBefore);
		logger.info("prune - deleted " + count + " key stat records");
	}

}
