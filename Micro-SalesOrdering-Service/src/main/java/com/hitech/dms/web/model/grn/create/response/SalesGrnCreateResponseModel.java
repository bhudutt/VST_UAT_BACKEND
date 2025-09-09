/**
 * 
 */
package com.hitech.dms.web.model.grn.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class SalesGrnCreateResponseModel {
	private BigInteger grnId;
	private String grnNumber;
	private String msg;
	private Integer statusCode;
}
