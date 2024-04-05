package org.rhm.stock.repository;

import org.rhm.stock.domain.FinancialGrowth;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FinancialGrowthRepo extends MongoRepository<FinancialGrowth, String> {
}
