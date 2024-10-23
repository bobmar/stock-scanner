package org.rhm.stock.repository;

import com.mongodb.client.result.DeleteResult;
import org.rhm.stock.domain.StockAveragePrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

public class AveragePriceCustomRepoImpl implements AveragePriceCustomRepo {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * @deprecated Use deleteByPriceDateBefore method.
	 */
	@Override
	@Deprecated
	public long deleteOlderThan(Date deleteBefore) {
		CriteriaDefinition crit = Criteria.where("priceDate").lt(deleteBefore);
		DeleteResult result = mongoTemplate.remove(Query.query(crit), StockAveragePrice.class);
		return result.getDeletedCount();
	}

	@Override
	public List<String> findUniqueTickerSymbols(Date deleteBefore) {
		List<String> tickerSymbolList = null;
		CriteriaDefinition crit = Criteria.where("priceDate").gte(deleteBefore);
		tickerSymbolList = mongoTemplate.findDistinct(Query.query(crit), "tickerSymbol", StockAveragePrice.class, String.class);
		return tickerSymbolList;
	}

}
