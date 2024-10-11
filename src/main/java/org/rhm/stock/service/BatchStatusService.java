package org.rhm.stock.service;

import org.rhm.stock.batch.BatchStatus;
import org.rhm.stock.domain.BatchStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class BatchStatusService {
	@Autowired
	private BatchStatusRepo batchRepo;
	
	public BatchStatus saveBatchStatus(BatchStatus batchStatus) {
		return batchRepo.save(batchStatus);
	}
}
