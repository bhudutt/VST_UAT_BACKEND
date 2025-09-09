/**
 * 
 */
package com.hitech.dms.web.model.spare.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class partSearchDetailsResponse {
	private int partId;
	private BigInteger pohdrId;
	private String partNumber;
	private String PartDesc;
	private String ProductSubCategory;
	private int PackQty;
	private int MinOrderQty;
	private int CurrentStock;
	private int BackOrderQty;
	private int TransitQty;
	private BigDecimal BasicUnitPrice;
	private Integer OrderQty;
	private BigDecimal TotalBasePrice;
	private BigDecimal GSTP;
	private String GSTAmount;
	private String Amount;
	private String SONO;
	private String SODate;
	private String SAPRemarks;
	private String ViewImage;
	private String InvoiceQty;
	private String HSNCode;
	private int partcategoryId; 

}
