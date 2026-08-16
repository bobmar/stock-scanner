package org.rhm.stock.repository;

import org.rhm.stock.domain.FinancialScore;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FinancialScoreRepo extends MongoRepository<FinancialScore, String> {
}
