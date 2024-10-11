package org.rhm.stock.batch;

import org.rhm.stock.handler.aggregate.AggregatorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
@Deprecated
@Component
@Qualifier("aggregatorJob")
public class AggregatorJob implements BatchJob {
	@Autowired
	@Qualifier("yahooStatAggr")
	private AggregatorHandler yahooAggr = null;
	
	@Override
	public BatchStatus run() {
		BatchStatus status = new BatchStatus(AggregatorJob.class);
		yahooAggr.process();
		return status;
	}

}
