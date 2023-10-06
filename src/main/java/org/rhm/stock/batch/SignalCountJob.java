package org.rhm.stock.batch;

import java.time.LocalDateTime;

import org.rhm.stock.handler.CountSignals;
import org.rhm.stock.service.BatchStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Component
@Qualifier("signalCount")
public class SignalCountJob implements BatchJob {
	@Autowired
	private CountSignals countSignals = null;
	@Autowired
	private BatchStatusService batchStatSvc = null;

	@Override
	public BatchStatus run() {
		BatchStatus status = new BatchStatus(CountSignals.class);
		countSignals.run();
		status.setSuccess(true);
		status.setCompletionMsg("Successfully summarized signal counts");
		status.setFinishDate(LocalDateTime.now());
		batchStatSvc.saveBatchStatus(status);
		return status;
	}

}
