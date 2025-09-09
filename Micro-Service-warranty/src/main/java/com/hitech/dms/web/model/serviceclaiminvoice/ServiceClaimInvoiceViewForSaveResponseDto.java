package com.hitech.dms.web.model.serviceclaiminvoice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ServiceClaimInvoiceViewForSaveResponseDto {
	
	private BigInteger claimId;
	
	private String claimNo;
	
	private Date claimDate;
	
	private BigDecimal basePrice;
	
	private BigDecimal gstAmount;
	
	private BigDecimal invoiceAmount;
	
	private List<SvClaimInvoiceoViewForSaveDtlResponseDto> dtlResponseDtos;
	
}
