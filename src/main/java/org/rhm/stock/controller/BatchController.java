package org.rhm.stock.controller;

import java.util.Date;

import org.rhm.stock.batch.BatchJob;
import org.rhm.stock.batch.BatchStatus;
import org.rhm.stock.controller.dto.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {
	@Autowired
	@Qualifier("priceLoader")
	private BatchJob priceLoader = null;
	
	@Autowired
	@Qualifier("avgPriceCalc")
	private BatchJob avgPriceCalc = null;

	@Autowired
	@Qualifier("statsCalcJob")
	private BatchJob statsCalcJob = null;
	
	@Autowired
	@Qualifier("signalScan")
	private BatchJob signalScanJob = null;
	
	private Logger logger = LoggerFactory.getLogger(BatchController.class);

	@RequestMapping(value="/stocks/batch/price", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponse startPriceLoader() {
		GeneralResponse response = new GeneralResponse();
		BatchStatus status = priceLoader.run();
		response.setRequestDate(new Date());
		response.setMessageText("Trigger the price loader");
		response.setMessageCode("200");
		logger.debug("startPriceLoader - return response");
		return response;
	}
	
	@RequestMapping(value="/stocks/batch/avgcalc", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponse startAvgPriceCalc() {
		GeneralResponse response = new GeneralResponse();
		BatchStatus status = avgPriceCalc.run();
		response.setRequestDate(new Date());
		response.setMessageText("Trigger the average price calculator");
		response.setMessageCode("200");
		logger.debug("startAvgPriceCalc - return response");
		return response;
	}
	
	@RequestMapping(value="/stocks/batch/statscalc", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponse startStatsCalc() {
		GeneralResponse response = new GeneralResponse();
		BatchStatus status = statsCalcJob.run();
		response.setRequestDate(new Date());
		response.setMessageText("Trigger the statistics calculator");
		response.setMessageCode("200");
		logger.debug("startStatsCalc - return response");
		return response;
	}
	
	@RequestMapping(value="/stocks/batch/signalscan", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponse startSignalScan() {
		GeneralResponse response = new GeneralResponse();
		BatchStatus status = signalScanJob.run();
		response.setRequestDate(new Date());
		response.setMessageText("Trigger the signal scanner");
		response.setMessageCode("200");
		logger.debug("startSignalScan - return response");
		return response;
	}
}
