package com.hitech.dms.web.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author vinay.gautam
 *
 */
public class ExcelImportManager {
	public List<String> getXLSHeaders(Workbook wb) {
		List<String> headers = new ArrayList<>();

		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(0);
		row.cellIterator().forEachRemaining(cell -> headers.add(cell.getStringCellValue()));
		return headers;
	}

	public String checkXLSValidity(String[] preDefinedColumns, List<String> headers)
			throws ExcelInvalidColumnException {
		StringBuilder msg = new StringBuilder();

		IntStream.range(0, preDefinedColumns.length - 1).forEach(fdc -> {
			if (!preDefinedColumns[fdc].equals(headers.get(fdc))) {
				throw new ExcelInvalidColumnException(
						"Invalid column at the position " + fdc + " Invalid Column Name is " + headers.get(fdc)
								+ "Expected Column is " + preDefinedColumns[fdc]);
			}
		});
		if (headers.size() > preDefinedColumns.length) {
			throw new ExcelInvalidColumnException("You have an extra column");
		} else if (headers.size() < preDefinedColumns.length) {
			throw new ExcelInvalidColumnException("You have an less columns than expected");
		}
		return msg.toString();
	}

}
