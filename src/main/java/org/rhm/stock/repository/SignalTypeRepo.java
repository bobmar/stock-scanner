package org.rhm.stock.repository;

import org.rhm.stock.domain.SignalType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SignalTypeRepo extends MongoRepository<SignalType, String> {

}
