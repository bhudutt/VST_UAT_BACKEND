package com.hitech.dms.web.dao.sales.stockupload;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;

import lombok.Data;

@Data
public class StockUploadExcelDto {


	@ExcelCellName("CUSTOMER NAME")
	@ExcelCell(0)
    private String customerName;
	
	@ExcelCellName("MOBILE NO.")
	@ExcelCell(1)
    private String mobileNo;
	
	@ExcelCellName("EMAIL ID")
	@ExcelCell(2)
    private String emailId;
	
	@ExcelCellName("MODEL")
	@ExcelCell(3)
    private String model;
	
	@ExcelCellName("DISTRICT")
	@ExcelCell(4)
    private String district;
	
	@ExcelCellName("TEHSIL/TALUKA/MANDAL")
	@ExcelCell(5)
    private String tehsil;
	
	@ExcelCellName("STATE")
	@ExcelCell(6)
    private String state;
	


}
