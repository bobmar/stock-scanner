package org.rhm.stock.repository;

import java.util.List;

import org.rhm.stock.domain.StatisticType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticTypeRepo extends MongoRepository<StatisticType, String> {
	public List<StatisticType> findByShowInDashboard(Boolean show);
}
