/**
 * 
 */
package com.hitech.dms.web.model.spare.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePartDetailsResponse {
	private BigInteger poHdrId;
	private String partNo;
	private String partDescription;
	private String prodSubCat;
	private Integer packQty;
	private Integer minOrderQty;
	private Integer currentStock;
	private Integer backOrderQty;
	private Integer transitQty;
	private Integer mrpPrice;
	private Integer quantity;
	private BigDecimal netAmount;
	private BigDecimal gstPercent;
	private BigDecimal gstAmount;
	private Boolean deleteFlag;
	private BigDecimal totalAmount;
}
