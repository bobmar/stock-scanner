package org.rhm.stock.repository;

import org.rhm.stock.domain.StockTicker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TickerRepo extends MongoRepository<StockTicker, String>, PagingAndSortingRepository<StockTicker, String> {
}
