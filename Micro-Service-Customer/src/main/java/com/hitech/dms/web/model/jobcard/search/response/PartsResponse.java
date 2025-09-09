/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class PartsResponse {
	private BigInteger requisitionId;
	private String biliableTypeDesc;
	private String partNumber;
	private String partDesc;
	private String partCategory;
	private BigDecimal requestedQty;
	private BigDecimal issuedQty;
	private BigDecimal mrp;
	private Integer oem;
	private Integer customer;
	private Integer dealer;
	private Integer insurance;

}
