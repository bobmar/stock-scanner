package org.rhm.stock;

import org.rhm.stock.batch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableCaching
public class StockDataLoader implements CommandLineRunner {
	@Autowired
	private ApplicationContext ctx;

	private static final Logger LOGGER = LoggerFactory.getLogger(StockDataLoader.class);
	public static void main(String...args) {
		SpringApplication app = new SpringApplication(StockDataLoader.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<BatchJob> jobList = new ArrayList<BatchJob>();
		for (String arg: args) {
			switch (arg) {
			case "AVGPRICE":
				jobList.add(ctx.getBean(AveragePriceCalculator.class));
				break;
			case "PRICELOAD":
				jobList.add(ctx.getBean(PriceLoaderJob.class));
				break;
			case "SIGSCAN":
				jobList.add(ctx.getBean(SignalScanJob.class));
				break;
			case "STATSCALC":
				jobList.add(ctx.getBean(StatisticsCalcJob.class));
				break;
			case "PRUNER":
				jobList.add(ctx.getBean(PrunerJob.class));
				break;
			case "SIGNALCNT":
				jobList.add(ctx.getBean(SignalCountJob.class));
				break;
			case "FINRATIO":
				jobList.add(ctx.getBean(FinancialRatioLoader.class));
				break;
			case "FINGROWTH":
				jobList.add(ctx.getBean(FinancialGrowthLoader.class));
				break;
			case "EMAPRICE":
				jobList.add(ctx.getBean(EmaPriceLoader.class));
				break;
			}
		}
		LocalDateTime startTime = LocalDateTime.now();
		for (BatchJob job: jobList) {
			LOGGER.info("run - batch job: " + job.getClass().getName());
			BatchStatus status = job.run();
			LOGGER.info("run - batch job status: " + status.getCompletionMsg());
			LOGGER.info("run - batch job start: " + status.getStartDate());
			LOGGER.info("run - batch job finish: " + status.getFinishDate());
		}
		LocalDateTime finishTime = LocalDateTime.now();
		LOGGER.info("run - batch start: " + startTime.toString());
		LOGGER.info("run - batch finish: " + finishTime.toString());
	}
}
