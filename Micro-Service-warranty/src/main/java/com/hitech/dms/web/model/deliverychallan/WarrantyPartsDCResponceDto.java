package com.hitech.dms.web.model.deliverychallan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class WarrantyPartsDCResponceDto {
	
	private BigInteger id;
	private String dcNo;
	private Date dcDate;
	private String transpoterName;
	private String lrNo;
	private Date lrDate;
	private BigDecimal baseAmount;
	private BigDecimal gstAmount;
	private BigDecimal totalAmount;
	
	private List<DcWcrItemListDto> dcDetailList;

}
