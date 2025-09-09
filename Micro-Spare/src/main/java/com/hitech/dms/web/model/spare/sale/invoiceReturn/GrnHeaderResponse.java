package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class GrnHeaderResponse {
	
	
	BigInteger mrnHdrId;
	BigInteger branchId;
	String mrnNumber;
	String mrnDate;
	BigInteger partyId;
	String Status;
	BigInteger branchStoreId;
	String invoiceNumber;
	String invoiceDate;
	String remarks;
	String ClaimStatus;
	String grnFrom;
	String claimDate;
	String spareSaleReturnInvoiceNo;
	String spareSaleInvoiceDate;
	String claimType;
	
	
	

}
