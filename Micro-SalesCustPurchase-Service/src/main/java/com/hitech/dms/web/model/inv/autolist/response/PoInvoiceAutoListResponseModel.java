/**
 * 
 */
package com.hitech.dms.web.model.inv.autolist.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PoInvoiceAutoListResponseModel {	
	private BigInteger poHdrId;
	private String poNumber;
	private String poStatus;
	private String poDate;
	private BigInteger dealerId;
	private BigInteger poToDealerId;
	private String displayValue;
}
