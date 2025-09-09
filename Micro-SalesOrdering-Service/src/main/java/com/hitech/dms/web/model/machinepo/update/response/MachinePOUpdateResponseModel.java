/**
 * 
 */
package com.hitech.dms.web.model.machinepo.update.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePOUpdateResponseModel {
	private BigInteger poHdrId;
	private String poNumber;
	private String msg;
	private Integer statusCode;
}
