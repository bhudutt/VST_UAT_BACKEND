package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.Pattern;

import lombok.Data;
@Data
public class PartyInvoiceList {
	
	private BigInteger saleInvoiceId;
	
	private String invoiceNumber;
	
	private Date invoiceDate;

}
