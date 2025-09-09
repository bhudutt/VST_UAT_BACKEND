/**
 * 
 */
package com.hitech.dms.web.model.SpareModel;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class partSerachDetailsByPohdrIdResponseModel {
	private int partId;
	private BigInteger pohdrId;
	private String partNumber;
	private String partDesc;
	private String productSubCategory;
	private partSearchResponseModel partDtl;
	private int packQty;
	private int minOrderQty;
	private int currentStock;
	private int backOrderQty;
	private int transitQty;
	private BigDecimal basicUnitPrice;
	private BigDecimal orderQty;
	private BigDecimal totalBasePrice;
	private BigDecimal GSTP;
	private BigDecimal GSTAmount;
	private BigDecimal amount;
	private String sONO;
	private String sODate;
	private String sAPRemarks;
	private String viewImage;

}
