package org.rhm.stock.controller;

import org.rhm.stock.dto.CompositePrice;
import org.rhm.stock.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {
	@Autowired
	private PriceService priceSvc = null;

	@GetMapping(value="/stocks/price/latest/{tickerSymbol}")
	public CompositePrice findLatestPrice(@PathVariable String tickerSymbol) {
		CompositePrice cPrice = priceSvc.retrieveCurrentPrice(tickerSymbol.toUpperCase());
		return cPrice;
	}
}
