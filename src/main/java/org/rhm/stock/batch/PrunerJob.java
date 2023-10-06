package org.rhm.stock.batch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.rhm.stock.handler.maint.MaintHandler;
import org.rhm.stock.service.BatchStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("pruner")
public class PrunerJob implements BatchJob {
	@Autowired
	@Qualifier("pruneAvgPrice")
	private MaintHandler avgPrice = null;
	@Autowired
	@Qualifier("prunePrice")
	private MaintHandler price = null;
	@Autowired
	@Qualifier("pruneSignal")
	private MaintHandler signal = null;
	@Autowired
	@Qualifier("pruneStat")
	private MaintHandler stat = null;
	@Autowired
	@Qualifier("pruneIbdStat")
	private MaintHandler ibdStat = null;
	@Autowired
	@Qualifier("pruneKeyStat")
	private MaintHandler keyStat = null;
	@Autowired
	@Qualifier("pruneOrphan")
	private MaintHandler orphan = null;
	@Autowired
	private BatchStatusService batchStatSvc = null;
	private Logger logger = LoggerFactory.getLogger(PrunerJob.class);
	private List<MaintHandler> prunerList = null;
	
	private List<MaintHandler> createPrunerList() {
		List<MaintHandler> prunerList = new ArrayList<MaintHandler>();
		prunerList.add(avgPrice);
		prunerList.add(price);
		prunerList.add(signal);
		prunerList.add(stat);
		prunerList.add(ibdStat);
		prunerList.add(keyStat);
		prunerList.add(orphan);
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
			logger.info("run - delete records older than " + deleteBefore.toString());
			pruner.prune(deleteBefore);
		}
		status.setCompletionMsg("Successfully pruned collections");
		status.setFinishDate(LocalDateTime.now());
		status.setSuccess(true);
		batchStatSvc.saveBatchStatus(status);
		return status;
	}

}
