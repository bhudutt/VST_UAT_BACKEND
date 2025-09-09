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
public class MachineItemDTLRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer productDivisionId;
	private String itemNumber;
	private BigInteger placedToDealerId;
	private Integer plantRsoId;
}
