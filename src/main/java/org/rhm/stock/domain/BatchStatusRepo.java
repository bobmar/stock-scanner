package org.rhm.stock.domain;

import org.rhm.stock.batch.BatchStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatchStatusRepo extends MongoRepository<BatchStatus, String> {

}
