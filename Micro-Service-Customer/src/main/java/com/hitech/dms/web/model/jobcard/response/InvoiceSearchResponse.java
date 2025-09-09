/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class InvoiceSearchResponse {
	private BigInteger invoiceHdrId;
	private String invoiceNumber;
	private Date invoiceDate;
}
