/**
 * 
 */
package com.hitech.dms.web.model.spare.search.resquest;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoCancelRequestModel {
	
	private BigInteger poHdrId;
	private String poNumber;
	private Integer poCancelReasonId;
	private String poCancelRemarks;

}
