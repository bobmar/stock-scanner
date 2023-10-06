package org.rhm.stock.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.rhm.stock.controller.dto.GeneralResponse;
import org.rhm.stock.controller.dto.StatListRequest;
import org.rhm.stock.controller.dto.StatListResponse;
import org.rhm.stock.controller.dto.StatMapRequest;
import org.rhm.stock.domain.StatisticType;
import org.rhm.stock.domain.StockStatistic;
import org.rhm.stock.service.StatisticService;
import org.rhm.stock.util.StockUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class StatisticsController {
	@Autowired
	private StatisticService statSvc = null;
	private Logger logger = LoggerFactory.getLogger(StatisticsController.class);
	@RequestMapping(value="/stocks/stat/type", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	private GeneralResponse createStatType(@RequestBody StatisticType statType) {
		GeneralResponse response = new GeneralResponse();
		if (statSvc.createStatType(statType) != null) {
			response.setMessageText("Created statistic type successfully: " + statType.getStatisticDesc());
		}
		return response;
	}

	@GetMapping("/stocks/stat/type/list")
	private List<StatisticType> retrieveStatTypeList() {
		return statSvc.retrieveStatTypeList();
	}
	
	@GetMapping("/stocks/stat/dashboardtype/list")
	private List<StatisticType> retrieveDashboardStatTypeList() {
		return statSvc.retrieveDashboardStatTypeList();
	}
	
	@RequestMapping(value="/stocks/stat/{tickerSymbol}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	private Map<String,List<StockStatistic>> retrieveStatMap(@PathVariable String tickerSymbol) {
		return statSvc.retrieveStatMap(tickerSymbol);
	}

	@PostMapping(value="/stocks/stat/datemap")
	private Map<String,List<StockStatistic>> retrieveStatMapByDate(@RequestBody StatMapRequest request) {
		logger.debug("retrieveStatMapByDate - " + request.getFromDate() + " through " + request.getToDate() + "/" + request.getStatType());
		Map<String,List<StockStatistic>> statMap = statSvc.retrieveStatMap(request.getFromDate(), request.getToDate(), request.getStatType());
		return statMap;
	}
	
	@PostMapping(value="/stocks/stat/list")
	private StatListResponse retrieveStatListByDate(@RequestBody StatListRequest request) {
		Date statDate = null;
		String searchDate = request.getStatDate();
		if (searchDate == null) {
			statDate = statSvc.findMaxDate().getPriceDate();
		}
		else {
			try {
				statDate = StockUtil.stringToDate(searchDate);
			} 
			catch (ParseException e) {
				logger.warn("retrieveStatListByDate - " + e.getMessage());
			}
		}
		logger.debug("retrieveStatListByDate - statDate=" + statDate.toString());
		StatListResponse response = new StatListResponse();
		List<StockStatistic> statList = statSvc.retrieveStatList(statDate, request.getStatCode());
		List<StockStatistic> returnList = null;
		if (request.getLowValue() != null && request.getHighValue() != null) {
			returnList = statList.stream().filter((stat)->{
				return (stat.getStatisticValue().doubleValue() > request.getLowValue() 
						&& stat.getStatisticValue().doubleValue() < request.getHighValue());})
					.collect(Collectors.toList());

		}
		else {
			returnList = statList;
		}
		if (request.getMaxResults() == null || request.getMaxResults() > returnList.size()) {
			response.setStatList(returnList);
		}
		else {
			response.setStatList(returnList.subList(0, request.getMaxResults()));
		}
		response.setStatDate(statDate);
		if (returnList.size() > 0) {
			response.setLowValue(returnList.get(0).getStatisticValue().doubleValue());
			if (returnList.size() > 1) {
				response.setHighValue(returnList.get(returnList.size() - 1).getStatisticValue().doubleValue());
			}
		}
		return response;
	}
	
	@PostMapping(value="/stocks/stat/bullbear/list")
	private ResponseEntity<Map<String,List<StockStatistic>>> retrieveBullBearStatList(@RequestBody StatListRequest request) {
		String searchDate = request.getStatDate();
		Date statDate = null;
		if (searchDate == null) {
			statDate = statSvc.findMaxDate().getPriceDate();
		}
		else {
			try {
				statDate = StockUtil.stringToDate(searchDate);
			} 
			catch (ParseException e) {
				logger.warn("retrieveBullBearStatList - " + e.getMessage());
			}
		}
		logger.debug("retrieveBullBearStatList - " + statDate.toString() + "/" + request.getStatCode());
		List<StockStatistic> statList = statSvc.retrieveStatList(statDate, request.getStatCode());
		Map<String,List<StockStatistic>> statMap = new HashMap<String,List<StockStatistic>>();
		if (statList.size() > 24) {
			statMap.put("bearishList", statList.subList(0, 12)
					);
			statMap.put("bullishList", statList.subList(statList.size() - 12, statList.size())
					.stream()
					.sorted((o1,o2)->{return o1.getStatisticValue().compareTo(o2.getStatisticValue()) * -1;})
					.collect(Collectors.toList())
					);
		}
		else {
			statMap.put("bullishList", statList
					.stream()
					.sorted((o1,o2)->{return o1.getStatisticValue().compareTo(o2.getStatisticValue()) * -1;})
					.collect(Collectors.toList())
					);
			statMap.put("bearishList", statList);
		}
		return new ResponseEntity<Map<String,List<StockStatistic>>>(statMap, HttpStatus.OK);
	}
}
