/**
 * 
 */
package com.hitech.dms.web.model.machinepo.cancel.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePOCancelRequestModel {
	private BigInteger poHdrId;
	private String poNumber;
	private BigInteger poCancelReason;
	private String poCancelRemarks;
}
