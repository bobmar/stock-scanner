package org.rhm.stock.repository;

import java.util.Date;
import java.util.List;

import org.rhm.stock.domain.StockSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.DeleteResult;

public class SignalCustomRepoImpl implements SignalCustomRepo {
	@Autowired
	private MongoTemplate mongoTemplate = null;
	
	private Logger logger = LoggerFactory.getLogger(SignalCustomRepoImpl.class);
	@Override
	public List<StockSignal> findSignalsByType(List<String> signalTypeList, Date priceDate) {
		CriteriaDefinition crit = Criteria.where("signalType").in(signalTypeList).and("priceDate").gt(priceDate);
		logger.debug("findSignalsByType - " + crit.getCriteriaObject().toJson());
		List<StockSignal> signalList = mongoTemplate.find(Query.query(crit), StockSignal.class);
		return signalList;
	}
	
	/**
	 * @deprecated Use deleteByPriceDateBefore method.
	 */
	@Deprecated
	@Override
	public long deleteOlderThan(Date deleteBefore) {
		CriteriaDefinition crit = Criteria.where("priceDate").lt(deleteBefore);
		DeleteResult result = mongoTemplate.remove(Query.query(crit), StockSignal.class);
		return result.getDeletedCount();
	}

	@Override
	public List<String> findUniqueTickerSymbols(Date deleteBefore) {
		List<String> tickerSymbolList = null;
		CriteriaDefinition crit = Criteria.where("priceDate").gte(deleteBefore);
		tickerSymbolList = mongoTemplate.findDistinct(Query.query(crit), "tickerSymbol", StockSignal.class, String.class);
		return tickerSymbolList;
	}

}
