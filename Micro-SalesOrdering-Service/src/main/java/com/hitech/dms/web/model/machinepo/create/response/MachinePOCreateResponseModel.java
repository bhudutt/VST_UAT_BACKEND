/**
 * 
 */
package com.hitech.dms.web.model.machinepo.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOCreateResponseModel {
	private BigInteger poHdrId;
	private String poNumber;
	private String msg;
	private Integer statusCode;
}
