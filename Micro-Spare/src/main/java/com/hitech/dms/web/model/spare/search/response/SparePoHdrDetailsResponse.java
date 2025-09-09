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
public class SparePoHdrDetailsResponse {
	private BigInteger poHdrId;
	private String branchName;
	private String poNumber;
	private String poStatus;
	private String pOType;
	private String productCategory;
	private String partyCode;
	private String partyName;
	private String poON;
	private String jobCardNo;
	private String remarks;
	private BigDecimal totelItem;
	private BigDecimal totalQty;
	private Date  poReleaseDate;
	private Date  POCreationDate;
	private String createdBy;
	private String rso;
	private BigDecimal totalBaseAmount;
	private BigDecimal totalGstAmount;	
	private String tcsPercent;
	private BigDecimal totalPoAmount;	
	private BigDecimal totalTcsAmount;
	private BigInteger branchId;
	private BigInteger dealerId;
	private Integer categoryId;

}
