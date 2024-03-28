package org.rhm.stock.repository;

import org.rhm.stock.domain.FinancialRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FinancialRatioRepo extends MongoRepository<FinancialRatio, String> {
  public List<FinancialRatio> findBySymbolOrderByDate(String symbol);
}
