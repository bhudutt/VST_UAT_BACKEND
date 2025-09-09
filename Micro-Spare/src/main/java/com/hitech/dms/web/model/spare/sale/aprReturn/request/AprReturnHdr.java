package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
@Data
public class AprReturnHdr {
	
	private BigInteger aprReturedId;
	
	private BigInteger branchId;
	
	private String aprReturedDocNo;
	
	private LocalDate aprReturnDate;
	
	private BigInteger partyId;
	
	private BigInteger invoiceId;
	
	private String aprReturnStatus;
	
	private BigInteger deliveryChallanId;
	
	private LocalDate createdDate;
	
	private BigInteger createdBy;
	
	private String invoiceNumber;

}
