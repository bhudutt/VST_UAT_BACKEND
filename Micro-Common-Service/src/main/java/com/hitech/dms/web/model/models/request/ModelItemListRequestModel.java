/**
 * 
 */
package com.hitech.dms.web.model.models.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ModelItemListRequestModel {
	private Integer pcID;
	private BigInteger branchId;
	private Long modelID;
	private String searchValue;
	private String productGroup;
	private Long dealerId;
	private String isFor;
}
