package org.rhm.stock.controller;

import org.rhm.stock.controller.dto.FileContent;
import org.rhm.stock.controller.dto.GeneralResponse;
import org.rhm.stock.controller.dto.TickerInfo;
import org.rhm.stock.controller.dto.TickerUploadResponse;
import org.rhm.stock.domain.IbdStatistic;
import org.rhm.stock.domain.StockTicker;
import org.rhm.stock.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
public class TickerController {
	@Autowired
	private TickerService tickerSvc = null;
	private Logger logger = LoggerFactory.getLogger(TickerController.class);
	@RequestMapping(value="/stocks/ticker/{tickerSymbol}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public GeneralResponse createTicker(@PathVariable String tickerSymbol) {
		GeneralResponse response = new GeneralResponse();
		response.setMessageText(tickerSvc.createTicker(tickerSymbol.toUpperCase()));
		response.setRequestDate(new Date());
		return response;
	}

	@CrossOrigin
	@PostMapping(value="/stocks/ticker/upload")
	public TickerUploadResponse uploadTickers(@RequestBody FileContent file) {
		TickerUploadResponse response = new TickerUploadResponse();
		logger.info("uploadTickers - " + file.getFileContent().substring(0, 50));
		String[] uploadPart = file.getFileContent().split(",");
		byte[] decodedStr = Base64.getDecoder().decode(uploadPart[1]);
		List<TickerInfo> tickerInfoList = tickerSvc.retrieveTickerInfo(decodedStr);
		List<TickerInfo> keepList = new ArrayList<TickerInfo>();
		List<TickerInfo> discardList = new ArrayList<TickerInfo>();
		for (TickerInfo info: tickerInfoList) {
			if (info.getStatus().equals("OK")) {
				keepList.add(info);
			}
			else {
				discardList.add(info);
			}
		}
		response.setFileName(file.getFileName());
		response.setKeep(keepList);
		response.setDiscard(discardList);
		response.setMessageText(file.getFileName());
		response.setRequestDate(new Date());
		return response;
	}

	@CrossOrigin
	@PostMapping(value="/stocks/ticker/list/save")
	public GeneralResponse saveTickerList(@RequestBody List<TickerInfo> tickerList) {
		GeneralResponse response = new GeneralResponse();
		int savedCnt = tickerSvc.saveTickerList(tickerList);
		response.setRequestDate(new Date());
		response.setMessageText(String.format("Saved %s tickers", savedCnt));
		return response;
	}
	
	@CrossOrigin
	@GetMapping(value="/stocks/ticker/page")
	public Page<StockTicker> retrieveTickerPage(Pageable pageable) {
		Page<StockTicker> tickerPage = tickerSvc.findPage(pageable);
		logger.debug("retrieveTickerPage - " + pageable.getClass().getName());
		return tickerPage;
	}
	
	@GetMapping(value="/stocks/ibdstat/{tickerSymbol}")
	public List<IbdStatistic> retrieveIbdStat(@PathVariable String tickerSymbol) {
		return tickerSvc.findIbdStats(tickerSymbol.toUpperCase());
	}

	@PostMapping(value = "/stocks/ticker/weeklyoptions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GeneralResponse> updateWeeklyOptions() {
		GeneralResponse response = new GeneralResponse();
		int weeklyCnt = tickerSvc.updateWeeklyOptions();
		response.setMessageText(String.format("Found %s tickers having weekly options", weeklyCnt));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
