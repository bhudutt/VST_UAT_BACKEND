/**
 * 
 */
package com.hitech.dms.web.model.spare.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SearchPartsByCategoryRequest {
	private String partNumber;
	private BigInteger branchId;
	private Integer categoryId;

}
