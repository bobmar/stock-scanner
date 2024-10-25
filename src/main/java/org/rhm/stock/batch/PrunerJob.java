package org.rhm.stock.batch;

import org.rhm.stock.handler.maint.*;
import org.rhm.stock.service.BatchStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Qualifier("pruner")
public class PrunerJob implements BatchJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrunerJob.class);
	@Autowired
	@Qualifier("pruneAvgPrice")
	private MaintHandler avgPrice;
	@Autowired
	@Qualifier("prunePrice")
	private MaintHandler price;
	@Autowired
	@Qualifier("pruneSignal")
	private MaintHandler signal;
	@Autowired
	@Qualifier("pruneStat")
	private MaintHandler stat;
	@Autowired
	@Qualifier("pruneIbdStat")
	private MaintHandler ibdStat;
	@Autowired
	@Qualifier("pruneOrphan")
	private MaintHandler orphan;
	@Autowired
	private BatchStatusService batchStatSvc;
	private List<MaintHandler> prunerList = null;

	@Autowired
	private ApplicationContext ctx;

	private List<MaintHandler> createPrunerList() {
		List<MaintHandler> prunerList = new ArrayList<MaintHandler>();
		prunerList.add(ctx.getBean(PruneAvgPrice.class));
		prunerList.add(ctx.getBean(PrunePrice.class));
		prunerList.add(ctx.getBean(PruneSignal.class));
		prunerList.add(ctx.getBean(PruneStatistic.class));
		prunerList.add(ctx.getBean(PruneIbdStat.class));
		prunerList.add(ctx.getBean(PruneOrphan.class));
		return prunerList;
	}

	private Date calcDeleteBefore(int days) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, days * -1);
		return cal.getTime();
	}
	
	@Override
	public BatchStatus run() {
		BatchStatus status = new BatchStatus(PrunerJob.class);
		this.prunerList = this.createPrunerList();
		Date deleteBefore = calcDeleteBefore(365);
		for (MaintHandler pruner: prunerList) {
			LOGGER.info("run - delete records older than " + deleteBefore.toString());
			pruner.prune(deleteBefore);
		}
		status.setCompletionMsg("Successfully pruned collections");
		status.setFinishDate(LocalDateTime.now());
		status.setSuccess(true);
		batchStatSvc.saveBatchStatus(status);
		return status;
	}

}
