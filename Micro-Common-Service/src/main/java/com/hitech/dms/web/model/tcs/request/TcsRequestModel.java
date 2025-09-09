/**
 * 
 */
package com.hitech.dms.web.model.tcs.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class TcsRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String isFor;
}
