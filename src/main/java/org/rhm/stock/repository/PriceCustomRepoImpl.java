package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.DeleteResult;

public class PriceCustomRepoImpl implements PriceCustomRepo {
	@Autowired
	private MongoTemplate mongoTemplate = null;

	/**
	 * @deprecated Use deleteByPriceDateBefore method.
	 */
	@Deprecated
	@Override
	public long deleteOlderThan(Date deleteBefore) {
		CriteriaDefinition crit = Criteria.where("priceDate").lt(deleteBefore);
		DeleteResult result = mongoTemplate.remove(Query.query(crit), StockPrice.class);
		return result.getDeletedCount();
	}

	@Override
	public List<String> findUniqueTickerSymbols(Date deleteBefore) {
		List<String> tickerSymbolList = null;
		CriteriaDefinition crit = Criteria.where("priceDate").gte(deleteBefore);
		tickerSymbolList = mongoTemplate.findDistinct(Query.query(crit), "tickerSymbol", StockPrice.class, String.class);
		return tickerSymbolList;
	}

}
