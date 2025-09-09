/**
 * 
 */
package com.hitech.dms.web.model.quotation.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoResponseModel {
	private BigInteger quotationHDRId;
	private String quotationNumber;
	private String msg;
	private int statusCode;
}
