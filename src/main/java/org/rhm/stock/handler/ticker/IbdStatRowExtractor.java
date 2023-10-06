package org.rhm.stock.handler.ticker;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.rhm.stock.domain.IbdStatistic;

public class IbdStatRowExtractor {
	/*
	 * Symbol, Company Name, Price, Price Chg., Price % Chg., Volume (000), Volume % Chg., EPS % Chg (Latest Qtr), EPS % Chg (Prior Qtr), Sale % Chg (Last Qtr), EPS Est % Chg (Currrent Qtr), EPS Est % Chg (Current Yr), Composite Rating, EPS Rating, RS Rating, SMR Rating, Acc/Dis Rating, Group Rel Str Rating
	 * Symbol, Company Name, Industry Group Rank, Price, Price Change, Price % Change, % off High, Volume, Volume % Change, Composite Rating, EPS Rating, RS Rating, SMR Rating, ACC/DIS Rating, Group Rel Str Rating, EPS % Change(Latest Qtr), EPS % Change(Prev Qtr), EPS EST % Change(Current Qtr), EPS EST % Change(Current Yr), Sales % Change(Last Qtr), Annual ROE, Annual Profit Margin (Latest Yr), Mgmt Own %, Qtrs of Rising Sponsorship
	 */

	private static final String STAT_SYMBOL = 	"Symbol"; 
	private static final String STAT_COMPOSITE = "Composite Rating";
	private static final String STAT_EPS = "EPS Rating";
	private static final String STAT_RS = "RS Rating";
	private static final String STAT_SMR = "SMR Rating";
	private static final String STAT_ACC_DIS = "Acc/Dis Rating";
	private static final String STAT_GRP = "Group Rel Str Rating";
	private static final String STAT_CO_NAME = "Company Name";
	private static final String STAT_MGMT_OWN_PCT = "Mgmt Own %";
	private static final int EXPECTED_FOUND_CNT = 8;
	
	private int columnIndex(List<String> columnNames, String columnName) {
		int col = -1;
		for (int i = 0; i < columnNames.size(); i++) {
			if (columnNames.get(i).equalsIgnoreCase(columnName)) {
				col = i;
				break;
			}
		}
		return col;
	}
	
	private boolean foundColumnNames(List<String> columnNames) {
		int foundCnt = 0;
		foundCnt += (this.columnIndex(columnNames, STAT_ACC_DIS) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_COMPOSITE) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_EPS) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_GRP) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_RS) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_SMR) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_CO_NAME) > -1)?1:0;
		foundCnt += (this.columnIndex(columnNames, STAT_MGMT_OWN_PCT) > -1)?1:0;
		return foundCnt == EXPECTED_FOUND_CNT;
	}
	
	public IbdStatistic transformRow(Row row, List<String> columnNames) {
		IbdStatistic ibdStat = null;
		if (this.foundColumnNames(columnNames)) {
			ibdStat = new IbdStatistic();
			ibdStat.setAccumDist(row.getCell(columnIndex(columnNames, STAT_ACC_DIS)).getStringCellValue());
			ibdStat.setCompositeRating(row.getCell(columnIndex(columnNames, STAT_COMPOSITE)).getStringCellValue());
			ibdStat.setEpsRating(row.getCell(columnIndex(columnNames, STAT_EPS)).getStringCellValue());
			ibdStat.setGroupStrength(row.getCell(columnIndex(columnNames, STAT_GRP)).getStringCellValue());
			ibdStat.setRelativeStrength(row.getCell(columnIndex(columnNames, STAT_RS)).getStringCellValue());
			ibdStat.setSalesMarginRoe(row.getCell(columnIndex(columnNames, STAT_SMR)).getStringCellValue());
			ibdStat.setTickerSymbol(row.getCell(columnIndex(columnNames, STAT_SYMBOL)).getStringCellValue());
			ibdStat.setCompanyName(row.getCell(columnIndex(columnNames, STAT_CO_NAME)).getStringCellValue());
			try {
				ibdStat.setMgmtOwnPct(Double.valueOf(row.getCell(columnIndex(columnNames, STAT_MGMT_OWN_PCT)).getStringCellValue()));
			}
			catch (NumberFormatException e) {
				ibdStat.setMgmtOwnPct(0.0);
			}
		}
		return ibdStat;
	}
}
