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
public class MachineItemRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String searchValue;
	private Integer productDivisionId;
	private String productGrp;
	private String isFor;
	private String poOn;
	private Integer codealerId;
}
